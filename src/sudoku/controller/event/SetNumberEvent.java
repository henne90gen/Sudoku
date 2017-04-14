package sudoku.controller.event;

import sudoku.model.SudokuPosition;

public class SetNumberEvent extends SudokuEvent {

	public SetNumberEvent(SudokuPosition position, int newNumber) {
		super(SudokuEventType.SetNumber);
		this.message = "Placing number " + newNumber + " at position " + position.getRow() + " " + position.getCol();
		this.position = position;
		this.newNumber = newNumber;
	}
}
