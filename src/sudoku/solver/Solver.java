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

    protected Integer[] solution;

    private Thread solveThread;

    public Solver(ISudokuController controller, SudokuModel sudoku, SolverType solverType) {
        this.controller = controller;
        this.sudoku = sudoku;
        this.solverType = solverType;

        solution = sudoku.getGridCopy();
    }

    /**
     * Starts the solving process.
     * A new thread is used for this, to prevent the solver from blocking anything else.
     */
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

    /**
     * Blocks until the solver has finished solving
     */
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

    /**
     * @param colToCheck Zero-indexed column
     * @return True if column is valid, false if there are duplicate numbers
     */
    public boolean validateColumn(int colToCheck) {
        for (int row1 = 0; row1 < 9; row1++) {
            for (int row2 = 0; row2 < row1; row2++) {
                if (getNumber(row1, colToCheck) != 0 && getNumber(row1, colToCheck) == getNumber(row2,
                        colToCheck)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param rowToCheck Zero-indexed row
     * @return True if row is valid, false if there are duplicate numbers
     */
    public boolean validateRow(int rowToCheck) {
        int firstIndex = rowToCheck * 9;
        for (int i = firstIndex; i < firstIndex + 9; i++) {
            for (int j = firstIndex; j < i; j++) {
                if (solution[i] != 0 && solution[i].equals(solution[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates the block associated with the field indicated by the give row and column
     *
     * @param row Zero-indexed row
     * @param col Zero-indexed column
     * @return True if the block that contains the field with row and col is valid, false if there are duplicate numbers
     */
    public boolean validateBlock(int row, int col) {
        // Get the row and column of the top left corner of the block that contains the field with the given row and col
        col = col / 3 * 3;
        row = row / 3 * 3;

        // Copy the nine fields of the block into one long array
        int[] blockAsRow = new int[9];
        int index = 0;
        for (int i = row; i < row + 3; i++) {
            for (int j = col; j < col + 3; j++) {
                blockAsRow[index++] = getNumber(i, j);
            }
        }

        // Apply same algorithm as validateRow() uses
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < i; j++) {
                if (blockAsRow[i] != 0 && blockAsRow[i] == blockAsRow[j]) {
                    return false;
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
