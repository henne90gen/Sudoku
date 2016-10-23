package sudoku.model;

import sudoku.SudokuController;
import sudoku.exceptions.IllegalGridException;
import sudoku.solver.Solver;
import sudoku.solver.SolverFactory;
import sudoku.solver.SolverType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by henne on 16.10.16.
 */
public class SudokuModel {

    private String name;
    private Integer[] grid;
    private Map<SolverType, Solver> solvers;
    private Map<SolverType, Integer[]> solutions;
    private boolean[] editableFields;

    public SudokuModel(SudokuController controller, String name, int[] grid) throws IllegalGridException {
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

        solutions = new LinkedHashMap<>();
        solvers = SolverFactory.INSTANCE.getAllSolvers(controller, this);
    }

    public String getName() {
        return name;
    }

    public Integer[] getGridCopy() {
        return Arrays.copyOf(grid, grid.length);
    }

    public int getNumber(SolverType solverType, int row, int col) {
        Integer[] solution = solutions.get(solverType);
        if (solution != null && isFieldEditable(row, col)) {
            return solution[row * 9 + col];
        }
        return grid[row * 9 + col];
    }

    public boolean setNumber(SolverType solverType, int row, int col, int num) {
        Integer[] solution = solutions.get(solverType);

        if (solution == null) return false;

        if (editableFields[row * 9 + col]) {
            solution[row * 9 + col] = num;
            return true;
        }
        return false;
    }

    public boolean solveUsingSolver(SolverType solverType) {
        Solver solver = solvers.get(solverType);
        if (solver != null) {
            new Thread(solver::solve).start();
            return true;
        }
        return false;
    }

    public void addSolution(SolverType solverType, Integer[] solutionGrid) {
        solutions.put(solverType, solutionGrid);
    }

    public Integer[] getSolution(SolverType solverType) {
        return solutions.get(solverType);
    }

    public boolean isFieldEditable(int row, int col) {
        return editableFields[row * 9 + col];
    }

    public void resetSolver(SolverType solverType) {
        solutions.put(solverType, getGridCopy());
    }
}
