package sudoku.view.event;

import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

/**
 * Created by henne on 22.10.16.
 */
public abstract class SudokuEvent {

    private final SudokuEventType eventType;

    private final SudokuModel sudoku;

    String message;

    SudokuPosition position;

    int newNumber;

    float time;

    int operations;

    SudokuEvent(SudokuEventType eventType, SudokuModel sudoku) {
        this.eventType = eventType;
        this.sudoku = sudoku;
    }

    public SudokuEventType getType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public SudokuModel getSudoku() {
        return sudoku;
    }

    public SudokuPosition getPosition() {
        return position;
    }

    public int getNewNumber() {
        return newNumber;
    }

    public float getTime() {
        return time;
    }

    public int getOperations() {
        return operations;
    }
}
