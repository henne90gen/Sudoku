package sudoku.solver;

import sudoku.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

import java.util.ArrayList;
import java.util.List;

public class SmartSolver extends Solver {

    private List<Integer>[] notesListGrid;

    public SmartSolver(ISudokuController controller, SudokuModel sudoku) {
        super(controller, sudoku, SolverType.SmartSolver);
        resetSolver();
    }

    @Override
    protected void resetSolver() {
        //noinspection unchecked
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

    @Override
    protected void startSolving() {
        SudokuPosition position = new SudokuPosition(0, 0);
        scanGrid();
        do {
            List<Integer> notes = getNotesList(position);
            if (notes != null) {
                if (notes.size() == 1) {
                    placeNumberAndResetPosition(position, notes.get(0));
                    continue;
                }
                for (Integer currentNumber : notes) {
                    if (checkNumberInColumn(position, currentNumber) || checkNumberInRow(position, currentNumber) ||
                            checkNumberInBlock(position, currentNumber)) {
                        placeNumberAndResetPosition(position, currentNumber);
                        break;
                    }
                }
            }
        } while (position.moveRight());
    }

    private SudokuPosition placeNumberAndResetPosition(SudokuPosition position, Integer number) {
        setNumber(position, number);
        // FIXME replace scanGrid with a more efficient algorithm
//        scanGrid();
        removeNumberFromNotes(position, number);
        position.setRow(0);
        position.setCol(-1);
        return position;
    }

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

    // FIXME gets stuck in an infinite loop
    private void removeNumberFromNotes(SudokuPosition position, Integer number) {
        SudokuPosition currentPosition = position.getCopy();
        // go through row
        currentPosition.setCol(0);
        while (currentPosition.getRow() == position.getRow()) {

            removeNumberFromCellsNotes(currentPosition, number);

            if (!currentPosition.moveRight()) {
                break;
            }
        }

        // go through column
        currentPosition = position.getCopy();
        currentPosition.setRow(0);
        while (currentPosition.getCol() == position.getCol()) {
            removeNumberFromCellsNotes(currentPosition, number);
            if (!currentPosition.moveDown()) {
                break;
            }
        }

        // go through block
        // Moving currentPosition to the top left corner of the block it belongs to
        int topLeftRow = position.getRow() / 3 * 3;
        int topLeftCol = position.getCol() / 3 * 3;
        currentPosition = new SudokuPosition(topLeftRow, topLeftCol);
        while (currentPosition.getRow() < topLeftRow + 3 && currentPosition.getCol() < topLeftCol + 3) {
            removeNumberFromCellsNotes(currentPosition, number);
            currentPosition.moveRight();
            if (currentPosition.getCol() >= topLeftCol + 3) {
                for (int i = 0; i < 3; i++) currentPosition.moveLeft();
                if (currentPosition.moveDown()) {
                    break;
                }
            }
        }
    }

    /**
     * Goes through the row of the cell and checks each cells notesList for the given number, if the number is not
     * found, we can place it
     *
     * @param position Cell that is going to be checked against
     * @param number   Number that we try to place in the cell
     * @return True if the number can be placed in the cell, false otherwise
     */
    public boolean checkNumberInRow(SudokuPosition position, int number) {
        SudokuPosition currentPosition = position.getCopy();
        currentPosition.setCol(0);
        while (currentPosition.getRow() == position.getRow()) {
            if (currentPosition.getCol() != position.getCol()) {
                if (checkIsNumberInCell(currentPosition, number)) return false;
            }
            currentPosition.moveRight();
        }
        return true;
    }

    /**
     * Goes through the column of the cell and checks each cells notesList for the given number, if the number is not
     * found, we can place it
     *
     * @param position Cell that is going to be checked against
     * @param number   Number that we try to place in the cell
     * @return True if the number can be placed in the cell, false otherwise
     */
    public boolean checkNumberInColumn(SudokuPosition position, int number) {
        SudokuPosition currentPosition = position.getCopy();
        currentPosition.setRow(0);
        while (currentPosition.getCol() == position.getCol()) {
            if (position.getRow() != currentPosition.getRow()) {
                if (checkIsNumberInCell(currentPosition, number)) return false;
            }
            currentPosition.moveDown();
        }
        return true;
    }

    /**
     * Goes through the block of the cell and checks each cells notesList for the given number, if the number is not
     * found, we can place it
     *
     * @param position Cell that is going to be checked against
     * @param number   Number that we try to place in the cell
     * @return True if the number can be placed in the cell, false otherwise
     */
    public boolean checkNumberInBlock(SudokuPosition position, int number) {
        // Moving currentPosition to the top left corner of the block it belongs to
        int topLeftRow = position.getRow() / 3 * 3;
        int topLeftCol = position.getCol() / 3 * 3;
        SudokuPosition currentPosition = new SudokuPosition(topLeftRow, topLeftCol);
        while (currentPosition.getRow() < topLeftRow + 3 && currentPosition.getCol() < topLeftCol + 3) {
            if (!(currentPosition.getRow() == position.getRow() && currentPosition.getCol() == position.getCol())) {
                if (checkIsNumberInCell(currentPosition, number)) return false;
            }
            currentPosition.moveRight();
            if (currentPosition.getCol() >= topLeftCol + 3) {
                for (int i = 0; i < 3; i++) currentPosition.moveLeft();
                currentPosition.moveDown();
            }
        }
        return true;
    }

    /**
     * Checks a cell for a number.
     *
     * @param currentPosition Cell position
     * @param number          Number to check against
     * @return True if the number is either directly in the cell or in the notes corresponding to the cell.
     */
    private boolean checkIsNumberInCell(SudokuPosition currentPosition, int number) {
        if (getNotesList(currentPosition) != null) {
            for (int i = 0; i < getNotesList(currentPosition).size(); i++) {
                if (getNotesList(currentPosition).get(i) == number) {
                    return true;
                }
            }
        } else if (getNumber(currentPosition) == number) {
            return true;
        }
        return false;
    }

    public void scanGrid() {
        SudokuPosition position = new SudokuPosition(0, 0);
        do {
            scanCell(position);
        } while (position.moveRight());
    }

    private void scanCell(SudokuPosition pos) {
        SudokuPosition position = pos.getCopy();
        if (getNumber(position) == 0) {
            List<Integer> notes = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                setNumber(position, i);
                if (validateRow(position.getRow()) && validateColumn(position.getCol()) && validateBlock(position
                        .getRow(), position.getCol())) {
                    notes.add(i);
                }
                setNumber(position, 0);
            }
            setNotesList(position, notes);
        } else {
            setNotesList(position, null);
        }
    }

    public List<Integer> getNotesList(SudokuPosition position) {
        return notesListGrid[position.getRow() * 9 + position.getCol()];
    }

    private void setNotesList(SudokuPosition position, List<Integer> grid) {
        notesListGrid[position.getRow() * 9 + position.getCol()] = grid;
    }
}
