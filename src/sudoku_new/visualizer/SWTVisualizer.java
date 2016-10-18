package sudoku_new.visualizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sudoku_new.Sudoku;

/**
 * Created by henne on 16.10.16.
 */
public class SWTVisualizer extends Visualizer {

    private static final int SHELL_OPTIONS = SWT.SHELL_TRIM;

    private Shell shell;

    private Text[] grid;

    public SWTVisualizer(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public void open() {
        new Thread(() -> {
            Display display = new Display();
            shell = new Shell(display, SHELL_OPTIONS);
            shell.setLayout(new FormLayout());

            addComponents();

            shell.open();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            display.dispose();
        }).start();
    }

    private void addComponents() {
        grid = new Text[81];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Text tmp = new Text(shell, SWT.BORDER);
                FormData tmpData = new FormData();
                if (row == 0) {
                    tmpData.top = new FormAttachment();
                } else {
                    tmpData.top = new FormAttachment(grid[(row - 1) * 9 + col]);
                }
                if (col == 0) {
                    tmpData.left = new FormAttachment();
                } else {
                    tmpData.left = new FormAttachment(grid[row * 9 + (col - 1)]);
                }
                tmpData.width = 25;
                tmpData.height = 25;
                tmp.setLayoutData(tmpData);
                grid[row * 9 + col] = tmp;
            }
        }
    }

    @Override
    public void postMessage(int importance, String message) {

    }

    @Override
    public void updateSudoku() {

    }
}
