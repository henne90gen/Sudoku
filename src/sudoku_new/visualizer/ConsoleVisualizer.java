package sudoku_new.visualizer;

import sudoku_new.Sudoku;

/**
 * Created by henne on 16.10.16.
 */
public class ConsoleVisualizer extends Visualizer {

    public ConsoleVisualizer(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public void postMessage(int importance, String message) {
        if (importance > 5) {
            System.out.println(message);
        }
    }

    @Override
    public void updateSudoku() {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    System.out.print(sudoku.getNumber(i, j) + " ");
                }
                System.out.println();
            }

    }
}
