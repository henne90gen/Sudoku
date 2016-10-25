package sudoku.model;

import sudoku.ISudokuController;
import sudoku.exceptions.IllegalGridException;
import sudoku.solver.Solver;
import sudoku.solver.SolverFactory;
import sudoku.solver.SolverType;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by henne on 16.10.16.
 */
public class SudokuModel {

    private final String name;
    private Integer[] grid;
    private Map<SolverType, Solver> solvers;
    //    private Map<SolverType, Integer[]> solutions;
    private boolean[] editableFields;

    public SudokuModel(ISudokuController controller, String name, Integer[] grid) throws IllegalGridException {
        this.name = name;
        if (grid.length != 81) {
            throw new IllegalGridException();
        }


        this.grid = new Integer[81];

        for (int i = 0; i < 81; i++) {
            this.grid[i] = grid[i];
        }

        editableFields = new boolean[this.grid.length];
        for (int i = 0; i < grid.length; i++) {
            editableFields[i] = grid[i] == 0;
        }

        solvers = SolverFactory.INSTANCE.getAllSolvers(controller, this);
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
            return solver.getNumber(row, col);
        }
        return grid[row * 9 + col];
    }

    public void setNumber(SolverType solverType, int row, int col, int num) {
        Solver solver = solvers.get(solverType);

        if (solver != null && isFieldEditable(row, col)) {
            solver.setNumber(row, col, num);
        }
    }

    public boolean solveUsingSolver(SolverType solverType) {
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

    public Solver getSolver(SolverType solverType) {
        return solvers.get(solverType);
    }
}
