package sudoku_new.visualizer;

import sudoku_new.Sudoku;
import sudoku_new.solver.Solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henne on 16.10.16.
 */
public abstract class Visualizer {

    protected Sudoku sudoku;

    protected List<Solver> solvers;

    protected Visualizer(Sudoku sudoku) {
        this.sudoku = sudoku;
        solvers = new ArrayList<>();
    }

    public abstract void open();

    public abstract void postMessage(int importance, String message);

    public abstract void updateSudoku();

    public void addSolver(Solver solver) {
        solvers.add(solver);
    }
}
