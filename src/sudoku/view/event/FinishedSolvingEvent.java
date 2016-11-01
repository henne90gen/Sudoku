package sudoku.view.event;

import sudoku.model.SudokuModel;

/**
 * Created by henne on 23.10.16.
 */
class FinishedSolvingEvent extends SudokuEvent {

    FinishedSolvingEvent(SudokuModel sudoku, float time, int operations) {
        super(SudokuEventType.FinishedSolving, sudoku);
        this.message = "Solved '" + sudoku.getName() + "' in " + time + "ms with " + operations + " " +
                "operations";
        this.time = time;
        this.operations = operations;
    }
}
