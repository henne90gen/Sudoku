package sudoku;

/**
 * Created by henne on 22.10.16.
 */
public class SudokuFactory {

    public static SudokuFactory INSTANCE = new SudokuFactory();

    private static int[] easy = {0, 4, 3, 0, 0, 0, 6, 7, 0,
            0, 0, 0, 2, 9, 3, 0, 0, 4,
            2, 8, 0, 0, 0, 0, 3, 1, 0,
            0, 0, 0, 6, 0, 0, 0, 0, 0,
            1, 0, 0, 5, 0, 7, 0, 0, 8,
            0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 5, 6, 0, 0, 0, 0, 4, 1,
            3, 0, 0, 4, 5, 2, 0, 0, 0,
            0, 2, 7, 0, 0, 0, 9, 5, 0};

    private int sudokuIndex = 0;

    public SudokuModel getSudoku(SudokuController controller) {
        SudokuModel sudoku = new SudokuModel(controller, getSudokuName(), easy);
        return sudoku;
    }

    private String getSudokuName() {
        return "Sudoku " + sudokuIndex++;
    }
}
