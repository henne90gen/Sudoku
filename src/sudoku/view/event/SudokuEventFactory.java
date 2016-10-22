package sudoku.view.event;

import sudoku.SudokuModel;

/**
 * Created by henne on 22.10.16.
 */
public class SudokuEventFactory {

    public static SudokuEventFactory INSTANCE = new SudokuEventFactory();

    public SudokuEvent getPostMessageEvent(SudokuModel sudoku, String message) {
        return new SudokuEvent(SudokuEventType.PostMessage, sudoku, message);
    }

    public SudokuEvent getSetNumberEvent(SudokuModel sudoku, int row, int col, int newNumber) {
        return new SudokuEvent(SudokuEventType.SetNumber, sudoku, row, col, newNumber);
    }

    public SudokuEvent getFinishEvent(SudokuModel sudoku, String message) {
        return new SudokuEvent(SudokuEventType.FinishedSolving, sudoku, message);
    }
}
