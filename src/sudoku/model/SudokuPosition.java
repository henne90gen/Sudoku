package sudoku.model;

public class SudokuPosition {

	private int row;

	private int col;

	public SudokuPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public String toString() {
		return "(" + row + "|" + col + ")";
	}

	public SudokuPosition getCopy() {
		return new SudokuPosition(row, col);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean moveForward() {
		if (!moveRight()) {
			if (moveDown()) {
				col = 0;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean moveBackward() {
		if (!moveLeft()) {
			if (moveUp()) {
				col = 8;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Moves the position to the right until the end of the row is reached
	 *
	 * @return True if the operation was successful, false in case the right
	 *         edge is reached
	 */
	public boolean moveRight() {
		if (col < 8) {
			col++;
			return true;
		}
		return false;
	}

	/**
	 * Moves the position to the left until the left side of the row is reached
	 *
	 * @return True if the operation was successful, false in case the left edge
	 *         is reached
	 */
	public boolean moveLeft() {
		if (col > 0) {
			col--;
			return true;
		}
		return false;
	}

	/**
	 * Moves the position up until the top is reached
	 *
	 * @return True if the operation was successful, false in case the top is
	 *         reached.
	 */
	public boolean moveUp() {
		if (row > 0) {
			row--;
			return true;
		}
		return false;
	}

	/**
	 * Moves the position down until the bottom is reached
	 *
	 * @return True if the operation was successful, false in case the bottom is
	 *         reached.
	 */
	public boolean moveDown() {
		if (row < 8) {
			row++;
			return true;
		}
		return false;
	}
}
