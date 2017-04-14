package sudoku.view.swing;

import javax.swing.JFrame;
import javax.swing.JLabel;

import sudoku.controller.SudokuController;
import sudoku.view.View;

public class SwingView extends View {

	private static final String FRAME_TITLE = "Sudoku SwingView";
	private final JFrame frame;
	private final JLabel messageLabel;
	private final JLabel[] grid;

	protected SwingView(SudokuController controller) {
		super(controller);
		frame = new JFrame(FRAME_TITLE);

		messageLabel = new JLabel();

		grid = new JLabel[81];
		for (int i = 0; i < 81; i++) {
			grid[i] = new JLabel();
		}
	}

	@Override
	public void open() {

	}
}
