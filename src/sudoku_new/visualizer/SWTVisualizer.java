package sudoku_new.visualizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import sudoku_new.Sudoku;

/**
 * Created by henne on 16.10.16.
 */
public class SWTVisualizer extends Visualizer {

    private Shell shell;

    private static final int SHELL_OPTIONS = SWT.SHELL_TRIM;

    public SWTVisualizer(Sudoku sudoku) {
        super(sudoku);
    }

    public void open() {
        new Thread(() -> {
            Display display = new Display();
            shell = new Shell(display, SHELL_OPTIONS);
            shell.open();

            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            display.dispose();
        }).start();
    }

    @Override
    public void postMessage(int importance, String message) {

    }

    @Override
    public void updateSudoku() {

    }
}
