import org.junit.Test;
import sudoku.ISudokuController;
import sudoku.exceptions.IllegalGridException;
import sudoku.model.SudokuFactory;
import sudoku.model.SudokuModel;
import sudoku.solver.Solver;
import sudoku.solver.SolverFactory;
import sudoku.solver.SolverType;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by henne on 16.10.16.
 */
public class SolverTest {

    private Integer[] easy = {0, 4, 3, 0, 0, 0, 6, 7, 0,
            0, 0, 0, 2, 9, 3, 0, 0, 4,
            2, 8, 0, 0, 0, 0, 3, 1, 0,
            0, 0, 0, 6, 0, 0, 0, 0, 0,
            1, 0, 0, 5, 0, 7, 0, 0, 8,
            0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 5, 6, 0, 0, 0, 0, 4, 1,
            3, 0, 0, 4, 5, 2, 0, 0, 0,
            0, 2, 7, 0, 0, 0, 9, 5, 0};

    private Integer[] easySolution = {9, 4, 3, 1, 8, 5, 6, 7, 2,
            6, 7, 1, 2, 9, 3, 5, 8, 4,
            2, 8, 5, 7, 4, 6, 3, 1, 9,
            7, 9, 4, 6, 2, 8, 1, 3, 5,
            1, 6, 2, 5, 3, 7, 4, 9, 8,
            5, 3, 8, 9, 1, 4, 7, 2, 6,
            8, 5, 6, 3, 7, 9, 2, 4, 1,
            3, 1, 9, 4, 5, 2, 8, 6, 7,
            4, 2, 7, 8, 6, 1, 9, 5, 3};

    @Test
    public void testBruteForce() throws IllegalGridException {
        testSolver(SolverType.BruteForceSolver);
    }

    @Test
    public void testSmart() throws IllegalGridException {
        testSolver(SolverType.SmartSolver);
    }

    private void testSolver(SolverType solverType) {
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getSudoku(controller);
        sudoku.solveUsingSolver(solverType);
        sudoku.waitForSolver(solverType);
        final Integer[] solution = sudoku.getSolution(solverType);
        assertSudokuSolution(easySolution, solution);
    }

    @Test
    public void testCheckRow() throws IllegalGridException {
        Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 4;
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getSudoku(controller, tmpGrid);
        Solver solver = SolverFactory.INSTANCE.getBruteSolver(controller, sudoku);

        assertEquals(false, solver.validateRow(0));
        assertEquals(true, solver.validateRow(1));
    }

    @Test
    public void testCheckColumn() throws IllegalGridException {
        Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 2;
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getSudoku(controller, tmpGrid);
        Solver solver = SolverFactory.INSTANCE.getBruteSolver(controller, sudoku);

        assertEquals(false, solver.validateColumn(0));
        assertEquals(true, solver.validateColumn(1));
    }

    @Test
    public void testCheckBlock() throws IllegalGridException {
        Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 8;
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getSudoku(controller, tmpGrid);
        Solver solver = SolverFactory.INSTANCE.getBruteSolver(controller, sudoku);

        assertEquals(false, solver.validateBlock(0, 0));
        assertEquals(true, solver.validateBlock(3, 0));
    }

    private void assertSudokuSolution(Integer[] expectedSolution, Integer[] solution) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertEquals("Row: " + row + " | Col: " + col, expectedSolution[row * 9 + col], solution[row * 9 +
                        col]);
            }
        }
    }

    private void printGrid(Integer[] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(grid[row * 9 + col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}