package sudoku.solver;

import sudoku.SudokuController;
import sudoku.model.SudokuModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by henne on 20.10.16.
 */
public class SolverFactory {

    public static SolverFactory INSTANCE = new SolverFactory();

    private BruteForceSolver bruteForceSolver;

    private SmartSolver smartSolver;

    private SolverFactory() {
    }

    public Map<SolverType, Solver> getAllSolvers(SudokuController controller, SudokuModel sudoku) {
        Map<SolverType, Solver> solvers = new LinkedHashMap<>();

        Solver solver = getBruteSolver(controller, sudoku);
        solvers.put(solver.getSolverType(), solver);

        solver = getSmartSolver(controller, sudoku);
        solvers.put(solver.getSolverType(), solver);

        return solvers;
    }

    public BruteForceSolver getBruteSolver(SudokuController controller, SudokuModel sudoku) {
        if (bruteForceSolver == null) {
            bruteForceSolver = new BruteForceSolver(controller, sudoku);
        }
        return bruteForceSolver;
    }

    public SmartSolver getSmartSolver(SudokuController controller, SudokuModel sudoku) {
        if (smartSolver == null) {
            smartSolver = new SmartSolver(controller, sudoku);
        }
        return smartSolver;
    }
}
