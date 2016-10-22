package sudoku.view.event;

import sudoku.SudokuModel;

/**
 * Created by henne on 22.10.16.
 */
public class SudokuEvent {

    private SudokuEventType eventType;

    private SudokuModel sudoku;

    private String message;

    private int row;

    private int col;

    private int newNumber;

    public SudokuEvent(SudokuEventType eventType, SudokuModel sudoku, String message) {
        this.eventType = eventType;
        this.sudoku = sudoku;
        this.message = message;
    }

    public SudokuEvent(SudokuEventType eventType, SudokuModel sudoku, int row, int col, int newNumber) {
        this(eventType, sudoku, "Placing number " + newNumber + " at position " + row + " " + col);
        this.row = row;
        this.col = col;
        this.newNumber = newNumber;
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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNewNumber() {
        return newNumber;
    }
}
