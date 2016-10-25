package sudoku.solver;

import sudoku.ISudokuController;
import sudoku.SudokuController;
import sudoku.model.SudokuModel;

/**
 * Created by henne on 16.10.16.
 */
public class BruteForceSolver extends Solver {

    public BruteForceSolver(ISudokuController controller, SudokuModel sudoku) {
        super(controller, sudoku, SolverType.BruteForceSolver);
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
        if (sudoku.getNumber(solverType, row, col) < 9) {
            int newNumber = sudoku.getNumber(solverType, row, col) + 1;
            logSetNumber(row, col, newNumber);
            sudoku.setNumber(solverType, row, col, newNumber);
        } else {
            sudoku.setNumber(solverType, row, col, 0);
            return false;
        }
        return checkRow(row) && checkColumn(col) && checkBlock(row, col) || nextNumber(row, col);
    }
}
