package sudoku.view.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
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
    private final Map<String, SWTSudoku> sudokus;
    private Shell shell;
    private TabFolder tabFolder;

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
        addManagementTab();


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
                SWTSudoku swtSudoku = new SWTSudoku(this, sudokuModel, tabComposite);
                sudokus.put(sudokuName, swtSudoku);
            }

            SWTHelper.INSTANCE.createAddSudokuTab();
        });
    }

    private void addManagementTab() {
        display.syncExec(() -> {
            Composite tabComposite = SWTHelper.INSTANCE.getTabComposite(tabFolder);

            TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
            tabItem.setText("Management");
            tabItem.setControl(tabComposite);

            Label title = new Label(tabComposite, SWT.LEFT);
            title.setText("Manage your sudokus");
            title.setForeground(new Color(display, 0, 0, 0));
            FormData titleData = new FormData();
            titleData.left = new FormAttachment();
            titleData.top = new FormAttachment();
            title.setLayoutData(titleData);

            Button loadSudokuBtn = SWTHelper.INSTANCE.createButton(tabComposite, title, "Load Sudoku", true);

            Label someLabel = new Label(tabComposite, SWT.LEFT);
            someLabel.setText("Hello World");
            someLabel.setForeground(new Color(display, 0, 0, 0));
            FormData labelData = new FormData();
            labelData.top = new FormAttachment(loadSudokuBtn, SWTConstants.DEFAULT_MARGIN);
            labelData.left = new FormAttachment();
            labelData.right = new FormAttachment(100);
            someLabel.setLayoutData(labelData);

            loadSudokuBtn.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    FileDialog loadSudokuDialog = new FileDialog(shell, SWT.OPEN);
                    String fileName = loadSudokuDialog.open();
                    if (fileName != null) {
                        someLabel.setText(fileName);
                    }
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {

                }
            });

        });
    }

    private void resetGrid(String sudokuName) {
        sudokus.get(sudokuName).reset();
    }

    @Override
    public void handleSudokuEvent(SudokuEvent event) {
        sudokus.get(event.getSudoku().getName()).handleSudokuEvent(event);
    }

    Display getDisplay() {
        return display;
    }

    Shell getShell() {
        return shell;
    }
}
