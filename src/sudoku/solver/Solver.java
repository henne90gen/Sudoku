package sudoku.solver;

import sudoku.SudokuController;
import sudoku.SudokuModel;
import sudoku.view.event.SudokuEvent;
import sudoku.view.event.SudokuEventFactory;

/**
 * Created by henne on 16.10.16.
 */
public abstract class Solver {

    private SudokuController controller;

    protected final SudokuModel sudoku;

    protected SolverType solverType;

    public Solver(SudokuController controller, SudokuModel sudoku, SolverType solverType) {
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

        postFinishMessage("Solved sudoku '" + sudoku.getName() + "' in " + solveTime + "s");
    }

    protected abstract void startSolving();

    protected void postFinishMessage(String message) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getFinishEvent(sudoku, message));
    }

    protected void postMessage(String message) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getPostMessageEvent(sudoku, message));
    }

    protected void logSetNumber(int row, int col, int newNumber) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getSetNumberEvent(sudoku, row, col, newNumber));
    }

    private void pushSudokuEvent(SudokuEvent event) {
        controller.handleSudokuEvent(event);
    }

    public void reset() {
        sudoku.resetSolver(solverType);
    }

    public SolverType getSolverType() {
        return solverType;
    }

    protected boolean checkColumn(int col) {
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

    protected boolean checkRow(int row) {
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

    protected boolean checkBlock(int row, int col) {
        col = (int) (col / 3) * 3;
        row = (int) (row / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku.getNumber(solverType, j + row, i + col) != 0) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            if (sudoku.getNumber(solverType, l + row, k + col) != 0)
                                if ((sudoku.getNumber(solverType, j + row, i + col) == sudoku.getNumber(solverType, l + row, k + col)) &&
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
