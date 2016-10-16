package sudoku_new.solver;

import sudoku_new.Sudoku;

/**
 * Created by henne on 16.10.16.
 */
public class BruteForceSolver extends Solver {

    public BruteForceSolver(Sudoku sudoku) {
        super(sudoku, SolverType.BruteForce);
    }

    @Override
    public void startSolving() {
        nextCell(0, 0);
    }

    private boolean nextCell(int row, int col) {
        if (sudoku.isFieldEditable(row, col)) {
            if (nextNumber(row, col)) {
                if (col < 8) {
                    col++;
                } else if (row < 8) {
                    col = 0;
                    row++;
                } else {
                    return true;
                }
                if (!nextCell(row, col)) {
                    if (col > 0) {
                        col--;
                    } else if (row > 0) {
                        col = 8;
                        row--;
                    }
                    return nextCell(row, col);
                } else {
                    return true;
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
            return nextCell(row, col);
        }
    }

    private boolean nextNumber(int row, int col) {
        if (sudoku.getNumber(row, col) < 9) {
            int newNumber = sudoku.getNumber(row, col) + 1;
            postMessage(0, "Placing number " + newNumber + " at position " + row + " " + col);
            sudoku.setNumber(row, col, newNumber);
        } else {
            sudoku.setNumber(row, col, 0);
            return false;
        }
        return checkRow(row) && checkColumn(col) && checkBlock(row, col) || nextNumber(row, col);
    }
}
