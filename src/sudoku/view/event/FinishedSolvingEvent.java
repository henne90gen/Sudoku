package sudoku.view.event;

import sudoku.model.SudokuModel;

/**
 * Created by henne on 23.10.16.
 */
public class FinishedSolvingEvent extends SudokuEvent {

    public FinishedSolvingEvent(SudokuModel sudoku, float time) {
        super(SudokuEventType.FinishedSolving, sudoku);
        this.message = "Solved " + sudoku.getName() + " in " + (time / 1000) + "s";
        this.time = time;
    }
}
