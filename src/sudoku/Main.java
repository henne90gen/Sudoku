package sudoku;

import sudoku.controller.SudokuController;
import sudoku.controller.SudokuFactory;
import sudoku.view.lwjgl.LWJGLView;
import sudoku.view.swt.SWTView;

public class Main {

	public static void main(String[] args) {
		SudokuController controller = new SudokuController();
		controller.addModel(SudokuFactory.INSTANCE.getEasySudoku(controller));
		controller.addModel(SudokuFactory.INSTANCE.getMediumSudoku(controller));
		controller.addModel(SudokuFactory.INSTANCE.getHardSudoku(controller));
//		new SWTView(controller);
		new LWJGLView(controller);
	}
}
