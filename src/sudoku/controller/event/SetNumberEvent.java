package sudoku.controller.event;

import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

/**
 * Created by henne on 23.10.16.
 */
class SetNumberEvent extends SudokuEvent {

    public SetNumberEvent(SudokuModel sudoku, SudokuPosition position, int newNumber) {
        super(SudokuEventType.SetNumber, sudoku);
        this.position = position;
        this.message = "Placing number " + newNumber + " at position " + position.getRow() + " " + position.getCol();
        this.newNumber = newNumber;
    }
}
