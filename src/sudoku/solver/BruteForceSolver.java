package sudoku.solver;

import sudoku.ISudokuController;
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
        nextCell(new SudokuPosition(0, 0));
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
        if (getNumber(position.getRow(), position.getCol()) < 9) {
            int newNumber = getNumber(position.getRow(), position.getCol()) + 1;
            logSetNumber(position.getRow(), position.getCol(), newNumber);
            setNumber(position.getRow(), position.getCol(), newNumber);
        } else {
            setNumber(position.getRow(), position.getCol(), 0);
            return false;
        }
        return validateRow(position.getRow()) && validateColumn(position.getCol()) && validateBlock(position.getRow()
                , position.getCol()) || nextNumber(position);
    }
}
