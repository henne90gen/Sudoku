package sudoku.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import sudoku.SudokuController;
import sudoku.SudokuModel;
import sudoku.solver.SolverType;
import sudoku.view.event.SudokuEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by henne on 16.10.16.
 */
public class SWTView extends View {

    private static final int SHELL_OPTIONS = SWT.SHELL_TRIM;

    private static final int DEFAULT_MARGIN = 10;

    private static final String SOLVE_BUTTON_TEXT = "Solve Sudoku";
    private static final String RESET_BUTTON_TEXT = "Reset Sudoku";
    private static final String SHELL_TITLE = "Sudoku SWTView";

    private Display display;

    private Shell shell;

    private TabFolder tabFolder;

    private Combo solverSelector;

    private Map<String, Label> messageLabels;

    private Map<String, Text[]> grids;
    private Button solveBtn;
    private Button resetBtn;

    public SWTView(SudokuController controller) {
        super(controller);

        messageLabels = new LinkedHashMap<>();
        grids = new LinkedHashMap<>();

        display = new Display();
        display.syncExec(() -> {
            shell = new Shell(display, SHELL_OPTIONS);
            shell.setLayout(new FormLayout());
            shell.setText(SHELL_TITLE);

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

//        controller.updateAllSudokus();

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
                SudokuModel sudokuModel = controller.getSudoku(sudokuName);

                Composite tabComposite = new Composite(tabFolder, SWT.BORDER);
                tabComposite.setLayout(new FormLayout());
                FormData compositeData = new FormData();
                compositeData.top = new FormAttachment(0, DEFAULT_MARGIN);
                compositeData.left = new FormAttachment(0, DEFAULT_MARGIN);
                compositeData.right = new FormAttachment(100, DEFAULT_MARGIN);
                compositeData.bottom = new FormAttachment(100, DEFAULT_MARGIN);
                tabComposite.setLayoutData(compositeData);

                TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
                tabItem.setText(sudokuName);
                tabItem.setControl(tabComposite);

                // create message label
                Label messageLabel = new Label(tabComposite, SWT.LEFT | SWT.WRAP);
                messageLabel.setText(sudokuName);
                messageLabel.setForeground(new Color(display, 0, 0, 0));
                FormData messageData = new FormData();
                messageData.top = new FormAttachment();
                messageData.left = new FormAttachment();

                messageLabels.put(sudokuName, messageLabel);

                solverSelector = new Combo(tabComposite, SWT.DROP_DOWN | SWT.BORDER);
                solverSelector.addListener(SWT.Selection, event -> {
                    SolverType solverType = getSelectedSolver();
                    enableGrid(sudokuName, solverType);
                });
                for (SolverType type : SolverType.values()) {
                    solverSelector.add(type.toString());
                }
                solverSelector.select(0);

                FormData solverData = new FormData();
                solverData.top = new FormAttachment(messageLabel, DEFAULT_MARGIN);
                solverData.left = new FormAttachment();
                solverSelector.setLayoutData(solverData);

                // create grid of text components
                Text[] grid = new Text[81];
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        Text cell = new Text(tabComposite, SWT.CENTER);
                        cell.setData(sudokuModel.isFieldEditable(row, col));
                        cell.setEnabled(false);
                        int number = sudokuModel.getNumber((SolverType) solverSelector.getData(), row,col);
                        if (number > 0) {
                            cell.setText("" + number);
                        }

                        FormData numberData = new FormData();
                        if (row == 0) {
                            numberData.top = new FormAttachment(solverSelector, DEFAULT_MARGIN);
                        } else {
                            numberData.top = new FormAttachment(grid[(row - 1) * 9 + col]);
                        }
                        if (col == 0) {
                            numberData.left = new FormAttachment();
                        } else {
                            numberData.left = new FormAttachment(grid[row * 9 + (col - 1)]);
                        }
                        numberData.width = 25;
                        numberData.height = 25;
                        cell.setLayoutData(numberData);

                        grid[row * 9 + col] = cell;
                    }
                }
                grids.put(sudokuName, grid);

                messageData.right = new FormAttachment(grid[80], 0, SWT.RIGHT);
                messageLabel.setLayoutData(messageData);

                // TODO maybe place buttons next to each other
                // create solve button
                solveBtn = new Button(tabComposite, SWT.PUSH);
                solveBtn.setText(SOLVE_BUTTON_TEXT);
                solveBtn.setData(sudokuName);
                FormData solveData = new FormData();
                solveData.left = new FormAttachment();
                solveData.right = new FormAttachment(grid[80], 0, SWT.RIGHT);
                solveData.top = new FormAttachment(grid[80], DEFAULT_MARGIN);
                solveBtn.setLayoutData(solveData);

                // create reset button
                resetBtn = new Button(tabComposite, SWT.PUSH);
                resetBtn.setText(RESET_BUTTON_TEXT);
                resetBtn.setData(sudokuName);
                FormData resetData = new FormData();
                resetData.left = new FormAttachment();
                resetData.top = new FormAttachment(solveBtn, DEFAULT_MARGIN);
                resetData.right = new FormAttachment(solveBtn, 0, SWT.RIGHT);
                resetBtn.setLayoutData(resetData);

                addListeners(solveBtn, resetBtn);
            }
        });
    }

    private void enableGrid(String sudokuName, SolverType solverType) {
        switch (solverType) {
            case BruteForceSolver:
            case SmartSolver:
                for (Text cell : grids.get(sudokuName)) {
                    cell.setEnabled(false);
                }
                solveBtn.setEnabled(true);
                break;
            case CustomSolver:
                for (Text cell : grids.get(sudokuName)) {
                    if ((boolean) cell.getData()) {
                        cell.setEnabled(true);
                    } else {
                        cell.setEnabled(false);
                    }
                }
                solveBtn.setEnabled(false);
                break;
        }
    }

    private SolverType getSelectedSolver() {
        return SolverType.valueOf(solverSelector.getText());
    }

    private void addListeners(Button solveBtn, Button resetBtn) {
        solveBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.getSudoku((String) solveBtn.getData()).solveUsingSolver(getSelectedSolver());
//                solve((String) solveBtn.getData());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        resetBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
//                controller.resetSolver((String) resetBtn.getData());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
    }

    @Override
    public void handleSudokuEvent(SudokuEvent event) {
        // TODO handle event
    }

//    @Override
//    public void postMessage(String identifier, int importance, String message) {
//        Label label = messageLabels.get(identifier);
//        if (label != null) label.setText(message);
//    }
//
//    @Override
//    public void updateSudoku(String identifier) {
//        Label[] grid = grids.get(identifier);
//
//        if (grid == null) return;
//
//        for (int row = 0; row < 9; row++) {
//            for (int col = 0; col < 9; col++) {
//                grid[row * 9 + col].setText(Integer.toString(sudoku.getNumber(identifier, row, col)));
//            }
//        }
//    }
}
