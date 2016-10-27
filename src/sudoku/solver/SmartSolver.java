package sudoku.solver;

import sudoku.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

import java.util.ArrayList;
import java.util.List;

public class SmartSolver extends Solver {

    private List<Integer>[] possibilityGrid;

    public SmartSolver(ISudokuController controller, SudokuModel sudoku) {
        super(controller, sudoku, SolverType.SmartSolver);
        resetSolver();
    }

    protected void resetSolver() {
        //noinspection unchecked
        possibilityGrid = new ArrayList[81];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                possibilityGrid[row * 9 + col] = new ArrayList<>();
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
            if (possibilityGrid[position.getRow() * 9 + position.getCol()].size() == 0) {
                scanPossibilities(new SudokuPosition(0, 0));
                if (possibilityGrid[position.getRow() * 9 + position.getCol()].size() == 0) {
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
        if (possibilityGrid[position.getRow() * 9 + position.getCol()] != null && possibilityGrid[position.getRow() *
                9 + position.getCol()].size() > index) {
            setNumber(position, possibilityGrid[position.getRow() * 9 + position.getCol()].get(index));
            logSetNumber(position, possibilityGrid[position.getRow() * 9 + position.getCol()].get(index));
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
        if (possibilityGrid[position.getRow() * 9 + position.getCol()] != null) {
            for (int j = 0; j < possibilityGrid[position.getRow() * 9 + position.getCol()].size(); j++) {
                if (checkNumberInColumn(position.getRow(), position.getCol(), possibilityGrid[position.getRow() * 9 +
                        position.getCol()].get(j)) ||
                        checkNumberInRow(position.getRow(), position.getCol(), possibilityGrid[position.getRow() * 9
                                + position.getCol()].get(j)) ||
                        checkNumberInBlock(position.getRow(), position.getCol(), possibilityGrid[position.getRow() *
                                9 + position.getCol()].get(j))) {
                    setNumber(position, possibilityGrid[position.getRow() * 9 + position.getCol()].get(j));
                    logSetNumber(position, possibilityGrid[position.getRow() * 9 + position.getCol()].get(j));
                    return true;
                }
            }
        }
        return position.moveRight() && placeSingleNumber(position);
    }

    private boolean checkNumberInRow(int row, int col, int number) {
        for (int i = 0; i < 9; i++) {
            if (possibilityGrid[row * 9 + i] != null) {
                for (int j = 0; j < possibilityGrid[row * 9 + i].size(); j++) {
                    if (possibilityGrid[row * 9 + i].get(j) == number && col != i) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkNumberInColumn(int row, int col, int number) {
        for (int i = 0; i < 9; i++) {
            if (possibilityGrid[i * 9 + col] != null) {
                for (int j = 0; j < possibilityGrid[i * 9 + col].size(); j++) {
                    if (possibilityGrid[i * 9 + col].get(j) == number && row != i) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkNumberInBlock(int row, int col, int number) {
        int realCol = col;
        int realRow = row;
        col = col / 3;
        row = row / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (possibilityGrid[(j + row * 3) * 9 + (i + col * 3)] != null) {
                    for (int k = 0; k < possibilityGrid[(j + row * 3) * 9 + (i + col * 3)].size(); k++) {
                        if (possibilityGrid[(j + row * 3) * 9 + (i + col * 3)].get(k) == number) {
                            if (realCol != i + col * 3 && realRow != j + row * 3) {
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
            possibilityGrid[position.getRow() * 9 + position.getCol()] = new ArrayList<>();
            for (int k = 1; k < 10; k++) {
                setNumber(position, k);
                if (validateRow(position.getRow()) && validateColumn(position.getCol()) && validateBlock(position
                        .getRow(), position.getCol())) {
                    possibilityGrid[position.getRow() * 9 + position.getCol()].add(k);
                }
                setNumber(position, 0);
            }
            if (possibilityGrid[position.getRow() * 9 + position.getCol()].size() == 1) {
                setNumber(position, possibilityGrid[position.getRow() * 9 + position.getCol()].get(0));
                logSetNumber(position, possibilityGrid[position.getRow() * 9 + position.getCol()].get(0));
                possibilityGrid[position.getRow() * 9 + position.getCol()] = null;
                scanPossibilities(new SudokuPosition(0, 0));
            }
        } else {
            possibilityGrid[position.getRow() * 9 + position.getCol()] = null;
        }
        if (!position.moveRight()) {
            return;
        }
        scanPossibilities(position);
    }
}
