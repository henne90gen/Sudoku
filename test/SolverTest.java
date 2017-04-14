import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import sudoku.controller.ISudokuController;
import sudoku.controller.SudokuController;
import sudoku.controller.SudokuFactory;
import sudoku.controller.solver.SmartSolver;
import sudoku.controller.solver.Solver;
import sudoku.controller.solver.SolverFactory;
import sudoku.controller.solver.SolverType;
import sudoku.model.SudokuModel;
import sudoku.model.SudokuPosition;

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
		SudokuController controller = new SudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, grid);

		controller.startSolver(sudoku, solverType);
		controller.waitForSolver(solverType);

		final Integer[] solution = sudoku.getGridCopy();
		assertSudokuSolution(expectedSolution, solution);
	}

	private void assertSudokuSolution(Integer[] expectedSolution, Integer[] solution) {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				assertEquals("Row: " + row + " | Col: " + col, expectedSolution[row * 9 + col],
						solution[row * 9 + col]);
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
		// testSolver(SolverType.SmartSolver, grid, solution);
	}

	@Test
	public void testScanGrid() {
		ISudokuController controller = new TestSudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller);
		SmartSolver solver = new SmartSolver(controller);
		solver.fillNotesListGrid(sudoku);

		List<Integer>[] expectedScanGrid = TestHelper.INSTANCE.getExpectedScanGrid();
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				List<Integer> notes = expectedScanGrid[row * 9 + col];
				if (notes == null) {
					continue;
				}
				assertNotNull("Row: " + row + " | Col: " + col + " | Notes: " + notes.size(),
						solver.getNotesList(new SudokuPosition(row, col)));
				for (int j = 0; j < notes.size(); j++) {
					assertEquals(notes.get(j), solver.getNotesList(new SudokuPosition(row, col))
							.get(j));
				}
			}
		}
	}

	@Test
	public void testCheckNumberInRow() {
		ISudokuController controller = new TestSudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, TestHelper.INSTANCE.getEasy());
		SudokuPosition topLeft = new SudokuPosition(0, 0);
		SmartSolver solver = new SmartSolver(controller);
		solver.fillNotesListGrid(sudoku);

		assertEquals(true, solver.checkNumberInRow(sudoku, topLeft, 9));
		assertEquals(false, solver.checkNumberInRow(sudoku, topLeft, 5));
		assertEquals(false, solver.checkNumberInRow(sudoku, topLeft, 3));
	}

	@Test
	public void testCheckNumberInColumn() {
		ISudokuController controller = new TestSudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, TestHelper.INSTANCE.getEasy());
		SudokuPosition topLeft = new SudokuPosition(0, 0);
		SmartSolver solver = new SmartSolver(controller);
		solver.fillNotesListGrid(sudoku);

		assertEquals(true, solver.checkNumberInColumn(sudoku, topLeft, 9));
		assertEquals(false, solver.checkNumberInColumn(sudoku, topLeft, 5));
		assertEquals(false, solver.checkNumberInColumn(sudoku, topLeft, 3));
	}

	@Test
	public void testCheckNumberInBlock() {
		ISudokuController controller = new TestSudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, TestHelper.INSTANCE.getEasy());
		SudokuPosition topLeft = new SudokuPosition(0, 0);
		SmartSolver solver = new SmartSolver(controller);
		solver.fillNotesListGrid(sudoku);

		assertEquals(true, solver.checkNumberInBlock(sudoku, topLeft, 9));
		assertEquals(false, solver.checkNumberInBlock(sudoku, topLeft, 5));
		assertEquals(false, solver.checkNumberInBlock(sudoku, topLeft, 3));
	}

	@Test
	public void testValidateRow() {
		Integer[] easy = TestHelper.INSTANCE.getEasy();
		Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
		tmpGrid[0] = 4;
		ISudokuController controller = new TestSudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, tmpGrid);
		Solver solver = SolverFactory.getBruteSolver(controller);

		assertEquals(false, solver.validateRow(sudoku, 0));
		assertEquals(true, solver.validateRow(sudoku, 1));
	}

	@Test
	public void testValidateColumn() {
		Integer[] easy = TestHelper.INSTANCE.getEasy();
		Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
		tmpGrid[0] = 2;
		ISudokuController controller = new TestSudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, tmpGrid);
		Solver solver = SolverFactory.getBruteSolver(controller);

		assertEquals(false, solver.validateColumn(sudoku, 0));
		assertEquals(true, solver.validateColumn(sudoku, 1));
	}

	@Test
	public void testValidateBlock() {
		Integer[] easy = TestHelper.INSTANCE.getEasy();
		Integer[] tmpGrid = Arrays.copyOf(easy, easy.length);
		tmpGrid[0] = 8;
		ISudokuController controller = new TestSudokuController();
		SudokuModel sudoku = SudokuFactory.INSTANCE.getEasySudoku(controller, tmpGrid);
		Solver solver = SolverFactory.getBruteSolver(controller);

		assertEquals(false, solver.validateBlock(sudoku, 0, 0));
		assertEquals(true, solver.validateBlock(sudoku, 3, 0));
	}

	@SuppressWarnings("unused")
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