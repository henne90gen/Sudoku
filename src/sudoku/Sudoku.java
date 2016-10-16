package sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

//import sudoku.solver.BruteForceSolver;
//import sudoku.solver.SmartSolver;
//import sudoku.solver.SudokuSolver;

public class Sudoku {

	public enum FeedbackMode {
		Console, Window, Silent
	}
	public enum SolverType {
		BruteForce, Smart
	}
	
	private int[][] grid;
	private int[][] originalGrid;
	@SuppressWarnings("unused")
	private int[][] solution;
	private FeedbackMode feedback;
	private SolverType solverType;
	private String name = "Sudoku";
//	private SudokuSolver solver;
	private SudokuWindow window;
	
	public Sudoku(String name, SolverType solverType, FeedbackMode feedback, int[][] grid, int[][] solution) {
		this(name, solverType, feedback, grid);
		this.solution = solution;
	}
	
	public Sudoku(String name, SolverType solverType, FeedbackMode feedback, int[][] grid) {
		this.name = name;
		this.feedback = feedback;
		this.grid = new int[9][9];
		
		for (int i = 0; i < 9; i++) {
			this.grid[i] = Arrays.copyOfRange(grid[i], 0, grid[i].length);
		}
		
		originalGrid = new int[9][9];
		for (int i = 0; i < 9; i++) {
			originalGrid[i] = Arrays.copyOfRange(grid[i], 0, grid[i].length);
		}
		if (feedback == FeedbackMode.Window) {
			window = new SudokuWindow(this);
		}
//		setSolverType(solverType);
	}
	
//	public boolean solve(boolean wait) {
//		Thread thread = new Thread(solver);
//		thread.start();
//		if (wait) {
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				solver.stopSolving();
//				resetGrid();
//			}
//			return solver.getSolved();
//		}
//		return true;
//	}
	
	public boolean checkGridValidity() {
		boolean gridValid = true;//solver.checkGridValidity();
		if (gridValid && feedback == FeedbackMode.Console)
			System.out.println("Sudoku " + name + " is valid.");
		else if (!gridValid && feedback == FeedbackMode.Console) {
			System.out.println("Sudoku " + name + " is not valid.");
		}
		return gridValid;
	}
	
	public void print() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(grid[j][i]);
			}
			System.out.print("\n");
		}
	}
	
	public static void print(int[][] grid) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(grid[j][i]);
			}
			System.out.print("\n");
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumber(int col, int row) {
		return grid[col][row];
	}
	
	public void setNumber(int col, int row, int number, boolean highlight) {
		grid[col][row] = number;
		if (feedback == FeedbackMode.Window) {
			window.updateVisualGrid();
			window.highlightNumber(col, row, highlight);
		}
	}
	
	public int getOriginalNumber(int col, int row) {
		return originalGrid[col][row];
	}
	
//	public float getSolveTime() {
//		return solver.getSolveTime();
//	}
	
	public int[][] getGrid() {
		return grid;
	}
	
	public void setGrid(int[][] grid) {
		this.grid = new int[9][9];
		
		for (int i = 0; i < 9; i++) {
			this.grid[i] = Arrays.copyOfRange(grid[i], 0, grid[i].length);
		}
	}
	
	public void resetGrid() {
		grid = new int[9][9];
		for (int i = 0; i < 9; i++) {
			grid[i] = Arrays.copyOfRange(originalGrid[i], 0, originalGrid[i].length);
		}
		if (feedback == FeedbackMode.Window) {
			window.resetGrid();
		}
	}
	
	public int[][] getOriginalGrid() {
		return originalGrid;
	}
	
	public void setOriginalGrid(int[][] originalGrid) {
		this.originalGrid = originalGrid;
	}
	
	public FeedbackMode getFeedbackMode() {
		return feedback;
	}
	
	public void setFeedbackMode(FeedbackMode feedback) {
		this.feedback = feedback;
	}
	
	public SolverType getSolverType() {
		return solverType;
	}
	
//	public void setSolverType(SolverType solverType) {
//		this.solverType = solverType;
//		if (this.feedback == FeedbackMode.Window) {
//			switch (solverType) {
//			case BruteForce:
//				solver = new BruteForceSolver(this, window);
//				break;
//			case Smart:
//				solver = new SmartSolver(this, window);
//				break;
//			}
//		} else {
//			switch (solverType) {
//			case BruteForce:
//				solver = new BruteForceSolver(this);
//				break;
//			case Smart:
//				solver = new SmartSolver(this);
//				break;
//			}
//		}
//	}
	
	public void openWindow() {
		window.open();
	}
	
	public void addToPanel(JPanel panel) {
		window.addToPanel(panel);
	}
	
	public void removeFromPanel(JPanel panel) {
		window.removeFromPanel(panel);
	}
	
	public void saveToFile(String name, boolean printSolution) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(name, true));
			if (printSolution) {
				writer.write("Original    Solution");
				writer.newLine();
			} else {
				writer.write("Sudoku");
				writer.newLine();
			}
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					writer.write(Integer.toString(originalGrid[j][i]));
				}
				if (printSolution) {
					writer.write("   ");
					for (int j = 0; j < 9; j++) {
						writer.write(Integer.toString(grid[j][i]));
					}
				}
				writer.newLine();
			}
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<int[][]> readFromFile(String name, boolean hasSolution) {
		ArrayList<int[][]> result = new ArrayList<int[][]>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(name));
			reader.readLine();
			boolean nextSudoku = true;
			while (nextSudoku) {
				int[][] grid = new int[9][9];
				int[][] solution = new int[9][9];
				for (int i = 0; i < 9; i++) {
					if (hasSolution) {
						for (int j = 0; j < 9; j++) {
							grid[j][i] = Character.getNumericValue(reader.read());
						}
						for (int k = 0; k < 3; k++) {
							reader.read();
						}
						for (int j = 0; j < 9; j++) {
							solution[j][i] = Character.getNumericValue(reader.read());
						}
						reader.readLine();
					} else {
						for (int j = 0; j < 9; j++) {
							grid[j][i] = Character.getNumericValue(reader.read());
						}
						reader.readLine();
					}
				}
				result.add(grid);
				if (hasSolution) {
					result.add(solution);
				}
				reader.readLine();
				if (hasSolution) {
					int tmp = reader.read();
					nextSudoku = (tmp == 'O' || tmp == 'S');
				} else {
					nextSudoku = (reader.read() == 'S');
				}
				reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public void exportAsPDF(String name) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("pdf/" + name));
			document.open();
			
			PdfPTable table = new PdfPTable(9);
			table.setTotalWidth(360);
			table.setLockedWidth(true);
			Font font = FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					String number = Integer.toString(grid[j][i]);
					if (grid[j][i] == 0)
						number = "";
					PdfPCell cell = new PdfPCell(new Phrase(number, font));
					cell.setFixedHeight(40);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					if (i == 8 || j == 8) {
						cell.setBorder(com.itextpdf.text.Rectangle.RIGHT | com.itextpdf.text.Rectangle.BOTTOM | com.itextpdf.text.Rectangle.LEFT | com.itextpdf.text.Rectangle.TOP);
					} else {
						cell.setBorder(com.itextpdf.text.Rectangle.LEFT | com.itextpdf.text.Rectangle.TOP);
					}
					
					if (i == 0 || i%3 == 0) {
						cell.setBorderWidthTop(2);
						cell.setBorderWidthBottom(0.5f);
					} else if (i == 8) {
						cell.setBorderWidthBottom(2);
						cell.setBorderWidthTop(0.5f);
					} else {
						cell.setBorderWidthTop(0.5f);
						cell.setBorderWidthBottom(0.5f);
					}
					if (j == 0 || j%3 == 0) {
						cell.setBorderWidthLeft(2);
						cell.setBorderWidthRight(0.5f);
					} else if (j == 8) {
						cell.setBorderWidthRight(2);
						cell.setBorderWidthLeft(0.5f);
					} else {
						cell.setBorderWidthLeft(0.5f);
						cell.setBorderWidthRight(0.5f);
					}
					
					table.addCell(cell);
				}
			}
			
			document.add(table);
		    document.close();
		    System.out.println("Sudoku exported to PDF: " + name);
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
