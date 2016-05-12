package sudoku.solver;

import sudoku.Sudoku;
import sudoku.Sudoku.FeedbackMode;
import sudoku.SudokuWindow;

public class BruteForceSolver extends SudokuSolver {
	
	public BruteForceSolver(Sudoku sudoku) {
		super(sudoku);
	}
	
	public BruteForceSolver(Sudoku sudoku, SudokuWindow window) {
		super(sudoku, window);
	}
	
	public boolean solve() {
		boolean solved = nextCell(0,0);
		int timesFlipped = 0;
		while (!solved) {
			if (timesFlipped >= 1) {
				break;
			} else {
				timesFlipped++;
			}
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					sudoku.setNumber(i, j, sudoku.getOriginalNumber(i, j), false);
				}
			}
			sudoku.setGrid(SudokuSolver.flipGrid(sudoku.getGrid()));
			sudoku.setOriginalGrid(SudokuSolver.flipGrid(sudoku.getOriginalGrid()));
			solved = nextCell(0,0);
		}
		if (timesFlipped % 2 != 0) {
			sudoku.setGrid(SudokuSolver.flipGrid(sudoku.getGrid()));
			sudoku.setOriginalGrid(SudokuSolver.flipGrid(sudoku.getOriginalGrid()));
		}
		return checkWholeGrid(true);
	}
	
	private boolean nextCell(int col, int row) {
		if ((System.nanoTime() / 1000000000.0f - getSolveTime() > 1.0f && sudoku.getFeedbackMode() != FeedbackMode.Window) || !solving) {
			return false;
		}
		if (sudoku.getOriginalNumber(col, row) == 0) {
			if (nextNumber(col, row)) {
				if (col < 8) {
					col++;
				} else if (row < 8) {
					col = 0;
					row++;
				} else {
					return true;
				}
				if (!nextCell(col, row)) {
					if (col > 0) {
						col--;
					} else if (row > 0) {
						col = 8;
						row--;
					}
					return nextCell(col, row);
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
			return nextCell(col, row);
		}
	}
	
	private boolean nextNumber(int col, int row) {
		if (sudoku.getNumber(col, row) < 9) {
			sudoku.setNumber(col, row, sudoku.getNumber(col, row) + 1, true);
		} else {
			sudoku.setNumber(col, row, 0, false);
			return false;
		}
		if (checkRow(row) && checkColumn(col) && checkBlock(col, row))
			return true;
		return nextNumber(col, row);
	}
}
