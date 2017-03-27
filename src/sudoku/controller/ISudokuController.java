package sudoku.controller;

import sudoku.controller.event.SudokuEvent;
import sudoku.controller.listener.ISudokuListener;
import sudoku.model.SudokuModel;
import sudoku.view.View;

import java.util.*;

/**
 * Created by henne on 25.10.16.
 */
public interface ISudokuController {

	SudokuModel getSudoku(String name);

	Set<String> getSudokuNames();

	void addView(View view);

	void addModel(SudokuModel model);

	void addEvent(SudokuEvent event);

	void addListener(ISudokuListener listener);
}
