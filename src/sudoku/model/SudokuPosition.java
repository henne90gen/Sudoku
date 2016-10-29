package sudoku.model;

/**
 * Created by henne on 27.10.16.
 */
public class SudokuPosition {

    private int row;

    private int col;

    public SudokuPosition(int row, int col) {
        this.row = row;
        this.col = col;
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

    /**
     * Moves the position to the right, will move a row down if the end of the current row is reached.
     * @return True if the operation was successful, false in case the right edge in the bottom row is reached.
     */
    public boolean moveRight() {
        if (col < 8) {
            col++;
            return true;
        } else if (row < 8) {
            col = 0;
            row++;
            return true;
        }
        return false;
    }

    /**
     * Moves the position to the left, will move a row up if the beginning of the current row is reached.
     * @return True if the operation was successful, false in case the left edge in the top row is reached.
     */
    public boolean moveLeft() {
        if (col > 0) {
            col--;
        } else if (row > 0) {
            col = 8;
            row--;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Moves the position up, will move a column to the left if the top of the current column is reached.
     * @return True if the operation was successful, false in case the top left position is is reached.
     */
    public boolean moveUp() {
        // FIXME implement this
        return false;
    }

    /**
     * Moves the position down, will move a column to the right if the end of the current column is reached.
     * @return True if the operation was successful, false in case the bottom right position is reached.
     */
    public boolean moveDown() {
        if (row < 8) {
            row++;
        } else if (col < 8) {
            row = 0;
            col++;
        } else {
            return false;
        }
        return true;
    }
}
