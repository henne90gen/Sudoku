package sudoku;

import sudoku.model.SudokuFactory;
import sudoku.model.SudokuModel;
import sudoku.view.View;
import sudoku.view.event.SudokuEvent;
import sudoku.view.swt.SWTView;

import java.util.*;

/**
 * Created by henne on 19.10.16.
 */
public class SudokuController implements ISudokuController {

    private final List<View> views;
    private final Map<String, SudokuModel> sudokus;

    private SudokuController() {
        views = new ArrayList<>();
        sudokus = new LinkedHashMap<>();

        SudokuModel sudokuModel1 = SudokuFactory.INSTANCE.getEasySudoku(this);
        sudokus.put(sudokuModel1.getName(), sudokuModel1);
        SudokuModel sudokuModel2 = SudokuFactory.INSTANCE.getMediumSudoku(this);
        sudokus.put(sudokuModel2.getName(), sudokuModel2);
        SudokuModel sudokuModel3 = SudokuFactory.INSTANCE.getHardSudoku(this);
        sudokus.put(sudokuModel3.getName(), sudokuModel3);

//        new ConsoleView(this);
        new SWTView(this);

        openViews();
    }

    public void openViews() {
        views.forEach(View::open);
    }

    public static void main(String[] args) {
        new SudokuController();
    }

    public SudokuModel getSudoku(String name) {
        return sudokus.get(name);
    }

    public Set<String> getSudokuNames() {
        return sudokus.keySet();
    }

    public void addView(View view) {
        views.add(view);
    }

    public void handleSudokuEvent(SudokuEvent event) {
        for (View view : views) {
            view.handleSudokuEvent(event);
        }
    }
}
