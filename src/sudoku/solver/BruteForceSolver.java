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
        nextCell(new SudokuPosition(0, 0));
    }

    @Override
    protected void resetSolver() {
        // do nothing
    }

    private boolean nextCell(SudokuPosition p) {
        SudokuPosition position = new SudokuPosition(p.getRow(), p.getCol());
        if (sudoku.isFieldEditable(position.getRow(), position.getCol())) {
            if (nextNumber(position)) {
                if (!position.moveRight()) {
                    return true;
                }
                if (!nextCell(position)) {
                    position.moveLeft();
                    return nextCell(position);
                }
                return true;
            }
            return false;
        }
        return !position.moveRight() || nextCell(position);
    }

    private boolean nextNumber(SudokuPosition p) {
        SudokuPosition position = new SudokuPosition(p.getRow(), p.getCol());
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
