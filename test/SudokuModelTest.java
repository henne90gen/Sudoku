import org.junit.Test;
import sudoku.SudokuModel;
import sudoku.exceptions.IllegalGridException;

import static org.junit.Assert.assertEquals;

/**
 * Created by henne on 16.10.16.
 */
public class SudokuModelTest {

    private int[] grid = {0, 4, 3, 0, 0, 0, 6, 7, 0,
            0, 0, 0, 2, 9, 3, 0, 0, 4,
            2, 8, 0, 0, 0, 0, 3, 1, 0,
            0, 0, 0, 6, 0, 0, 0, 0, 0,
            1, 0, 0, 5, 0, 7, 0, 0, 8,
            0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 5, 6, 0, 0, 0, 0, 4, 1,
            3, 0, 0, 4, 5, 2, 0, 0, 0,
            0, 2, 7, 0, 0, 0, 9, 5, 0};

    @Test
    public void testConstructor() throws IllegalGridException {
//        SudokuModel sudoku = new SudokuModel("Hello", grid);
//        for (int i = 0; i < grid.length; i++) {
//            assertEquals(grid[i], (long)sudoku.getGridCopy()[i]);
//        }
    }
}
