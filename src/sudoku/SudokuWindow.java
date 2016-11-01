/*
package sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SudokuWindow {

	private JLabel[][] visualGrid;
	private JPanel gridPanel;
	private JFrame frame;
	private Sudoku sudoku;
	
	public SudokuWindow(Sudoku sudoku) {
		this.sudoku = sudoku;
		visualGrid = new JLabel[9][9];
	}
	
	public void highlightNumber(int col, int row, boolean highlight) {
		visualGrid[col][row].setBackground(Color.GREEN);
		visualGrid[col][row].setOpaque(highlight);
		gridPanel.invalidate();
		gridPanel.repaint();
	}
	
	public void updateVisualGrid() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (new Integer(visualGrid[i][j].getText()) != sudoku.getNumber(i, j) && visualGrid[i][j].getBackground() != Color.GREEN) {
					visualGrid[i][j].setBackground(Color.YELLOW);
					visualGrid[i][j].setOpaque(true);
				} else if (visualGrid[i][j].getBackground() != Color.GREEN) {
					visualGrid[i][j].setOpaque(false);
				}
				visualGrid[i][j].setText(new Integer(sudoku.getNumber(i, j)).toString());
			}
		}
		gridPanel.invalidate();
		gridPanel.repaint();
	}
	
	public void open() {
		frame = new JFrame(sudoku.getName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(550, 550));
		frame.setMinimumSize(new Dimension(550, 550));
		
		JPanel mainPanel = new JPanel();
		addToPanel(mainPanel);
		
		JButton solveBtn = new JButton("Solve");
		solveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				sudoku.solve(false);
			}
		});
		mainPanel.add(solveBtn);
		
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void addToPanel(JPanel panel) {
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(9, 9));
		gridPanel.setSize(new Dimension(450, 450));
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				visualGrid[i][j] = new JLabel(new Integer(sudoku.getNumber(i, j)).toString(), SwingConstants.CENTER);
				if (sudoku.getNumber(i, j) != 0) {
					visualGrid[i][j].setForeground(Color.BLUE);
				}
				visualGrid[i][j].setSize(new Dimension(50,50));
				visualGrid[i][j].setPreferredSize(new Dimension(50, 50));
				visualGrid[i][j].setMaximumSize(new Dimension(50, 50));
				visualGrid[i][j].setMinimumSize(new Dimension(50, 50));
				visualGrid[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				gridPanel.add(visualGrid[i][j]);
			}
		}
		panel.add(gridPanel);
		panel.invalidate();
		panel.repaint();
	}

	public void removeFromPanel(JPanel panel) {
		panel.remove(gridPanel);
		panel.invalidate();
		panel.repaint();
	}

	public void resetGrid() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				visualGrid[i][j].setText(new Integer(sudoku.getNumber(i, j)).toString());
				visualGrid[i][j].setBackground(new Color(Color.TRANSLUCENT));
				visualGrid[i][j].setOpaque(false);
				if (sudoku.getNumber(i, j) != 0) {
					visualGrid[i][j].setForeground(Color.BLUE);
				}
			}
		}
	}
}
*/