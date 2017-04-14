package sudoku.model;

import java.io.File;
import java.util.Arrays;

import sudoku.controller.ISudokuController;
import sudoku.controller.event.SetNumberEvent;
import sudoku.controller.event.SudokuEvent;
import sudoku.exceptions.IllegalGridException;

public class SudokuModel {

	private ISudokuController controller;

	private final String name;

	private Integer[] grid;

	private Boolean[] editableFields;

	public SudokuModel(ISudokuController controller, String name, Integer[] grid) throws IllegalGridException {
		this.controller = controller;
		this.name = name;
		if (grid.length != 81) {
			throw new IllegalGridException();
		}
		this.grid = new Integer[81];
		System.arraycopy(grid, 0, this.grid, 0, 81);

		editableFields = new Boolean[81];
		for (int i = 0; i < 81; i++) {
			editableFields[i] = grid[i] == 0;
		}
	}

	public String getName() {
		return name;
	}

	public Integer[] getGridCopy() {
		return Arrays.copyOf(grid, grid.length);
	}

	public int getNumber(int row, int col) {
		return grid[row * 9 + col];
	}

	public boolean setNumber(int row, int col, int value) {
		if (isFieldEditable(row, col)) {
			grid[row * 9 + col] = value;
			SudokuEvent event = new SetNumberEvent(new SudokuPosition(row, col), value);
			controller.fireEvent(this, event);
			return true;
		}
		return false;
	}

	public boolean isFieldEditable(int row, int col) {
		return editableFields[row * 9 + col];
	}

	public void printToFile(File file) {
		// TODO implement this
	}

	public void reset() {
		for (int i = 0; i < 81; i++) {
			if (editableFields[i]) {
				grid[i] = 0;
			}
		}
	}
}
