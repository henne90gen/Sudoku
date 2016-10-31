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
import sudoku.solver.SolverType;
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

    private Combo solverSelector;

    private final Map<String, Label> messageLabels;

    private final Map<String, Text[]> grids;

    private final Map<String, Button> solveBtns;

    private final Map<String, Button> resetBtns;

    private int highlightedRow;

    private int highlightedCol;

    public SWTView(SudokuController controller) {
        super(controller);

        highlightedRow = 0;
        highlightedCol = 0;

        messageLabels = new LinkedHashMap<>();
        grids = new LinkedHashMap<>();
        solveBtns = new LinkedHashMap<>();
        resetBtns = new LinkedHashMap<>();

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
                SudokuModel sudokuModel = controller.getSudoku(sudokuName);

                Composite tabComposite = SWTHelper.INSTANCE.getTabComposite(tabFolder);

                TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
                tabItem.setText(sudokuName);
                tabItem.setControl(tabComposite);
                tabItem.addListener(SWT.Selection, new Listener() {
                    @Override
                    public void handleEvent(Event event) {
                        resetGrid(sudokuName, getSelectedSolver());
                    }
                });

                Label messageLabel = SWTHelper.INSTANCE.getMessageLabel(display, tabComposite);
                messageLabels.put(sudokuName, messageLabel);

                Listener solverSelectorListener = event -> {
                    SolverType solverType = getSelectedSolver();
                    controller.getSudoku(sudokuName).resetSolver(solverType);
                    resetGrid(sudokuName, solverType);
                    messageLabel.setText("");
                };
                solverSelector = SWTHelper.INSTANCE.getSolverSelector(tabComposite, messageLabel,
                        solverSelectorListener);

                // create grid of text components
                Text[] grid = new Text[81];
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        Text cell = SWTHelper.INSTANCE.getCell(sudokuModel, getSelectedSolver(), display,
                                tabComposite, grid, row, col);
                        if (row == 0) {
                            FormData cellData = (FormData) cell.getLayoutData();
                            cellData.top = new FormAttachment(solverSelector, SWTConstants.DEFAULT_MARGIN);
                        }
                        grid[row * 9 + col] = cell;
                    }
                }
                grids.put(sudokuName, grid);

                // Attach message label to the right edge of the grid
                ((FormData) messageLabel.getLayoutData()).right = new FormAttachment(grid[8], 0, SWT.RIGHT);

                // TODO maybe place buttons next to each other
                Button solveBtn = SWTHelper.INSTANCE.createSolveButton(sudokuName, tabComposite, grid);

                Button resetBtn = SWTHelper.INSTANCE.createResetButton(sudokuName, tabComposite, solveBtn);

                addListeners(solveBtn, resetBtn);

                solveBtns.put(sudokuName, solveBtn);
                resetBtns.put(sudokuName, resetBtn);
            }

            SWTHelper.INSTANCE.createAddSudokuTab();
        });
    }

    private SolverType getSelectedSolver() {
        return SolverType.valueOf(solverSelector.getText());
    }

    private void addListeners(Button solveBtn, Button resetBtn) {
        solveBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.getSudoku((String) solveBtn.getData()).solveUsingSolver(getSelectedSolver());
                solveBtn.setEnabled(false);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        resetBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.getSudoku((String) solveBtn.getData()).resetSolver(getSelectedSolver());
                resetGrid((String) solveBtn.getData(), getSelectedSolver());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
    }

    private void resetGrid(String sudokuName, SolverType solverType) {
        solveBtns.get(sudokuName).setEnabled(true);
        resetBtns.get(sudokuName).setEnabled(false);

        SudokuModel sudoku = controller.getSudoku(sudokuName);
        Text[] grid = grids.get(sudokuName);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row * 9 + col].setBackground(new Color(display, 0, 0, 0));
                int number = sudoku.getNumber(solverType, row, col);
                if (number > 0) {
                    grid[row * 9 + col].setText("" + number);
                } else {
                    grid[row * 9 + col].setText("");
                }
                highlightedRow = 0;
                highlightedCol = 0;
            }
        }
    }

    @Override
    public void handleSudokuEvent(SudokuEvent event) {
        display.syncExec(() -> {
            switch (event.getType()) {
                case FinishedSolving:
                    resetBtns.get(event.getSudoku().getName()).setEnabled(true);
                case PostMessage:
                    messageLabels.get(event.getSudoku().getName()).setText(event.getMessage());
                    break;
                case SetNumber:
                    resetHighlightedColor(event.getSudoku().getName());
                    Text[] grid = grids.get(event.getSudoku().getName());
                    highlightedRow = event.getPosition().getRow();
                    highlightedCol = event.getPosition().getCol();
                    grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(display, 0, 255, 0));
                    grid[highlightedRow * 9 + highlightedCol].setText("" + event.getNewNumber());
                    break;
            }
        });
    }

    private void resetHighlightedColor(String sudokuName) {
        display.syncExec(() -> {
            Text[] grid = grids.get(sudokuName);
            grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(display, 0, 0, 0));
        });
    }
}
