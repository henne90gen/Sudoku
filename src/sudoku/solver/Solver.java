package sudoku.solver;

import sudoku.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.view.event.SudokuEvent;
import sudoku.view.event.SudokuEventFactory;

/**
 * Created by henne on 16.10.16.
 */
public abstract class Solver {

    final SudokuModel sudoku;
    final SolverType solverType;
    private final ISudokuController controller;
    private Thread solveThread;

    private Integer[] solution;

    public Solver(ISudokuController controller, SudokuModel sudoku, SolverType solverType) {
        this.controller = controller;
        this.sudoku = sudoku;
        this.solverType = solverType;

        solution = sudoku.getGridCopy();
    }

    public void solve() {
        solveThread = new Thread(this::run);
        solveThread.start();
    }

    private void run() {
        long startTime = System.nanoTime();
        startSolving();
        long endTime = System.nanoTime();
        float solveTime = (endTime - startTime) / 1000000000.0f;

        postFinishMessage(solveTime);
    }

    public void waitFor() {
        try {
            solveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void startSolving();

    public int getNumber(int row, int col) {
        return solution[row * 9 + col];
    }

    public void setNumber(int row, int col, int num) {
        solution[row * 9 + col] = num;
    }

    private void postFinishMessage(float time) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getFinishEvent(sudoku, time));
    }

    protected void postMessage(String message) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getPostMessageEvent(sudoku, message));
    }

    void logSetNumber(int row, int col, int newNumber) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getSetNumberEvent(sudoku, row, col, newNumber));
    }

    private void pushSudokuEvent(SudokuEvent event) {
        controller.handleSudokuEvent(event);
    }

    public SolverType getSolverType() {
        return solverType;
    }

    public boolean checkColumn(int col) {
        for (int i = 0; i < 9; i++) {
            if (sudoku.getNumber(solverType, i, col) != 0) {
                for (int j = 0; j < 9; j++) {
                    if (sudoku.getNumber(solverType, j, col) != 0) {
                        if (sudoku.getNumber(solverType, i, col) == sudoku.getNumber(solverType, j, col) && i != j)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkRow(int row) {
        for (int i = 0; i < 9; i++) {
            if (sudoku.getNumber(solverType, row, i) != 0) {
                for (int j = 0; j < 9; j++) {
                    if (sudoku.getNumber(solverType, row, j) != 0) {
                        if (sudoku.getNumber(solverType, row, i) == sudoku.getNumber(solverType, row, j) && i != j)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkBlock(int row, int col) {
        col = col / 3 * 3;
        row = row / 3 * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku.getNumber(solverType, j + row, i + col) != 0) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            if (sudoku.getNumber(solverType, l + row, k + col) != 0)
                                if (sudoku.getNumber(solverType, j + row, i + col) == sudoku.getNumber(solverType, l
                                        + row, k + col) && (i != k ^ j != l || i != k)) {
                                    return false;
                                }
                        }
                    }
                }
            }
        }
        return true;
    }

    public Integer[] getSolution() {
        return solution;
    }

    public void reset() {
        solution = sudoku.getGridCopy();
    }
}
