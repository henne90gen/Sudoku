import org.junit.Test;
import sudoku_new.Sudoku;
import sudoku_new.exceptions.IllegalGridException;
import sudoku_new.solver.BruteForceSolver;
import sudoku_new.solver.SmartSolver;
import sudoku_new.solver.Solver;
import sudoku_new.visualizer.ConsoleVisualizer;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by henne on 16.10.16.
 */
public class SolverTest {

    private int[] easy = {0, 4, 3, 0, 0, 0, 6, 7, 0,
            0, 0, 0, 2, 9, 3, 0, 0, 4,
            2, 8, 0, 0, 0, 0, 3, 1, 0,
            0, 0, 0, 6, 0, 0, 0, 0, 0,
            1, 0, 0, 5, 0, 7, 0, 0, 8,
            0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 5, 6, 0, 0, 0, 0, 4, 1,
            3, 0, 0, 4, 5, 2, 0, 0, 0,
            0, 2, 7, 0, 0, 0, 9, 5, 0};

    private int[] easySolution = {9, 4, 3, 1, 8, 5, 6, 7, 2,
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
        Sudoku sudoku = new Sudoku("Test", easy);
        Solver solver = new BruteForceSolver(sudoku);
        solver.addVisualizer(new ConsoleVisualizer(sudoku));
        solver.solve();
        assertSudoku(sudoku, easySolution);
    }

    @Test
    public void testSmart() throws IllegalGridException {
        Sudoku sudoku = new Sudoku("Test", easy);
        Solver solver = new SmartSolver(sudoku);
        solver.addVisualizer(new ConsoleVisualizer(sudoku));
        solver.solve();
        assertSudoku(sudoku, easySolution);
    }

    @Test
    public void testCheckRow() throws IllegalGridException {
        int[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 4;
        Sudoku sudoku = new Sudoku("Test", tmpGrid);
        Solver solver = new Solver(sudoku, Solver.SolverType.Custom) {
            @Override
            protected void startSolving() {
                assertEquals(false, checkRow(0));
                assertEquals(true, checkRow(1));
            }
        };
        solver.solve();
    }

    @Test
    public void testCheckColumn() throws IllegalGridException {
        int[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 2;
        Sudoku sudoku = new Sudoku("Test", tmpGrid);
        Solver solver = new Solver(sudoku, Solver.SolverType.Custom) {
            @Override
            protected void startSolving() {
                assertEquals(false, checkColumn(0));
                assertEquals(true, checkColumn(1));
            }
        };
        solver.solve();
    }

    @Test
    public void testCheckBlock() throws IllegalGridException {
        int[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 8;
        Sudoku sudoku = new Sudoku("Test", tmpGrid);
        Solver solver = new Solver(sudoku, Solver.SolverType.Custom) {
            @Override
            protected void startSolving() {
                assertEquals(false, checkBlock(0, 0));
                assertEquals(true, checkBlock(3, 0));
            }
        };
        solver.solve();
    }

    private void assertSudoku(Sudoku sudoku, int[] solution) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertEquals(solution[row * 9 + col], sudoku.getNumber(row, col));
            }
        }
    }
}