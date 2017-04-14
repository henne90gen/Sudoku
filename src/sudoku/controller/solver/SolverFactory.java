package sudoku.controller.solver;

import java.util.LinkedHashMap;
import java.util.Map;

import sudoku.controller.ISudokuController;
import sudoku.model.SudokuModel;

public class SolverFactory {

	private SolverFactory() {
	}

	public static Map<SolverType, Solver> getAllSolvers(ISudokuController controller, SudokuModel sudoku) {
		Map<SolverType, Solver> solvers = new LinkedHashMap<>();

		Solver solver = getBruteSolver(controller);
		solvers.put(solver.getSolverType(), solver);

		solver = getSmartSolver(controller);
		solvers.put(solver.getSolverType(), solver);

		return solvers;
	}

	public static BruteForceSolver getBruteSolver(ISudokuController controller) {
		return new BruteForceSolver(controller);
	}

	private static SmartSolver getSmartSolver(ISudokuController controller) {
		return new SmartSolver(controller);
	}

	public static Solver getSolver(ISudokuController controller, SolverType solverType) {
		Solver solver = null;

		switch (solverType) {
		case BruteForceSolver:
			solver = getBruteSolver(controller);
			break;
		case SmartSolver:
			solver = getSmartSolver(controller);
			break;
		}

		return solver;
	}
}
