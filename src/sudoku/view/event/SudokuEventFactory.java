package sudoku.view.event;

import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

/**
 * Created by henne on 22.10.16.
 */
public class SudokuEventFactory {

    public static final SudokuEventFactory INSTANCE = new SudokuEventFactory();

    public SudokuEvent getPostMessageEvent(SudokuModel sudoku, String message) {
        return new PostMessageEvent(sudoku, message);
    }

    public SudokuEvent getSetNumberEvent(SudokuModel sudoku, SudokuPosition position, int newNumber) {
        return new SetNumberEvent(sudoku, position, newNumber);
    }

    public SudokuEvent getFinishEvent(SudokuModel sudoku, float time) {
        return new FinishedSolvingEvent(sudoku, time);
    }
}
