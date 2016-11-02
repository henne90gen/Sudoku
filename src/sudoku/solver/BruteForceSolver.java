package sudoku.solver;

import sudoku.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

/**
 * Created by henne on 16.10.16.
 */
public class BruteForceSolver extends Solver {

    public BruteForceSolver(ISudokuController controller, SudokuModel sudoku) {
        super(controller, sudoku, SolverType.BruteForceSolver);
    }

    @Override
    public void startSolving() {
        SudokuPosition position = new SudokuPosition(0, 0);
        boolean backtracking = false;
        while (true) {
            if (sudoku.isFieldEditable(position.getRow(), position.getCol())) {
                if (nextNumber(position)) {
                    if (!position.moveForward()) {
                        break;
                    }
                    backtracking = false;
                } else {
                    position.moveBackward();
                    backtracking = true;
                }
            } else {
                if (backtracking) {
                    position.moveBackward();
                } else if (!position.moveForward()) {
                    break;
                }
            }
        }
    }

    private boolean nextNumber(SudokuPosition p) {
        SudokuPosition position = p.getCopy();
        if (getNumber(position) < 9) {
            int newNumber = getNumber(position) + 1;
            setNumber(position, newNumber);
        } else {
            setNumber(position, 0);
            return false;
        }
        return validateRow(position.getRow()) && validateColumn(position.getCol()) && validateBlock(position.getRow()
                , position.getCol()) || nextNumber(position);
    }

    @Override
    protected void resetSolver() {
        // do nothing
    }
}
