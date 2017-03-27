package sudoku.controller.listener;

import sudoku.controller.event.SudokuEvent;

public interface ISudokuListener {

	
	public void handleEvent(SudokuEvent event);
}
