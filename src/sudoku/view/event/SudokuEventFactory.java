package sudoku.view.event;

import sudoku.model.SudokuModel;

/**
 * Created by henne on 22.10.16.
 */
public class SudokuEventFactory {

    public static final SudokuEventFactory INSTANCE = new SudokuEventFactory();

    public SudokuEvent getPostMessageEvent(SudokuModel sudoku, String message) {
        return new PostMessageEvent(sudoku, message);
    }

    public SudokuEvent getSetNumberEvent(SudokuModel sudoku, int row, int col, int newNumber) {
        return new SetNumberEvent(sudoku, row, col, newNumber);
    }

    public SudokuEvent getFinishEvent(SudokuModel sudoku, float time) {
        return new FinishedSolvingEvent(sudoku, time);
    }
}
