package sudoku.model.solver;

import sudoku.controller.ISudokuController;
import sudoku.model.SudokuModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by henne on 20.10.16.
 */
public class SolverFactory {

    public static final SolverFactory INSTANCE = new SolverFactory();

    private SolverFactory() {
    }

    public Map<SolverType, Solver> getAllSolvers(ISudokuController controller, SudokuModel sudoku) {
        Map<SolverType, Solver> solvers = new LinkedHashMap<>();

        Solver solver = getBruteSolver(controller, sudoku);
        solvers.put(solver.getSolverType(), solver);

        solver = getSmartSolver(controller, sudoku);
        solvers.put(solver.getSolverType(), solver);

        return solvers;
    }

    public BruteForceSolver getBruteSolver(ISudokuController controller, SudokuModel sudoku) {
        return new BruteForceSolver(controller, sudoku);
    }

    private SmartSolver getSmartSolver(ISudokuController controller, SudokuModel sudoku) {
        return new SmartSolver(controller, sudoku);
    }
}
