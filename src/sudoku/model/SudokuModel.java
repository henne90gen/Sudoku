package sudoku.model;

import sudoku.controller.ISudokuController;
import sudoku.exceptions.IllegalGridException;
import sudoku.model.solver.Solver;
import sudoku.model.solver.SolverFactory;
import sudoku.model.solver.SolverType;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class SudokuModel {

	private ISudokuController controller;
	private final String name;
	private Integer[] grid;
	private Map<SolverType, Solver> solvers;
	// private Map<SolverType, Integer[]> solutions;
	private boolean[] editableFields;

	public SudokuModel(ISudokuController controller, String name, Integer[] grid) throws IllegalGridException {
		this.controller = controller;
		this.name = name;
		if (grid.length != 81) {
			throw new IllegalGridException();
		}
		this.grid = new Integer[81];

		System.arraycopy(grid, 0, this.grid, 0, 81);

		editableFields = new boolean[this.grid.length];
		for (int i = 0; i < grid.length; i++) {
			editableFields[i] = grid[i] == 0;
		}

		solvers = SolverFactory.INSTANCE.getAllSolvers(controller, this);

		this.controller.addModel(this);
	}

	public String getName() {
		return name;
	}

	public Integer[] getGridCopy() {
		return Arrays.copyOf(grid, grid.length);
	}

	public int getNumber(SolverType solverType, int row, int col) {
		Solver solver = solvers.get(solverType);
		if (solver != null) {
			return solver.getNumber(new SudokuPosition(row, col));
		}
		return grid[row * 9 + col];
	}

	public boolean startSolver(SolverType solverType) {
		Solver solver = solvers.get(solverType);
		if (solver != null) {
			solver.solve();
			return true;
		}
		return false;
	}

	public Integer[] getSolution(SolverType solverType) {
		Solver solver = solvers.get(solverType);
		return solver.getSolution();
	}

	public boolean isFieldEditable(int row, int col) {
		return editableFields[row * 9 + col];
	}

	public void resetSolver(SolverType solverType) {
		Solver solver = solvers.get(solverType);
		if (solver != null) {
			solver.reset();
		}
	}

	public void waitForSolver(SolverType solverType) {
		Solver solver = solvers.get(solverType);
		if (solver != null) {
			solver.waitFor();
		}
	}

	public void printToFile(File file) {
		// TODO implement this
	}

	public boolean isSolverRunning(SolverType solverType) {
		Solver solver = solvers.get(solverType);
		if (solver != null) {
			return solver.isSolving();
		}
		return false;
	}

	public void stopSolver(SolverType solverType) {
		Solver solver = solvers.get(solverType);
		if (solver != null) {
			solver.stop();
		}
	}
}
