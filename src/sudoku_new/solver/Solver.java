package sudoku_new.solver;

import sudoku_new.Sudoku;
import sudoku_new.visualizer.Visualizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henne on 16.10.16.
 */
public abstract class Solver {

    public enum SolverType {
        BruteForce, Smart, Custom
    }

    protected final Sudoku sudoku;
    private List<Visualizer> visualizers = new ArrayList<>();
    protected SolverType solverType;

    public Solver(Sudoku sudoku, SolverType solverType) {
        this.sudoku = sudoku;
        this.solverType = solverType;
    }

    public void solve() {
        long startTime = System.nanoTime();
        startSolving();
        long endTime = System.nanoTime();
        float solveTime = (endTime - startTime) / 1000000000.0f;
        updateSudoku();
        postMessage(6, "Solved sudoku " + sudoku.getName() + " in " + solveTime + "s with " + solverType.toString());
    }

    protected abstract void startSolving();

    public void addVisualizer(Visualizer visualizer) {
        visualizers.add(visualizer);
    }

    protected void postMessage(int importance, String message) {
        for (Visualizer visualizer : visualizers) {
            visualizer.postMessage(importance, message);
        }
    }

    private void updateSudoku() {
        visualizers.forEach(Visualizer::updateSudoku);
    }

    protected boolean checkColumn(int col) {
        for (int i = 0; i < 9; i++) {
            if (sudoku.getNumber(i, col) != 0) {
                for (int j = 0; j < 9; j++) {
                    if (sudoku.getNumber(j, col) != 0) {
                        if (sudoku.getNumber(i, col) == sudoku.getNumber(j, col) && i != j)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean checkRow(int row) {
        for (int i = 0; i < 9; i++) {
            if (sudoku.getNumber(row, i) != 0) {
                for (int j = 0; j < 9; j++) {
                    if (sudoku.getNumber(row, j) != 0) {
                        if (sudoku.getNumber(row, i) == sudoku.getNumber(row, j) && i != j)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean checkBlock(int row, int col) {
        col = (int) (col / 3) * 3;
        row = (int) (row / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku.getNumber(j + row, i + col) != 0) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            if (sudoku.getNumber(l + row, k + col) != 0)
                                if ((sudoku.getNumber(j + row, i + col) == sudoku.getNumber(l + row, k + col)) &&
                                        (((i != k) ^ (j != l)) || ((i != k) && (j != l)))) {
                                    return false;
                                }
                        }
                    }
                }
            }
        }
        return true;
    }
}
