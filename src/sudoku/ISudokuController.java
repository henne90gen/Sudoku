package sudoku;

import sudoku.model.SudokuFactory;
import sudoku.model.SudokuModel;
import sudoku.view.View;
import sudoku.view.event.SudokuEvent;
import sudoku.view.swt.SWTView;

import java.util.*;

/**
 * Created by henne on 25.10.16.
 */
public interface ISudokuController {

//    List<View> views;
//    Map<String, SudokuModel> sudokus;

    SudokuModel getSudoku(String name);

    Set<String> getSudokuNames();

    void addView(View view);

    void openViews();

    void handleSudokuEvent(SudokuEvent event);
}
