package sudoku.controller.listener;

import sudoku.controller.event.SudokuEvent;

public interface SudokuListener {

	void handleEvent(SudokuEvent event);
}
