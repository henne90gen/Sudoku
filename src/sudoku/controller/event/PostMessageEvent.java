package sudoku.controller.event;

public class PostMessageEvent extends SudokuEvent {

	public PostMessageEvent(String message) {
		super(SudokuEventType.PostMessage);
		this.message = message;
	}
}
