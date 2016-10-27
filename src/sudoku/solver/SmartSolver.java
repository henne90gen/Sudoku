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
                notesListGrid[row * 9 + col] = new ArrayList<>();
            }
        }
    }

    @Override
    protected void startSolving() {
        do {
            scanPossibilities(new SudokuPosition(0, 0));
        } while (placeSingleNumber(new SudokuPosition(0, 0)));

        nextPossibleCell(new SudokuPosition(0, 0));
    }

    private boolean nextPossibleCell(SudokuPosition pos) {
        SudokuPosition position = new SudokuPosition(pos.getRow(), pos.getCol());
        scanPossibilities(new SudokuPosition(0, 0));
        if (getNumber(position) == 0) {
            if (getNotesList(position).size() == 0) {
                scanPossibilities(new SudokuPosition(0, 0));
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
        scanPossibilities(new SudokuPosition(0, 0));
        if (getNotesList(position) != null && getNotesList(position).size() > index) {
            setNumber(position, getNotesList(position).get(index));
            logSetNumber(position, getNotesList(position).get(index));
            scanPossibilities(new SudokuPosition(0, 0));
        } else {
            setNumber(position, 0);
            scanPossibilities(new SudokuPosition(0, 0));
            return false;
        }
        return nextPossibleNumber(position, ++index);
    }

    private boolean placeSingleNumber(SudokuPosition pos) {
        SudokuPosition position = new SudokuPosition(pos.getRow(), pos.getCol());
        if (getNotesList(position) != null) {
            for (int j = 0; j < getNotesList(position).size(); j++) {
                if (checkNumberInColumn(position.getRow(), position.getCol(), getNotesList(position).get(j)) ||
                        checkNumberInRow(position.getRow(), position.getCol(), getNotesList(position).get(j)) ||
                        checkNumberInBlock(position.getRow(), position.getCol(), getNotesList(position).get(j))) {
                    setNumber(position, getNotesList(position).get(j));
                    logSetNumber(position, getNotesList(position).get(j));
                    return true;
                }
            }
        }
        return position.moveRight() && placeSingleNumber(position);
    }

    private boolean checkNumberInRow(int row, int col, int number) {
        // TODO refactor this
        for (int i = 0; i < 9; i++) {
            if (getNotesList(new SudokuPosition(row, i)) != null) {
                for (int j = 0; j < getNotesList(new SudokuPosition(row, i)).size(); j++) {
                    if (getNotesList(new SudokuPosition(row, i)).get(j) == number && col != i) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkNumberInColumn(int row, int col, int number) {
        // TODO refactor this
        for (int i = 0; i < 9; i++) {
            if (getNotesList(new SudokuPosition(i, col)) != null) {
                for (int j = 0; j < getNotesList(new SudokuPosition(i, col)).size(); j++) {
                    if (getNotesList(new SudokuPosition(i, col)).get(j) == number && row != i) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkNumberInBlock(int row, int col, int number) {
        // TODO refactor this
        int realCol = col;
        int realRow = row;
        col = col / 3;
        row = row / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int currentRow = j + row * 3;
                int currentCol = i + col * 3;
                if (getNotesList(new SudokuPosition(currentRow, currentCol)) != null) {
                    for (int k = 0; k < getNotesList(new SudokuPosition(currentRow, currentCol)).size(); k++) {
                        if (getNotesList(new SudokuPosition(currentRow, currentCol)).get(k) == number) {
                            if (realCol != currentCol && realRow != currentRow) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void scanPossibilities(SudokuPosition pos) {
        SudokuPosition position = new SudokuPosition(pos.getRow(), pos.getCol());
        if (position.getCol() == 0 && position.getRow() == 0) {
            resetSolver();
        }
        if (getNumber(position) == 0) {
            setNotesList(position, new ArrayList<>());
            for (int k = 1; k < 10; k++) {
                setNumber(position, k);
                if (validateRow(position.getRow()) && validateColumn(position.getCol()) && validateBlock(position
                        .getRow(), position.getCol())) {
                    getNotesList(position).add(k);
                }
                setNumber(position, 0);
            }
            if (getNotesList(position).size() == 1) {
                setNumber(position, getNotesList(position).get(0));
                logSetNumber(position, getNotesList(position).get(0));
                setNotesList(position, null);
                scanPossibilities(new SudokuPosition(0, 0));
            }
        } else {
            setNotesList(position, null);
        }
        if (!position.moveRight()) {
            return;
        }
        scanPossibilities(position);
    }

    private List<Integer> getNotesList(SudokuPosition position) {
        return notesListGrid[position.getRow() * 9 + position.getCol()];
    }

    private void setNotesList(SudokuPosition position, List<Integer> grid) {
        notesListGrid[position.getRow() * 9 + position.getCol()] = grid;
    }
}
