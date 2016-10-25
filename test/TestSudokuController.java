import sudoku.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.view.View;
import sudoku.view.event.SudokuEvent;

import java.util.Set;

/**
 * Created by henne on 25.10.16.
 */
public class TestSudokuController implements ISudokuController {
    @Override
    public SudokuModel getSudoku(String name) {
        return null;
    }

    @Override
    public Set<String> getSudokuNames() {
        return null;
    }

    @Override
    public void addView(View view) {

    }

    @Override
    public void openViews() {

    }

    @Override
    public void handleSudokuEvent(SudokuEvent event) {

    }
}
