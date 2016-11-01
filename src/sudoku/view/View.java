package sudoku.view;

import sudoku.ISudokuController;
import sudoku.view.event.SudokuEvent;

/**
 * Created by henne on 16.10.16.
 */
public abstract class View {

    protected final ISudokuController controller;

    protected View(ISudokuController controller) {
        this.controller = controller;
        this.controller.addView(this);
    }

    public abstract void open();

    public abstract void handleSudokuEvent(SudokuEvent event);

}
