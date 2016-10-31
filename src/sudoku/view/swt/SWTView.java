package sudoku.view.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import sudoku.SudokuController;
import sudoku.model.SudokuModel;
import sudoku.view.View;
import sudoku.view.event.SudokuEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by henne on 16.10.16.
 */
public class SWTView extends View {

    private final Display display;

    private Shell shell;

    private TabFolder tabFolder;

    private final Map<String, SWTSudoku> sudokus;

    public SWTView(SudokuController controller) {
        super(controller);

        sudokus = new LinkedHashMap<>();

        display = new Display();
        display.syncExec(() -> {
            shell = new Shell(display, SWTConstants.SHELL_OPTIONS);
            shell.setLayout(new FormLayout());
            shell.setText(SWTConstants.SHELL_TITLE);

            tabFolder = new TabFolder(shell, SWT.BORDER);
            tabFolder.setLayout(new FormLayout());
            FormData tabData = new FormData();
            tabData.left = new FormAttachment();
            tabData.top = new FormAttachment();
            tabData.right = new FormAttachment(100);
            tabData.bottom = new FormAttachment(100);
            tabFolder.setLayoutData(tabData);
        });
    }

    @Override
    public void open() {

        addSudokuTabs();

        display.syncExec(() -> {

            shell.pack();
            shell.open();

            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            display.dispose();
        });
    }

    private void addSudokuTabs() {
        display.syncExec(() -> {
            Set<String> sudokuNames = controller.getSudokuNames();
            for (String sudokuName : sudokuNames) {

                Composite tabComposite = SWTHelper.INSTANCE.getTabComposite(tabFolder);

                TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
                tabItem.setText(sudokuName);
                tabItem.setControl(tabComposite);
                tabItem.addListener(SWT.Selection, event -> resetGrid(sudokuName));

                SudokuModel sudokuModel = controller.getSudoku(sudokuName);
                SWTSudoku swtSudoku = new SWTSudoku(sudokuModel, display, tabComposite);
                sudokus.put(sudokuName, swtSudoku);
            }

            SWTHelper.INSTANCE.createAddSudokuTab();
        });
    }

    private void resetGrid(String sudokuName) {
        sudokus.get(sudokuName).resetGrid();
    }

    @Override
    public void handleSudokuEvent(SudokuEvent event) {
        sudokus.get(event.getSudoku().getName()).handleSudokuEvent(event);
    }
}
