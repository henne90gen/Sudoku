package sudoku.view.event;

import sudoku.model.SudokuModel;
import sudoku.solver.SolverType;

/**
 * Created by henne on 23.10.16.
 */
public class PostMessageEvent extends SudokuEvent {

    public PostMessageEvent(SudokuModel sudoku, String message) {
        super(SudokuEventType.PostMessage, sudoku);
        this.message = message;
    }

}
