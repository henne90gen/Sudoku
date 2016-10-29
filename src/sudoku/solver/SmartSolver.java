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

    protected void resetSolver() {
        //noinspection unchecked
        notesListGrid = new ArrayList[81];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (sudoku.isFieldEditable(row, col)) {
                    notesListGrid[row * 9 + col] = new ArrayList<>();
                }
            }
        }
    }

    @Override
    protected void startSolving() {
        do {
            scanGrid();
        } while (placeSingleNumber(new SudokuPosition(0, 0)));

        nextPossibleCell(new SudokuPosition(0, 0));
    }

    private boolean nextPossibleCell(SudokuPosition pos) {
        SudokuPosition position = new SudokuPosition(pos.getRow(), pos.getCol());
        scanGrid();
        if (getNumber(position) == 0) {
            if (getNotesList(position).size() == 0) {
                scanGrid();
                if (getNotesList(position).size() == 0) {
                    return false;
                }
            }
            if (nextPossibleNumber(position, 0)) {
                if (!position.moveRight()) {
                    return true;
                }
                if (!nextPossibleCell(position)) {
                    position.moveLeft();
                    return nextPossibleCell(position);
                }
                return false;
            }
            return false;
        }
        return !position.moveRight() || nextPossibleCell(position);
    }

    private boolean nextPossibleNumber(SudokuPosition pos, int index) {
        SudokuPosition position = new SudokuPosition(pos.getRow(), pos.getCol());
        scanGrid();
        if (getNotesList(position) != null && getNotesList(position).size() > index) {
            setNumber(position, getNotesList(position).get(index));
            logSetNumber(position, getNotesList(position).get(index));
            scanGrid();
        } else {
            setNumber(position, 0);
            scanGrid();
            return false;
        }
        return nextPossibleNumber(position, ++index);
    }

    private boolean placeSingleNumber(SudokuPosition pos) {
        SudokuPosition position = new SudokuPosition(pos.getRow(), pos.getCol());
        if (getNotesList(position) != null) {
            for (int j = 0; j < getNotesList(position).size(); j++) {
                if (checkNumberInColumn(position, getNotesList(position).get(j)) ||
                        checkNumberInRow(position, getNotesList(position).get(j)) ||
                        checkNumberInBlock(position, getNotesList(position).get(j))) {
                    setNumber(position, getNotesList(position).get(j));
                    logSetNumber(position, getNotesList(position).get(j));
                    return true;
                }
            }
        }
        return position.moveRight() && placeSingleNumber(position);
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
        SudokuPosition currentPosition = new SudokuPosition(position.getRow(), 0);
        while (currentPosition.getRow() == position.getRow()) {
            if (currentPosition.getCol() != position.getCol()) {
                if (checkIsNumberInCell(currentPosition, number)) return false;
            }
            currentPosition.moveRight();
        }
        return true;
    }

    /**
     * Goes through the colunm of the cell and checks each cells notesList for the given number, if the number is not
     * found, we can place it
     *
     * @param position Cell that is going to be checked against
     * @param number   Number that we try to place in the cell
     * @return True if the number can be placed in the cell, false otherwise
     */
    public boolean checkNumberInColumn(SudokuPosition position, int number) {
        SudokuPosition currentPosition = new SudokuPosition(0, position.getCol());
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

    public void scanCell(SudokuPosition pos) {
        SudokuPosition position = new SudokuPosition(pos.getRow(), pos.getCol());
        if (sudoku.isFieldEditable(position.getRow(), position.getCol())) {
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

    public void setNotesList(SudokuPosition position, List<Integer> grid) {
        notesListGrid[position.getRow() * 9 + position.getCol()] = grid;
    }
}
