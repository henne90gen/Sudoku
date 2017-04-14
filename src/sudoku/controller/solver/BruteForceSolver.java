package sudoku.controller.solver;

import sudoku.controller.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

public class BruteForceSolver extends Solver {

	private SudokuPosition position;

	private boolean backtracking;

	public BruteForceSolver(ISudokuController controller) {
		super(controller, SolverType.BruteForceSolver);
		position = new SudokuPosition(0, 0);
		backtracking = false;
	}

	@Override
	protected void useSolver(SudokuModel sudoku) {
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		if (sudoku.isFieldEditable(position.getRow(), position.getCol())) {
			if (nextNumber(sudoku, position)) {
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

	private boolean nextNumber(SudokuModel sudoku, SudokuPosition p) {
		SudokuPosition position = p.getCopy();
		if (getNumber(sudoku, position) < 9) {
			int newNumber = getNumber(sudoku, position) + 1;
			setNumber(sudoku, position, newNumber);
		} else {
			setNumber(sudoku, position, 0);
			return false;
		}
		return validateRow(sudoku, position.getRow()) && validateColumn(sudoku, position.getCol())
				&& validateBlock(sudoku, position.getRow(), position.getCol()) || nextNumber(sudoku, position);
	}
}
