package sudoku.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sudoku.controller.event.SudokuEvent;
import sudoku.controller.listener.ISudokuListener;
import sudoku.model.SudokuModel;
import sudoku.view.View;

public class SudokuController implements ISudokuController {

	private final List<View> views;
	private final Map<String, SudokuModel> models;
	private final List<ISudokuListener> listeners;
	
	public SudokuController() {
		views = new ArrayList<>();
		models = new LinkedHashMap<>();
		listeners = new ArrayList<>();
	}

	public void openAllViews() {
		views.forEach(View::open);
	}
	
	public boolean hasOpenViews() {
		for (View view : views) {
			if (view.isOpen()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public SudokuModel getSudoku(String name) {
		return models.get(name);
	}

	@Override
	public void addView(View view) {
		views.add(view);
	}

	@Override
	public void addModel(SudokuModel sudoku) {
		models.put(sudoku.getName(), sudoku);
	}

	@Override
	public Set<String> getSudokuNames() {
		return models.keySet();
	}

	@Override
	public void addEvent(SudokuEvent event) {
		for (ISudokuListener listener: listeners) {
			listener.handleEvent(event);
		}
	}
	
	@Override
	public void addListener(ISudokuListener listener) {
		listeners.add(listener);
	}
}
