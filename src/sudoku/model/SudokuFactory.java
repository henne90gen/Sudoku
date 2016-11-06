package sudoku.model;

import sudoku.ISudokuController;
import sudoku.SudokuController;

/**
 * Created by henne on 22.10.16.
 */
public class SudokuFactory {

    public static final SudokuFactory INSTANCE = new SudokuFactory();

    private static final Integer[] easy = {
            0, 4, 3, 0, 0, 0, 6, 7, 0,
            0, 0, 0, 2, 9, 3, 0, 0, 4,
            2, 8, 0, 0, 0, 0, 3, 1, 0,
            0, 0, 0, 6, 0, 0, 0, 0, 0,
            1, 0, 0, 5, 0, 7, 0, 0, 8,
            0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 5, 6, 0, 0, 0, 0, 4, 1,
            3, 0, 0, 4, 5, 2, 0, 0, 0,
            0, 2, 7, 0, 0, 0, 9, 5, 0};

    private final Integer[] medium = {
            0, 8, 0, 0, 0, 4, 5, 6, 0,
            0, 6, 0, 0, 0, 0, 0, 1, 7,
            9, 0, 0, 0, 7, 0, 0, 8, 2,
            0, 0, 0, 7, 0, 0, 6, 0, 0,
            0, 0, 5, 1, 0, 6, 7, 0, 0,
            0, 0, 7, 0, 0, 3, 0, 0, 0,
            5, 1, 0, 0, 2, 0, 0, 0, 6,
            3, 2, 0, 0, 0, 0, 0, 4, 0,
            0, 7, 6, 4, 0, 0, 0, 9, 0};

    private final Integer[] hard = {
            5, 0, 0, 0, 0, 0, 0, 3, 6,
            0, 0, 8, 0, 7, 0, 0, 0, 0,
            0, 1, 4, 0, 0, 5, 0, 0, 0,
            9, 0, 0, 6, 0, 0, 0, 0, 0,
            4, 0, 0, 0, 8, 0, 1, 5, 0,
            1, 0, 0, 0, 0, 2, 0, 0, 7,
            8, 4, 0, 1, 0, 0, 9, 0, 0,
            7, 0, 0, 0, 4, 0, 8, 0, 0,
            2, 3, 0, 0, 0, 0, 0, 0, 0};

    private int sudokuIndex = 0;

    private SudokuFactory() {
    }

    public SudokuModel getEasySudoku(ISudokuController controller) {
        return new SudokuModel(controller, getSudokuName(), easy);
    }

    private String getSudokuName() {
        return "Sudoku " + sudokuIndex++;
    }

    public SudokuModel getEasySudoku(ISudokuController controller, Integer[] grid) {
        return new SudokuModel(controller, getSudokuName(), grid);
    }

    public SudokuModel getMediumSudoku(SudokuController controller) {
        return new SudokuModel(controller, getSudokuName(), medium);
    }

    public SudokuModel getHardSudoku(ISudokuController controller) {
        return new SudokuModel(controller, getSudokuName(), hard);
    }
}
