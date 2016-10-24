package sudoku.view.event;

import sudoku.model.SudokuModel;

/**
 * Created by henne on 23.10.16.
 */
public class SetNumberEvent extends SudokuEvent {

    public SetNumberEvent(SudokuModel sudoku, int row, int col, int newNumber) {
        super(SudokuEventType.SetNumber, sudoku);
        this.message = "Placing number " + newNumber + " at position " + row + " " + col;
        this.row = row;
        this.col = col;
        this.newNumber = newNumber;
    }
}
