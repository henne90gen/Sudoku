package sudoku.view.event;

import sudoku.model.SudokuModel;

/**
 * Created by henne on 23.10.16.
 */
class PostMessageEvent extends SudokuEvent {

    PostMessageEvent(SudokuModel sudoku, String message) {
        super(SudokuEventType.PostMessage, sudoku);
        this.message = message;
    }

}
