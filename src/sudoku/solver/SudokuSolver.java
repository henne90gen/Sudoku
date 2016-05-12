package sudoku.solver;

import java.util.ArrayList;

import sudoku.Sudoku;
import sudoku.Sudoku.FeedbackMode;
import sudoku.SudokuWindow;

public abstract class SudokuSolver implements Runnable {
	
	protected ArrayList<Integer>[][] possibilityGrid;
	private long time;
	private boolean solved;
	
	protected boolean solving;
	protected Sudoku sudoku;
	
	
	public SudokuSolver(Sudoku sudoku, SudokuWindow window) {
		this(sudoku);
	}
	
	public SudokuSolver(Sudoku sudoku) {
		this.sudoku = sudoku;
		possibilityGrid = new ArrayList[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				possibilityGrid[i][j] = new ArrayList();
			}
		}
	}
	
	@Override
	public void run() {
		if (!checkGridValidity())
			return;
		solving = true;
		time = System.nanoTime();
		solved = solve();
		if (solved && sudoku.getFeedbackMode() != FeedbackMode.Silent) {
			System.out.println("Solved sudoku " + sudoku.getName() + " in " + getSolveTime());
		} else if (!solved && sudoku.getFeedbackMode() != FeedbackMode.Silent) {
			System.out.println("Sudoku " + sudoku.getName() + " is not solvable.");
		}
		if (sudoku.getFeedbackMode() == FeedbackMode.Window) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					sudoku.setNumber(i, j, (solved)?(sudoku.getNumber(i, j)):((sudoku.getOriginalNumber(i, j) == 0)?(0):(sudoku.getOriginalNumber(i, j))), solved);
				}
			}
		}
	}
	
	protected abstract boolean solve();

	protected boolean checkWholeGrid(boolean takeTime) {
		boolean gridCorrect = true;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (!checkRow(j) || !checkColumn(i) || !checkBlock(i, j))
					gridCorrect = false;
						
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (sudoku.getNumber(i, j) == 0)
					gridCorrect = false;
					
		if (gridCorrect && takeTime) {
			time = System.nanoTime() - time;
		}
		return gridCorrect;
	}
	
	protected boolean checkRow(int row) {
		for (int i = 0; i < 9; i++) {
			if (sudoku.getNumber(i, row) != 0) {
				for (int j = 0; j < 9; j++) {
					if (sudoku.getNumber(j, row) != 0) {
						if (sudoku.getNumber(i, row) == sudoku.getNumber(j, row) && i != j)
							return false;
					}
				}
			}
		}
		return true;
	}
	
	protected boolean checkColumn(int col) {
		for (int i = 0; i < 9; i++) {
			if (sudoku.getNumber(col, i) != 0) {
				for (int j = 0; j < 9; j++) {
					if (sudoku.getNumber(col, j) != 0) {
						if (sudoku.getNumber(col, i) == sudoku.getNumber(col, j) && i != j)
							return false;
					}
				}
			}
		}
		return true;
	}
	
	protected boolean checkBlock(int col, int row) {
		col = (int)(col / 3) * 3;
		row = (int)(row / 3) * 3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (sudoku.getNumber(i + col, j + row) != 0) {
					for (int k = 0; k < 3; k++) {
						for (int l = 0; l < 3; l++) {
							if (sudoku.getNumber(k + col, l + row) != 0)
								if (sudoku.getNumber(i + col, j + row) == sudoku.getNumber(k + col, l + row) &&
									((i != k ^ j != l) || (i != k && j != l))) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean checkGridValidity() {
		boolean gridValid = true;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!checkRow(j) || !checkColumn(i) || !checkBlock(i, j)) {
					gridValid = false;
				}
			}
		}
		return gridValid;
	}

	public static int[][] flipGrid(int[][] grid) {
		int[][] tmp = new int[9][9];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				tmp[i][j] = grid[j][i];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				grid[i][j] = tmp[i][j];
		return tmp;
	}
	
	public float getSolveTime() {
		return time / 1000000000.0f;
	}

	public boolean getSolved() {
		return solved;
	}
	
	public void stopSolving() {
		solving = false;
	}
}
