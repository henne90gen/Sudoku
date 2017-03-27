package sudoku.model.solver;

import sudoku.controller.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

/**
 * Created by henne on 16.10.16.
 */
public class BruteForceSolver extends Solver {

    private SudokuPosition position;

    private boolean backtracking;

    public BruteForceSolver(ISudokuController controller, SudokuModel sudoku) {
        super(controller, sudoku, SolverType.BruteForceSolver);
    }

    @Override
    public void useSolver() {
        if (sudoku.isFieldEditable(position.getRow(), position.getCol())) {
            if (nextNumber(position)) {
                if (!position.moveForward()) {
                    stop();
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
                stop();
            }
        }
    }

    @Override
    protected void resetSolver() {
        position = new SudokuPosition(0, 0);
        backtracking = false;
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
}
