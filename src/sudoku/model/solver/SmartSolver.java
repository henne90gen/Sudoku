package sudoku.model.solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.controller.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

public class SmartSolver extends Solver {

	private List<Integer>[] notesListGrid;

	public SmartSolver(ISudokuController controller, SudokuModel sudoku) {
		super(controller, sudoku, SolverType.SmartSolver);
	}

	@Override
	protected void useSolver() {
		// This only needs to be done once.
		if (runCounter == 0) {
			fillNotesListGrid();
		}

		// FIXME this is only temporary to prevent SmartSolver to get stuck in
		// an infinite loop
		if (runCounter > 2) {
			stop();
		}

		useDumbNotesListSolving();

		useSmartNotesListSolving();
	}

	@Override
	protected void resetSolver() {
		notesListGrid = new ArrayList[81];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (sudoku.isFieldEditable(row, col)) {
					notesListGrid[row * 9 + col] = new ArrayList<>();
				} else {
					notesListGrid[row * 9 + col] = null;
				}
			}
		}
	}

	private void useSmartNotesListSolving() {
		// TODO implement something ;)
	}

	/**
	 * Creates the notes list grid and finds the easiest solution numbers. For
	 * example if there is only one number in a notes list or if there is only
	 * one of a certain number in a row, column or block.
	 */
	private void useDumbNotesListSolving() {
		SudokuPosition position = new SudokuPosition(0, 0);
		do {
			List<Integer> notes = getNotesList(position);
			if (notes != null) {
				if (notes.size() == 1) {
					placeNumberAndResetPosition(position, notes.get(0));
					continue;
				}
				for (Integer currentNumber : notes) {
					if (checkNumberInColumn(position, currentNumber) || checkNumberInRow(position, currentNumber)
							|| checkNumberInBlock(position, currentNumber)) {
						placeNumberAndResetPosition(position, currentNumber);
						break;
					}
				}
			}
		} while (position.moveForward());
	}

	/**
	 * Places a number in the specified cell, removes this number from the notes
	 * lists of all relevant cells and resets the position back to the start
	 *
	 * @param position
	 *            Position of the cell
	 * @param number
	 *            Number that's going to be placed
	 */
	private void placeNumberAndResetPosition(SudokuPosition position, Integer number) {
		setNumber(position, number);
		setNotesList(position, null);

		removeNumberFromRowNotes(position, number);
		removeNumberFromColumnNotes(position, number);
		removeNumberFromBlockNotes(position, number);

		// set position back to the start of the sudoku
		position.setRow(0);
		// compensating for moveForward at the end of do/while loop
		position.setCol(-1);
	}

	/**
	 * Goes through each cell of the row and removes the number from the notes
	 * lists of those cells.
	 *
	 * @param position
	 *            Position inside the row that's going to be checked
	 * @param number
	 *            Number which is going to be removed
	 */
	private void removeNumberFromRowNotes(SudokuPosition position, Integer number) {
		SudokuPosition currentPosition = position.getCopy();
		currentPosition.setCol(0);
		do {
			removeNumberFromCellsNotes(currentPosition, number);
		} while (currentPosition.moveRight());
	}

	/**
	 * Goes through each cell of the column and removes the number from the
	 * notes lists of those cells.
	 *
	 * @param position
	 *            Position inside the column that's going to be checked
	 * @param number
	 *            Number which is going to be removed
	 */
	private void removeNumberFromColumnNotes(SudokuPosition position, Integer number) {
		SudokuPosition currentPosition = position.getCopy();
		currentPosition.setRow(0);
		do {
			removeNumberFromCellsNotes(currentPosition, number);
		} while (!currentPosition.moveDown());
	}

	/**
	 * Goes through each cell of the block and removes the number from the notes
	 * lists of those cells.
	 *
	 * @param position
	 *            Position inside the block that's going to be checked
	 * @param number
	 *            Number which is going to be removed
	 */
	private void removeNumberFromBlockNotes(SudokuPosition position, Integer number) {
		// Moving currentPosition to the top left corner of the block it belongs
		// to
		int topLeftRow = position.getRow() / 3 * 3;
		int topLeftCol = position.getCol() / 3 * 3;
		SudokuPosition currentPosition = new SudokuPosition(topLeftRow, topLeftCol);
		while (currentPosition.getRow() < topLeftRow + 3) {
			removeNumberFromCellsNotes(currentPosition, number);

			boolean wasAbleToMoveRight = currentPosition.moveRight();
			if (!wasAbleToMoveRight) {
				for (int i = 0; i < 2; i++)
					currentPosition.moveLeft();
				if (!currentPosition.moveDown()) {
					break;
				}
			} else if (currentPosition.getCol() >= topLeftCol + 3) {
				for (int i = 0; i < 3; i++)
					currentPosition.moveLeft();
				if (!currentPosition.moveDown()) {
					break;
				}
			}
		}
	}

	/**
	 * Removes the given number from the cells notes list
	 *
	 * @param position
	 *            Position of the cell on which this operation is performed
	 * @param number
	 *            Number which is going to be removed
	 */
	private void removeNumberFromCellsNotes(SudokuPosition position, Integer number) {
		List<Integer> notes = getNotesList(position);
		if (notes != null) {
			for (int i = 0; i < notes.size(); i++) {
				if (notes.get(i) == number) {
					notes.remove(i);
					if (notes.size() == 0) {
						setNotesList(position, null);
					}
					break;
				}
			}
		}
	}

	/**
	 * Goes through the row of the cell and checks each cells notesList for the
	 * given number, if the number is not found, we can place it
	 *
	 * @param position
	 *            Cell that is going to be checked against
	 * @param number
	 *            Number that we try to place in the cell
	 * @return True if the number can be placed in the cell, false otherwise
	 */
	public boolean checkNumberInRow(SudokuPosition position, int number) {
		SudokuPosition currentPosition = position.getCopy();
		currentPosition.setCol(0);
		do {
			if (currentPosition.getCol() != position.getCol()) {
				if (checkIsNumberInCell(currentPosition, number))
					return false;
			}
		} while (currentPosition.moveRight());
		return true;
	}

	/**
	 * Goes through the column of the cell and checks each cells notesList for
	 * the given number, if the number is not found, we can place it
	 *
	 * @param position
	 *            Cell that is going to be checked against
	 * @param number
	 *            Number that we try to place in the cell
	 * @return True if the number can be placed in the cell, false otherwise
	 */
	public boolean checkNumberInColumn(SudokuPosition position, int number) {
		SudokuPosition currentPosition = position.getCopy();
		currentPosition.setRow(0);
		do {
			if (position.getRow() != currentPosition.getRow()) {
				if (checkIsNumberInCell(currentPosition, number))
					return false;
			}
		} while (currentPosition.moveDown());
		return true;
	}

	/**
	 * Goes through the block of the cell and checks each cells notesList for
	 * the given number, if the number is not found, we can place it
	 *
	 * @param position
	 *            Cell that is going to be checked against
	 * @param number
	 *            Number that we try to place in the cell
	 * @return True if the number can be placed in the cell, false otherwise
	 */
	public boolean checkNumberInBlock(SudokuPosition position, int number) {
		// Moving currentPosition to the top left corner of the block it belongs
		// to
		int topLeftRow = position.getRow() / 3 * 3;
		int topLeftCol = position.getCol() / 3 * 3;
		SudokuPosition currentPosition = new SudokuPosition(topLeftRow, topLeftCol);
		while (currentPosition.getRow() < topLeftRow + 3 && currentPosition.getCol() < topLeftCol + 3) {
			if (!(currentPosition.getRow() == position.getRow() && currentPosition.getCol() == position.getCol())) {
				if (checkIsNumberInCell(currentPosition, number))
					return false;
			}

			if (!currentPosition.moveRight()) {
				currentPosition.setCol(9);
			}
			if (currentPosition.getCol() >= topLeftCol + 3) {
				for (int i = 0; i < 3; i++)
					currentPosition.moveLeft();
				if (!currentPosition.moveDown()) {
					break;
				}
			}
		}
		return true;
	}

	/**
	 * Checks a cell for a number. Looks in the cell itself and in the cells
	 * notes list
	 *
	 * @param currentPosition
	 *            Cell position
	 * @param number
	 *            Number to check against
	 * @return True if the number is either directly in the cell or in the notes
	 *         corresponding to the cell.
	 */
	private boolean checkIsNumberInCell(SudokuPosition currentPosition, int number) {
		List<Integer> notesList = getNotesList(currentPosition);
		if (notesList != null) {
			for (int i = 0; i < notesList.size(); i++) {
				if (notesList.get(i) == number) {
					return true;
				}
			}
		} else if (getNumber(currentPosition) == number) {
			return true;
		}
		return false;
	}

	/**
	 * Scans the whole grid and fills each cells notes list
	 */
	public void fillNotesListGrid() {
		SudokuPosition position = new SudokuPosition(0, 0);
		do {
			fillNotesList(position);
		} while (position.moveForward());
	}

	/**
	 * Fills a cells notes list by going through 1-9 and checking if those
	 * numbers are already in the row, column or block of the cell
	 *
	 * @param pos
	 *            Position of the cell who's notes list is going to be filled
	 */
	private void fillNotesList(SudokuPosition pos) {
		SudokuPosition position = pos.getCopy();
		if (getNumber(position) == 0) {
			List<Integer> notes = new ArrayList<>();
			for (int i = 1; i < 10; i++) {
				setNumber(position, i);
				if (validateRow(position.getRow()) && validateColumn(position.getCol())
						&& validateBlock(position.getRow(), position.getCol())) {
					notes.add(i);
				}
				setNumber(position, 0);
			}
			setNotesList(position, notes);
		} else {
			setNotesList(position, null);
		}
	}

	/**
	 * @param position
	 *            Position of the cell for which the notes are being requested
	 * @return List of numbers that can currently be placed in this cell
	 */
	public List<Integer> getNotesList(SudokuPosition position) {
		return notesListGrid[position.getRow() * 9 + position.getCol()];
	}

	/**
	 * Sets the notes list for a cell. Set to null if there is a number in the
	 * cell.
	 *
	 * @param position
	 *            Position of the cell for which the notes are being set
	 * @param notesList
	 *            List of numbers that can currently be placed in this cell
	 */
	private void setNotesList(SudokuPosition position, List<Integer> notesList) {
		notesListGrid[position.getRow() * 9 + position.getCol()] = notesList;
	}
}
