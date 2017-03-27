package sudoku.view;

import sudoku.controller.ISudokuController;

public abstract class View {

	protected final ISudokuController controller;

	private boolean open;

	protected View(ISudokuController controller) {
		this.controller = controller;
		this.controller.addView(this);
		open = true;
		open();
	}

	public abstract void open();

	public void close() {
		open = false;
	}

	public boolean isOpen() {
		return open;
	}
}
