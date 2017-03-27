package sudoku;

import sudoku.controller.SudokuController;
import sudoku.model.SudokuFactory;
import sudoku.view.swt.SWTView;

public class Main {

	public static void main(String[] args) {
		SudokuController controller = new SudokuController();
		SudokuFactory.INSTANCE.getEasySudoku(controller);
		SudokuFactory.INSTANCE.getMediumSudoku(controller);
		SudokuFactory.INSTANCE.getHardSudoku(controller);
		// new ConsoleView(controller);
		new SWTView(controller);
	}
}
