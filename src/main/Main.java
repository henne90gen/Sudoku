package main;

import sudoku.SudokuGenerator;

public class Main {
	
	public static int[][] easy = {	{0, 4, 3, 0, 0, 0, 6, 7, 0},
							{0, 0, 0, 2, 9, 3, 0, 0, 4},
							{2, 8, 0, 0, 0, 0, 3, 1, 0},
							{0, 0, 0, 6, 0, 0, 0, 0, 0},
							{1, 0, 0, 5, 0, 7, 0, 0, 8},
							{0, 0, 0, 0, 0, 4, 0, 0, 0},
							{0, 5, 6, 0, 0, 0, 0, 4, 1},
							{3, 0, 0, 4, 5, 2, 0, 0, 0},
							{0, 2, 7, 0, 0, 0, 9, 5, 0}};

	int[][] easySolution = {{9, 4, 3, 1, 8, 5, 6, 7, 2},
							{6, 7, 1, 2, 9, 3, 5, 8, 4},
							{2, 8, 5, 7, 4, 6, 3, 1, 9},
							{7, 9, 4, 6, 2, 8, 1, 3, 5},
							{1, 6, 2, 5, 3, 7, 4, 9, 8},
							{5, 3, 8, 9, 1, 4, 7, 2, 6},
							{8, 5, 6, 3, 7, 9, 2, 4, 1},
							{3, 1, 9, 4, 5, 2, 8, 6, 7},
							{4, 2, 7, 8, 6, 1, 9, 5, 3}};

	public static int[][] medium = {	{0, 8, 0, 0, 0, 4, 5, 6, 0},
								{0, 6, 0, 0, 0, 0, 0, 1, 7},
								{9, 0, 0, 0, 7, 0, 0, 8, 2},
								{0, 0, 0, 7, 0, 0, 6, 0, 0},
								{0, 0, 5, 1, 0, 6, 7, 0, 0},
								{0, 0, 7, 0, 0, 3, 0, 0, 0},
								{5, 1, 0, 0, 2, 0, 0, 0, 6},
								{3, 2, 0, 0, 0, 0, 0, 4, 0},
								{0, 7, 6, 4, 0, 0, 0, 9, 0}};
	
	public static int[][] hard2 = {{8, 0, 0, 1, 0, 0, 0, 0, 6},
							{0, 0, 2, 0, 0, 9, 0, 0, 4},
							{3, 0, 0, 0, 0, 6, 2, 5, 0},
							{0, 2, 5, 0, 0, 0, 0, 0, 0},
							{4, 0, 0, 0, 1, 0, 0, 0, 3},
							{0, 0, 0, 0, 0, 0, 1, 6, 0},
							{0, 4, 1, 3, 0, 0, 0, 0, 7},
							{5, 0, 0, 8, 0, 0, 4, 0, 0},
							{9, 0, 0, 0, 0, 4, 0, 0, 1}};
	
	public static int[][] hard = {	{5, 0, 0, 0, 0, 0, 0, 3, 6},
							{0, 0, 8, 0, 7, 0, 0, 0, 0},
							{0, 1, 4, 0, 0, 5, 0, 0, 0},
							{9, 0, 0, 6, 0, 0, 0, 0, 0},
							{4, 0, 0, 0, 8, 0, 1, 5, 0},
							{1, 0, 0, 0, 0, 2, 0, 0, 7},
							{8, 4, 0, 1, 0, 0, 9, 0, 0},
							{7, 0, 0, 0, 4, 0, 8, 0, 0},
							{2, 3, 0, 0, 0, 0, 0, 0, 0}};
	
	int[][] hardSolution = {{5, 9, 7, 8, 2, 1, 4, 3, 6},
							{3, 2, 8, 4, 7, 6, 5, 9, 1},
							{6, 1, 4, 3, 9, 5, 2, 7, 8},
							{9, 7, 5, 6, 1, 4, 3, 8, 2},
							{4, 6, 2, 7, 8, 3, 1, 5, 9},
							{1, 8, 3, 9, 5, 2, 6, 4, 7},
							{8, 4, 6, 1, 3, 7, 9, 2, 5},
							{7, 5, 1, 2, 4, 9, 8, 6, 3},
							{2, 3, 9, 5, 6, 8, 7, 1, 4}};

	public static void hello(int i) {
		System.out.println(i);
	}
	
	public static void main(String[] args) {
		System.out.println("Start");
		SudokuGenerator sg = new SudokuGenerator("Random Sudoku Generator");
		
		//Sudoku sudoku = new Sudoku("Random", SolverType.Smart, FeedbackMode.Window, SudokuGenerator.getSudoku(25, true));
		//sudoku.openWindow();
		
		/*ArrayList<int[][]> grids = Sudoku.readFromFile("unsolvable.txt", false);
		for (int i = 0; i < grids.size(); i++) {
			Sudoku sudoku = new Sudoku(grids.get(i));
			sudoku.exportAsPDF("unsolvable" + (i+1) + ".pdf");
		}*/
	}
}

