package sudoku_new;

import sudoku_new.exceptions.IllegalGridException;

import java.util.Arrays;

/**
 * Created by henne on 16.10.16.
 */
public class Sudoku {

    private String name;
    private int[] grid;
    private boolean[] editableFields;

    public Sudoku(String name, int[] grid) throws IllegalGridException {
        this.name = name;
        if (grid.length != 81) {
            throw new IllegalGridException();
        }
        this.grid = Arrays.copyOf(grid, grid.length);
        editableFields = new boolean[this.grid.length];
        for (int i = 0; i < grid.length; i++) {
            editableFields[i] = grid[i] == 0;
        }
    }

    public String getName() {
        return name;
    }

    public int[] getGridCopy() {
        return Arrays.copyOf(grid, grid.length);
    }

    public int getNumber(int row, int col) {
        return grid[row * 9 + col];
    }

    public boolean setNumber(int row, int col, int num) {
        if (editableFields[row * 9 + col]) {
            grid[row * 9 + col] = num;
            return true;
        }
        return false;
    }

    public boolean isFieldEditable(int row, int col) {
        return editableFields[row * 9 + col];
    }
}
