package sudoku.model.solver;

import sudoku.controller.ISudokuController;
import sudoku.controller.event.SudokuEvent;
import sudoku.controller.event.SudokuEventFactory;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

/**
 * Created by henne on 16.10.16.
 */
public abstract class Solver {

    final SudokuModel sudoku;

    private final SolverType solverType;

    private final ISudokuController controller;

    protected boolean solving;

    /**
     * Keeps track of how many times the main solving loop has run
     */
    protected int runCounter = 0;

    private Integer[] solution;

    private Thread solveThread;

    private int operations;

    Solver(ISudokuController controller, SudokuModel sudoku, SolverType solverType) {
        this.controller = controller;
        this.sudoku = sudoku;
        this.solverType = solverType;
        reset();
    }

    public void reset() {
        solution = sudoku.getGridCopy();
        operations = 0;
        solving = false;
        runCounter = 0;
        resetSolver();
    }

    protected abstract void resetSolver();

    /**
     * Starts the solving process.
     * A new thread is used for this, to prevent the solver from blocking anything else.
     */
    public void solve() {
        solveThread = new Thread(this::run);
        solveThread.start();
    }

    private void run() {
        solving = true;
        long startTime = System.nanoTime();

        while (solving) {
            useSolver();
            runCounter++;
        }

        long endTime = System.nanoTime();
        float solveTime = (endTime - startTime) / 1000000000.0f;


        postFinishMessage(solveTime);

    }

    protected abstract void useSolver();

    private void postFinishMessage(float time) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getFinishEvent(sudoku, time, operations));
    }

    private void pushSudokuEvent(SudokuEvent event) {
        controller.addEvent(event);
    }

    /**
     * Blocks until the solver has finished solving
     */
    public void waitFor() {
        try {
            if (solveThread.isAlive()) {
                solveThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the solver if it is currently solving, does nothing otherwise
     */
    public void stop() {
        solving = false;
    }

    void setNumber(SudokuPosition position, int num) {
        solution[position.getRow() * 9 + position.getCol()] = num;
        logSetNumber(position, num);
        operations++;
    }

    private void logSetNumber(SudokuPosition position, int newNumber) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getSetNumberEvent(sudoku, position, newNumber));
    }

    public boolean isSolving() {
        return solving;
    }

    protected void postMessage(String message) {
        pushSudokuEvent(SudokuEventFactory.INSTANCE.getPostMessageEvent(sudoku, message));
    }

    SolverType getSolverType() {
        return solverType;
    }

    /**
     * @param colToCheck Zero-indexed column
     * @return True if column is valid, false if there are duplicate numbers
     */
    public boolean validateColumn(int colToCheck) {
        for (int row1 = 0; row1 < 9; row1++) {
            for (int row2 = 0; row2 < row1; row2++) {
                SudokuPosition pos1 = new SudokuPosition(row1, colToCheck);
                SudokuPosition pos2 = new SudokuPosition(row2, colToCheck);
                if (getNumber(pos1) != 0 && getNumber(pos1) == getNumber(pos2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getNumber(SudokuPosition position) {
        return solution[position.getRow() * 9 + position.getCol()];
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
                blockAsRow[index++] = getNumber(new SudokuPosition(i, j));
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
}
