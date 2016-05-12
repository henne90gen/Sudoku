package sudoku.solver;

import java.util.ArrayList;
import java.util.Arrays;

import sudoku.Sudoku;
import sudoku.SudokuWindow;
import sudoku.Sudoku.FeedbackMode;

public class SmartSolver extends SudokuSolver {
	
	public SmartSolver(Sudoku sudoku) {
		super(sudoku);
	}
	
	public SmartSolver(Sudoku sudoku, SudokuWindow window) {
		super(sudoku, window);
	}
	
	public boolean solve() {
		runSolve();
		return checkWholeGrid(true);
	}
	
	public boolean runSolve() {
		do {
			scanPossibilities(0, 0);
		} while (placeSingleNumber(0, 0));
		
		if (!checkGridValidity()) {
			System.out.println("What?");
		}
		
		nextPossibleCell(0, 0);
		
		return checkWholeGrid(false);
	}
	
	private boolean nextPossibleCell(int col, int row) {
		scanPossibilities(0, 0);
		if (sudoku.getNumber(col, row) == 0) {
			if (possibilityGrid[col][row].size() == 0) {
				scanPossibilities(0, 0);
				if (possibilityGrid[col][row].size() == 0) {
					return false;
				}
			}
			if (nextPossibleNumber(col, row, 0)) {
				if (col < 8) {
					col++;
				} else if (row < 8) {
					col = 0;
					row++;
				} else {
					return true;
				}
				if (!nextPossibleCell(col, row)) {
					if (col > 0) {
						col--;
					} else if (row > 0) {
						col = 8;
						row--;
					}
					return nextPossibleCell(col, row);
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			if (col < 8) {
				col++;
			} else if (row < 8) {
				col = 0;
				row++;
			} else {
				return true;
			}
			return nextPossibleCell(col, row);
		}
	}
	
	private boolean nextPossibleNumber(int col, int row, int index) {
		scanPossibilities(0, 0);
		if (possibilityGrid[col][row] != null && possibilityGrid[col][row].size() > index) {
			sudoku.setNumber(col, row, possibilityGrid[col][row].get(index), true);
			scanPossibilities(0, 0);
		} else {
			sudoku.setNumber(col, row, 0, false);
			scanPossibilities(0, 0);
			return false;
		}
		int[][] tmpGrid = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tmpGrid[i][j] = sudoku.getNumber(i, j);
			}
		}
		if (!runSolve()) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					sudoku.setNumber(i, j, tmpGrid[i][j], (sudoku.getNumber(i, j) != tmpGrid[i][j]));
				}
			}
			sudoku.setNumber(col, row, 0, false);
			scanPossibilities(0, 0);
		} else {
			return true;
		}
		return nextPossibleNumber(col, row, ++index);
	}
	
	private boolean placeSingleNumber(int col, int row) {
		if (possibilityGrid[col][row] != null) {
			for (int j = 0; j < possibilityGrid[col][row].size(); j++) {
				if (	checkNumberInColumn(col, row, possibilityGrid[col][row].get(j)) ||
						checkNumberInRow(col, row, possibilityGrid[col][row].get(j)) ||
						checkNumberInBlock(col, row, possibilityGrid[col][row].get(j))) {
					sudoku.setNumber(col, row, possibilityGrid[col][row].get(j), true);
					return true;
				}
			}
		}
		if (col < 8) {
			col++;
		} else if (row < 8) {
			col = 0;
			row++;
		} else {
			return false;
		}
		return placeSingleNumber(col, row);
	}
	
	private boolean checkNumberInRow(int col, int row, int number) {
		for (int i = 0; i < 9; i++) {
			if (possibilityGrid[i][row] != null) {
				for (int j = 0; j < possibilityGrid[i][row].size(); j++) {
					if (possibilityGrid[i][row].get(j) == number && col != i) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean checkNumberInColumn(int col, int row, int number) {
		for (int i = 0; i < 9; i++) {
			if (possibilityGrid[col][i] != null) {
				for (int j = 0; j < possibilityGrid[col][i].size(); j++) {
					if (possibilityGrid[col][i].get(j) == number && row != i) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean checkNumberInBlock(int col, int row, int number) {
		int realCol = col;
		int realRow = row;
		col = (int)(col / 3);
		row = (int)(row / 3);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (possibilityGrid[i + col * 3][j + row * 3] != null) {
					for (int k = 0; k < possibilityGrid[i + col * 3][j + row * 3].size(); k++) {
						if (possibilityGrid[i + col * 3][j + row * 3].get(k) == number) {
							if (realCol != i + col * 3 && realRow != j + row * 3) {
								return false;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private void scanPossibilities(int col, int row) {
		if (col == 0 && row == 0) {
			possibilityGrid = new ArrayList[9][9];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					possibilityGrid[i][j] = new ArrayList();
				}
			}
		}
		if (sudoku.getNumber(col, row) == 0) {
			possibilityGrid[col][row] = new ArrayList();
			for (int k = 1; k < 10; k++) {
				sudoku.setNumber(col, row, k, false);
				if (checkRow(row) && checkColumn(col) && checkBlock(col, row)) {
					possibilityGrid[col][row].add(k);
				}
				sudoku.setNumber(col, row, 0, false);
			}
			if (possibilityGrid[col][row].size() == 1) {
				sudoku.setNumber(col, row, possibilityGrid[col][row].get(0), true);
				possibilityGrid[col][row] = null;
				scanPossibilities(0, 0);
			}
		} else {
			possibilityGrid[col][row] = null;
		}
		if (col < 8) {
			col++;
		} else if (row < 8) {
			col = 0;
			row++;
		} else {
			return;
		}
		scanPossibilities(col, row);
	}
}
