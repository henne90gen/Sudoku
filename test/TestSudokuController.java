import java.util.Set;

import sudoku.controller.ISudokuController;
import sudoku.controller.event.SudokuEvent;
import sudoku.controller.listener.SudokuListener;
import sudoku.controller.solver.SolverType;
import sudoku.model.SudokuModel;
import sudoku.view.View;

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
	public void fireEvent(SudokuEvent event) {

	}

	@Override
	public void addModel(SudokuModel model) {

	}

	@Override
	public void addListener(SudokuListener listener) {

	}

	@Override
	public void fireEvent(SudokuModel sudoku, SudokuEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addListener(SudokuModel sudoku, SudokuListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSolverRunning(SudokuModel sudoku, SolverType bruteforcesolver) {
		// TODO Auto-generated method stub
		return false;
	}
}
