package sudoku.controller.solver;

import sudoku.controller.ISudokuController;
import sudoku.controller.event.FinishedSolvingEvent;
import sudoku.controller.event.PostMessageEvent;
import sudoku.controller.event.SudokuEvent;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

public abstract class Solver {

	// final SudokuModel sudoku;

	private final SolverType solverType;

	private final ISudokuController controller;

	protected boolean solving;

	/**
	 * Keeps track of how many times the main solving loop has run
	 */
	protected int runCounter = 0;

	private Thread solveThread;

	private int operations;

	Solver(ISudokuController controller, SolverType solverType) {
		this.controller = controller;
		this.solverType = solverType;
	}

	/**
	 * Starts the solving process. A new thread is used for this, to prevent the
	 * solver from blocking anything else.
	 */
	public void solve(SudokuModel sudoku) {
		solveThread = new Thread(() -> run(sudoku));
		solveThread.start();
	}

	private void run(SudokuModel sudoku) {
		solving = true;
		long startTime = System.nanoTime();

		while (solving) {
			useSolver(sudoku);
			runCounter++;
		}

		long endTime = System.nanoTime();
		float solveTime = (endTime - startTime) / 1000000000.0f;

		postFinishMessage(sudoku, solveTime);

	}

	protected abstract void useSolver(SudokuModel sudoku);

	/**
	 * Blocks until the solver has finished solving
	 */
	public void waitFor() {
		try {
			if (solveThread.isAlive()) {
				solveThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the solver if it is currently solving, does nothing otherwise
	 */
	public void stop() {
		solving = false;
	}

	protected boolean setNumber(SudokuModel sudoku, SudokuPosition position, int num) {
		if (sudoku.setNumber(position.getRow(), position.getCol(), num)) {

			operations++;
			return true;
		}
		return false;
	}

	public boolean isSolving() {
		return solving;
	}

	private void postFinishMessage(SudokuModel sudoku, float time) {
		SudokuEvent event = new FinishedSolvingEvent(solverType, time, operations);
		controller.fireEvent(sudoku, event);
	}

	protected void postMessage(String channel, String message) {
		SudokuEvent event = new PostMessageEvent(message);
		controller.fireEvent(event);
	}

	SolverType getSolverType() {
		return solverType;
	}

	/**
	 * @param colToCheck
	 *            Zero-indexed column
	 * @return True if column is valid, false if there are duplicate numbers
	 */
	public boolean validateColumn(SudokuModel sudoku, int colToCheck) {
		for (int row1 = 0; row1 < 9; row1++) {
			for (int row2 = 0; row2 < row1; row2++) {
				SudokuPosition pos1 = new SudokuPosition(row1, colToCheck);
				SudokuPosition pos2 = new SudokuPosition(row2, colToCheck);
				if (getNumber(sudoku, pos1) != 0 && getNumber(sudoku, pos1) == getNumber(sudoku, pos2)) {
					return false;
				}
			}
		}
		return true;
	}

	public int getNumber(SudokuModel sudoku, SudokuPosition position) {
		return sudoku.getNumber(position.getRow(), position.getCol());
	}

	/**
	 * @param rowToCheck
	 *            Zero-indexed row
	 * @return True if row is valid, false if there are duplicate numbers
	 */
	public boolean validateRow(SudokuModel sudoku, int rowToCheck) {
		for (int col = 0; col < 9; col++) {
			for (int i = 0; i < col; i++) {
				int num = sudoku.getNumber(rowToCheck, col);
				int numI = sudoku.getNumber(rowToCheck, i);
				if (num != 0 && num == numI) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Validates the block associated with the field indicated by the give row
	 * and column
	 *
	 * @param row
	 *            Zero-indexed row
	 * @param col
	 *            Zero-indexed column
	 * @return True if the block that contains the field with row and col is
	 *         valid, false if there are duplicate numbers
	 */
	public boolean validateBlock(SudokuModel sudoku, int row, int col) {
		// Get the row and column of the top left corner of the block that
		// contains the field with the given row and col
		col = col / 3 * 3;
		row = row / 3 * 3;

		// Copy the nine fields of the block into one long array
		int[] blockAsRow = new int[9];
		int index = 0;
		for (int i = row; i < row + 3; i++) {
			for (int j = col; j < col + 3; j++) {
				blockAsRow[index++] = getNumber(sudoku, new SudokuPosition(i, j));
			}
		}

		// Apply same algorithm as validateRow() uses
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < i; j++) {
				if (blockAsRow[i] != 0 && blockAsRow[i] == blockAsRow[j]) {
					return false;
				}
			}
		}
		return true;
	}
}
