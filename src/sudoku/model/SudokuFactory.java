package sudoku.model;

import sudoku.ISudokuController;
import sudoku.SudokuController;

/**
 * Created by henne on 22.10.16.
 */
public class SudokuFactory {

    public static final SudokuFactory INSTANCE = new SudokuFactory();

    private static final int[] easy = {0, 4, 3, 0, 0, 0, 6, 7, 0,
            0, 0, 0, 2, 9, 3, 0, 0, 4,
            2, 8, 0, 0, 0, 0, 3, 1, 0,
            0, 0, 0, 6, 0, 0, 0, 0, 0,
            1, 0, 0, 5, 0, 7, 0, 0, 8,
            0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 5, 6, 0, 0, 0, 0, 4, 1,
            3, 0, 0, 4, 5, 2, 0, 0, 0,
            0, 2, 7, 0, 0, 0, 9, 5, 0};

    private int sudokuIndex = 0;

    private SudokuFactory() {
    }

    public SudokuModel getSudoku(ISudokuController controller) {
        return new SudokuModel(controller, getSudokuName(), easy);
    }

    private String getSudokuName() {
        return "Sudoku " + sudokuIndex++;
    }
}
