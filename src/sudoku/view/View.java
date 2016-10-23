package sudoku.view;

import sudoku.SudokuController;
import sudoku.view.event.SudokuEvent;

/**
 * Created by henne on 16.10.16.
 */
public abstract class View {

    protected final SudokuController controller;

    protected View(SudokuController controller) {
        this.controller = controller;
        this.controller.addView(this);
    }

    public abstract void open();

    public abstract void handleSudokuEvent(SudokuEvent event);

}
