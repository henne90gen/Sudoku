/*
package sudoku;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import sudoku.Sudoku.FeedbackMode;
import sudoku.Sudoku.SolverType;

public class SudokuGenerator implements ActionListener {
	
	private static final String SOLVE_COMMAND = "Solve";
	private static final String GENERATE_COMMAND = "Generate";
	private static final String CANCEL_COMMAND = "Cancel";
	
	private static Random rand = new Random();
	
	private JFrame frame;
	private JPanel mainPanel = new JPanel();
	private JPanel settingsPanel = new JPanel();
	private JTextField numbers = new JTextField("25");
	private JComboBox<SolverType> solverType = new JComboBox<SolverType>();
	private JButton solveBtn = new JButton();
	private JButton generateBtn = new JButton();
	private JButton cancelBtn = new JButton();
	private JLabel status = new JLabel();
	
	private	Thread thread;
	private Sudoku sudoku;
	
	private boolean generating;
	
	public SudokuGenerator(String name) {
		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(640, 500));
		constructFrame();
		frame.setVisible(true);
	}
	
	public SudokuGenerator() {}

	private void constructFrame() {
		frame.remove(mainPanel);
		
		mainPanel = new JPanel();
		sudoku = new Sudoku("Random", SolverType.Smart, FeedbackMode.Window, new int[9][9]);
		sudoku.addToPanel(mainPanel);
		
		thread = new Thread(new Runnable(){
			@Override
			public void run() {
//				sudoku = new SudokuModel("Random",
//									SolverType.BruteForce,
//									FeedbackMode.Window,
//									Main.hard);
//									new SudokuGenerator().getSudoku(new Integer(numbers.getText()), SolverType.BruteForce));
				frame.remove(mainPanel);
				mainPanel = new JPanel();
				sudoku.addToPanel(mainPanel);
				
				status.setText("Ready");
				numbers.setEnabled(true);
				solveBtn.setEnabled(true);
				generateBtn.setEnabled(true);
				cancelBtn.setEnabled(false);
				
				mainPanel.add(settingsPanel);
				frame.add(mainPanel);
				frame.pack();
			}
		});
		thread.start();
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		int index = solverType.getSelectedIndex();
		SolverType[] st = {SolverType.BruteForce, SolverType.Smart};
		solverType = new JComboBox<SolverType>(st);
		solverType.setSelectedIndex((index < 0)?0:index);
		settingsPanel.add(solverType, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		settingsPanel.add(new JLabel("Numbers:"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		String tmp = numbers.getText();
		numbers = new JTextField(tmp);
		numbers.setEnabled(false);
		settingsPanel.add(numbers, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		solveBtn = new JButton(SOLVE_COMMAND);
		solveBtn.addActionListener(this);
		solveBtn.setEnabled(false);
		solveBtn.setActionCommand(SOLVE_COMMAND);
		settingsPanel.add(solveBtn, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		generateBtn = new JButton(GENERATE_COMMAND);
		generateBtn.addActionListener(this);
		generateBtn.setEnabled(false);
		generateBtn.setActionCommand(GENERATE_COMMAND);
		settingsPanel.add(generateBtn, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		cancelBtn = new JButton(CANCEL_COMMAND);
		cancelBtn.addActionListener(this);
		cancelBtn.setEnabled(true);
		cancelBtn.setActionCommand(CANCEL_COMMAND);
		settingsPanel.add(cancelBtn, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		status = new JLabel("Please wait...");
		settingsPanel.add(status, gbc);
		
		mainPanel.add(settingsPanel);
		
		frame.add(mainPanel);
		frame.pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case SOLVE_COMMAND:
//			sudoku.setSolverType((SolverType)solverType.getSelectedItem());
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					sudoku.resetGrid();
					boolean solved = true;//sudoku.solve(true);
					numbers.setEnabled(true);
					solveBtn.setEnabled(true);
					generateBtn.setEnabled(true);
					cancelBtn.setEnabled(false);
					status.setText((solved)?"Solved":"Not solvable");
				}
			});
			thread.start();
			numbers.setEnabled(false);
			solveBtn.setEnabled(false);
			generateBtn.setEnabled(false);
			cancelBtn.setEnabled(true);
			status.setText("Solving...");
			break;
		case GENERATE_COMMAND:
			constructFrame();
			break;
		case CANCEL_COMMAND:
			thread.interrupt();
		default:
			break;
		}
	}
	
//	public int[][] getSudoku(int amountOfNumbers, SolverType solverType) {
//		generating = true;
//		int[][] grid = new int[9][9];
//		if (amountOfNumbers <= 0)
//			amountOfNumbers = rand.nextInt(6) + 25;
//		grid[0][0] = 1;
//		grid[0][1] = 1;
//		SudokuModel sudoku = new SudokuModel("", solverType, FeedbackMode.Silent, grid);
//		while (!sudoku.solve(true) && generating) {
//			grid = new int[9][9];
//			for (int i = 0; i < amountOfNumbers; i++) {
//				placeNumber(grid);
//			}
//			sudoku.setGrid(grid);
//			sudoku.setOriginalGrid(grid);
//		}
//		if (!generating)
//			grid = new int[9][9];
//		return grid;
//	}
	
	private void placeNumber(int[][] grid) {
		int col = rand.nextInt(9);
		int row = rand.nextInt(9);
		int number = rand.nextInt(9) + 1;
		if (Thread.interrupted()) {
			Thread.currentThread().interrupt();
			generating = false;
			return;
		}
		if (grid[col][row] == 0) {
			grid[col][row] = number;
			Sudoku sudoku = new Sudoku("", SolverType.BruteForce, FeedbackMode.Silent, grid);
			if (!sudoku.checkGridValidity()) {
				grid[col][row] = 0;
				placeNumber(grid);
			}
		} else {
			placeNumber(grid);
		}
	}
	
	public void generateSudokus(int amount, int difficulty, boolean solvable) {
		int solved = 0;
		long time = System.nanoTime();
		ArrayList<Float> times = new ArrayList<Float>();
		ArrayList<int[][]> unsolvable = new ArrayList<int[][]>();
		for (int i = 1; i < amount + 1; i++) {
//			SudokuModel sudoku = new SudokuModel("#" + i, SolverType.BruteForce, FeedbackMode.Console, getSudoku(difficulty, (solvable)?SolverType.Smart:SolverType.BruteForce));
//			if (sudoku.solve(true)) {
//				solved++;
//				times.add(sudoku.getSolveTime());
//				sudoku.saveToFile("solvable.txt", true);
//			} else {
//				unsolvable.add(sudoku.getOriginalGrid());
//				sudoku.saveToFile("unsolvable.txt", false);
//			}
		}
		
		System.out.println("Solved " + solved + " out of " + amount + " in " + (System.nanoTime() - time) / 1000000000.0f + " seconds.");
		
		long averageTime = 0;
		for (float t : times) {
			averageTime += t;
		}
		averageTime = averageTime / times.size();
		System.out.println("Average time is " + averageTime / 1000000000.0f + " seconds.");
	}
}
*/