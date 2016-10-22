package sudoku.solver;

import sudoku.SudokuController;
import sudoku.SudokuModel;

import java.util.ArrayList;
import java.util.List;

public class SmartSolver extends Solver {

    private List<Integer>[] possibilityGrid;

    public SmartSolver(SudokuController controller, SudokuModel sudoku) {
        super(controller, sudoku, SolverType.SmartSolver);
        resetPossibilityGrid();
    }

    private void resetPossibilityGrid() {
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
            scanPossibilities(0, 0);
        } while (placeSingleNumber(0, 0));

        nextPossibleCell(0, 0);
    }

    private boolean nextPossibleCell(int row, int col) {
        scanPossibilities(0, 0);
        if (sudoku.getNumber(solverType, row, col) == 0) {
            if (possibilityGrid[row * 9 + col].size() == 0) {
                scanPossibilities(0, 0);
                if (possibilityGrid[row * 9 + col].size() == 0) {
                    return false;
                }
            }
            if (nextPossibleNumber(row, col, 0)) {
                if (col < 8) {
                    col++;
                } else if (row < 8) {
                    col = 0;
                    row++;
                } else {
                    return true;
                }
                if (!nextPossibleCell(row, col)) {
                    if (col > 0) {
                        col--;
                    } else if (row > 0) {
                        col = 8;
                        row--;
                    }
                    return nextPossibleCell(row, col);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (col < 8) {
                col++;
            } else if (row < 8) {
                col = 0;
                row++;
            } else {
                return true;
            }
            return nextPossibleCell(row, col);
        }
    }

    private boolean nextPossibleNumber(int row, int col, int index) {
        scanPossibilities(0, 0);
        if (possibilityGrid[row * 9 + col] != null && possibilityGrid[row * 9 + col].size() > index) {
            sudoku.setNumber(solverType, row, col, possibilityGrid[row * 9 + col].get(index));
            logSetNumber(row, col, possibilityGrid[row * 9 + col].get(index));
            scanPossibilities(0, 0);
        } else {
            sudoku.setNumber(solverType, row, col, 0);
            scanPossibilities(0, 0);
            return false;
        }
        int[][] tmpGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tmpGrid[i][j] = sudoku.getNumber(solverType, i, j);
            }
        }
        return nextPossibleNumber(row, col, ++index);
    }

    private boolean placeSingleNumber(int row, int col) {
        if (possibilityGrid[row * 9 + col] != null) {
            for (int j = 0; j < possibilityGrid[row * 9 + col].size(); j++) {
                if (checkNumberInColumn(row, col, possibilityGrid[row * 9 + col].get(j)) ||
                        checkNumberInRow(row, col, possibilityGrid[row * 9 + col].get(j)) ||
                        checkNumberInBlock(row, col, possibilityGrid[row * 9 + col].get(j))) {
                    sudoku.setNumber(solverType, row, col, possibilityGrid[row * 9 + col].get(j));
                    logSetNumber(row, col, possibilityGrid[row * 9 + col].get(j));
                    return true;
                }
            }
        }
        if (col < 8) {
            col++;
        } else if (row < 8) {
            col = 0;
            row++;
        } else {
            return false;
        }
        return placeSingleNumber(row, col);
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
        col = (int) (col / 3);
        row = (int) (row / 3);
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
        return false;
    }

    private void scanPossibilities(int row, int col) {
        if (col == 0 && row == 0) {
            resetPossibilityGrid();
        }
        if (sudoku.getNumber(solverType, row, col) == 0) {
            possibilityGrid[row * 9 + col] = new ArrayList<>();
            for (int k = 1; k < 10; k++) {
                sudoku.setNumber(solverType, row, col, k);
                if (checkRow(row) && checkColumn(col) && checkBlock(row, col)) {
                    possibilityGrid[row * 9 + col].add(k);
                }
                sudoku.setNumber(solverType, row, col, 0);
            }
            if (possibilityGrid[row * 9 + col].size() == 1) {
                sudoku.setNumber(solverType, row, col, possibilityGrid[row * 9 + col].get(0));
                logSetNumber(row, col, possibilityGrid[row * 9 + col].get(0));
                possibilityGrid[row * 9 + col] = null;
                scanPossibilities(0, 0);
            }
        } else {
            possibilityGrid[row * 9 + col] = null;
        }
        if (col < 8) {
            col++;
        } else if (row < 8) {
            col = 0;
            row++;
        } else {
            return;
        }
        scanPossibilities(row, col);
    }
}
