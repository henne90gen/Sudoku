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
public class SudokuController {

    private List<View> views;
    private Map<String, SudokuModel> sudokus;

    public SudokuController() {
        views = new ArrayList<>();
        sudokus = new LinkedHashMap<>();

        SudokuModel sudoku = SudokuFactory.INSTANCE.getSudoku(this);
        sudokus.put(sudoku.getName(), sudoku);

//        new ConsoleView(this);
        new SWTView(this);

        openViews();
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

    private void openViews() {
        views.forEach(View::open);
    }

    public void handleSudokuEvent(SudokuEvent event) {
        for (View view : views) {
            view.handleSudokuEvent(event);
        }
    }
}
