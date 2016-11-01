package sudoku;

import sudoku.model.SudokuModel;
import sudoku.view.View;
import sudoku.view.event.SudokuEvent;

import java.util.*;

/**
 * Created by henne on 25.10.16.
 */
public interface ISudokuController {

    SudokuModel getSudoku(String name);

    Set<String> getSudokuNames();

    void addView(View view);

    void handleSudokuEvent(SudokuEvent event);
}
