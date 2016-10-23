package sudoku.view.swing;

import sudoku.SudokuController;
import sudoku.view.View;
import sudoku.view.event.SudokuEvent;

import javax.swing.*;

/**
 * Created by henne on 16.10.16.
 */
public class SwingView extends View {

    private static final String FRAME_TITLE = "Sudoku SwingView";
    private JFrame frame;
    private JLabel messageLabel;
    private JLabel[] grid;

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

    @Override
    public void handleSudokuEvent(SudokuEvent event) {

    }
}
