package sudoku.controller;

import sudoku.controller.event.SudokuEvent;
import sudoku.controller.listener.SudokuListener;
import sudoku.controller.solver.SolverType;
import sudoku.model.SudokuModel;
import sudoku.view.View;

import java.util.*;

public interface ISudokuController {

	SudokuModel getSudoku(String name);

	Set<String> getSudokuNames();

	void addView(View view);

	void addModel(SudokuModel model);

	void fireEvent(SudokuEvent event);

	void fireEvent(SudokuModel sudoku, SudokuEvent event);

	void addListener(SudokuListener listener);

	void addListener(SudokuModel sudoku, SudokuListener listener);

	default void startSolver(SudokuModel sudoku, SolverType solverType) {
	}
}
