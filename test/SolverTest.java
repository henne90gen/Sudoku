import org.junit.Test;
import sudoku.ISudokuController;
import sudoku.model.SudokuFactory;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;
import sudoku.solver.SmartSolver;
import sudoku.solver.Solver;
import sudoku.solver.SolverFactory;
import sudoku.solver.SolverType;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by henne on 16.10.16.
 */
public class SolverTest {

    @Test
    public void testBruteForceWithEasy() {
        Integer[] grid = TestHelper.INSTANCE.getEasy();
        Integer[] solution = TestHelper.INSTANCE.getEasySolution();
        testSolver(SolverType.BruteForceSolver, grid, solution);
    }

    private void testSolver(SolverType solverType, Integer[] grid, Integer[] expectedSolution) {
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, grid);

        sudoku.startSolver(solverType);
        sudoku.waitForSolver(solverType);

        final Integer[] solution = sudoku.getSolution(solverType);
        assertSudokuSolution(expectedSolution, solution);
    }

    private void assertSudokuSolution(Integer[] expectedSolution, Integer[] solution) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertEquals("Row: " + row + " | Col: " + col, expectedSolution[row * 9 + col], solution[row * 9 +
                        col]);
            }
        }
    }

    @Test
    public void testBruteForceWithMedium() {
        Integer[] grid = TestHelper.INSTANCE.getMedium();
        Integer[] solution = TestHelper.INSTANCE.getMediumSolution();
        testSolver(SolverType.BruteForceSolver, grid, solution);
    }

    @Test
    public void testBruteForceWithHard() {
        Integer[] grid = TestHelper.INSTANCE.getHard();
        Integer[] solution = TestHelper.INSTANCE.getHardSolution();
        testSolver(SolverType.BruteForceSolver, grid, solution);
    }

    @Test
    public void testSmartWithEasy() {
        Integer[] grid = TestHelper.INSTANCE.getEasy();
        Integer[] solution = TestHelper.INSTANCE.getEasySolution();
        testSolver(SolverType.SmartSolver, grid, solution);
    }

    @Test
    public void testSmartWithMedium() {
        Integer[] grid = TestHelper.INSTANCE.getMedium();
        Integer[] solution = TestHelper.INSTANCE.getMediumSolution();
        testSolver(SolverType.SmartSolver, grid, solution);
    }

    @Test
    public void testSmartWithHard() {
        Integer[] grid = TestHelper.INSTANCE.getHard();
        Integer[] solution = TestHelper.INSTANCE.getHardSolution();
        testSolver(SolverType.SmartSolver, grid, solution);
    }

    @Test
    public void testScanGrid() {
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller);
        SmartSolver solver = new SmartSolver(controller, sudoku);
        solver.fillNotesListGrid();

        List<Integer>[] expectedScanGrid = TestHelper.INSTANCE.getExpectedScanGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                List<Integer> notes = expectedScanGrid[row * 9 + col];
                if (notes == null) {
                    continue;
                }
                assertNotNull("Row: " + row + " | Col: " + col + " | Notes: " + notes.size(), solver.getNotesList(new SudokuPosition(row, col)));
                for (int j = 0; j < notes.size(); j++) {
                    assertEquals(notes.get(j), solver.getNotesList(new SudokuPosition(row, col)).get(j));
                }
            }
        }
    }

    @Test
    public void testCheckNumberInRow() {
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, TestHelper.INSTANCE.getEasy());
        SudokuPosition topLeft = new SudokuPosition(0, 0);
        SmartSolver solver = new SmartSolver(controller, sudoku);
        solver.fillNotesListGrid();

        assertEquals(true, solver.checkNumberInRow(topLeft, 9));
        assertEquals(false, solver.checkNumberInRow(topLeft, 5));
        assertEquals(false, solver.checkNumberInRow(topLeft, 3));
    }

    @Test
    public void testCheckNumberInColumn() {
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, TestHelper.INSTANCE.getEasy());
        SudokuPosition topLeft = new SudokuPosition(0, 0);
        SmartSolver solver = new SmartSolver(controller, sudoku);
        solver.fillNotesListGrid();

        assertEquals(true, solver.checkNumberInColumn(topLeft, 9));
        assertEquals(false, solver.checkNumberInColumn(topLeft, 5));
        assertEquals(false, solver.checkNumberInColumn(topLeft, 3));
    }

    @Test
    public void testCheckNumberInBlock() {
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, TestHelper.INSTANCE.getEasy());
        SudokuPosition topLeft = new SudokuPosition(0, 0);
        SmartSolver solver = new SmartSolver(controller, sudoku);
        solver.fillNotesListGrid();

        assertEquals(true, solver.checkNumberInBlock(topLeft, 9));
        assertEquals(false, solver.checkNumberInBlock(topLeft, 5));
        assertEquals(false, solver.checkNumberInBlock(topLeft, 3));
    }

    @Test
    public void testValidateRow() {
        Integer[] easy = TestHelper.INSTANCE.getEasy();
        Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 4;
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, tmpGrid);
        Solver solver = SolverFactory.INSTANCE.getBruteSolver(controller, sudoku);

        assertEquals(false, solver.validateRow(0));
        assertEquals(true, solver.validateRow(1));
    }

    @Test
    public void testValidateColumn() {
        Integer[] easy = TestHelper.INSTANCE.getEasy();
        Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 2;
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, tmpGrid);
        Solver solver = SolverFactory.INSTANCE.getBruteSolver(controller, sudoku);

        assertEquals(false, solver.validateColumn(0));
        assertEquals(true, solver.validateColumn(1));
    }

    @Test
    public void testValidateBlock() {
        Integer[] easy = TestHelper.INSTANCE.getEasy();
        Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
        tmpGrid[0] = 8;
        ISudokuController controller = new TestSudokuController();
        SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, tmpGrid);
        Solver solver = SolverFactory.INSTANCE.getBruteSolver(controller, sudoku);

        assertEquals(false, solver.validateBlock(0, 0));
        assertEquals(true, solver.validateBlock(3, 0));
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