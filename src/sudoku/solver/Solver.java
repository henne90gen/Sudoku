package sudoku.solver;

import sudoku.ISudokuController;
import sudoku.SudokuController;
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

    Solver(ISudokuController controller, SudokuModel sudoku, SolverType solverType) {
        this.controller = controller;
        this.sudoku = sudoku;
        this.solverType = solverType;

        Integer[] solution = sudoku.getGridCopy();
        sudoku.addSolution(solverType, solution);
    }

    public void solve() {
        long startTime = System.nanoTime();
        startSolving();
        long endTime = System.nanoTime();
        float solveTime = (endTime - startTime) / 1000000000.0f;

        postFinishMessage((long) solveTime);
    }

    protected abstract void startSolving();

    private void postFinishMessage(long time) {
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

    boolean checkColumn(int col) {
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

    boolean checkRow(int row) {
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

    boolean checkBlock(int row, int col) {
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
}
