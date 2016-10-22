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

    private int highlightedRow;

    private int highlightedCol;

    public SWTView(SudokuController controller) {
        super(controller);

        highlightedRow = 0;
        highlightedCol = 0;

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

                Composite tabComposite = getTabComposite();

                TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
                tabItem.setText(sudokuName);
                tabItem.setControl(tabComposite);

                Label messageLabel = getMessageLabel(sudokuName, tabComposite);
                messageLabels.put(sudokuName, messageLabel);

                solverSelector = getSolverSelector(sudokuName, tabComposite, messageLabel);

                // create grid of text components
                Text[] grid = new Text[81];
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        Text cell = getCell(sudokuModel, tabComposite, grid, row, col);
                        grid[row * 9 + col] = cell;
                    }
                }
                grids.put(sudokuName, grid);

                // Attach message label to the right edge of the grid
                ((FormData) messageLabel.getLayoutData()).right = new FormAttachment(grid[8], 0, SWT.RIGHT);

                // TODO maybe place buttons next to each other
                createSolveButton(sudokuName, tabComposite, grid);

                createResetButton(sudokuName, tabComposite);

                addListeners(solveBtn, resetBtn);
            }
        });
    }

    private void createSolveButton(String sudokuName, Composite tabComposite, Text[] grid) {
        solveBtn = new Button(tabComposite, SWT.PUSH);
        solveBtn.setText(SOLVE_BUTTON_TEXT);
        solveBtn.setData(sudokuName);
        FormData solveData = new FormData();
        solveData.left = new FormAttachment();
        solveData.right = new FormAttachment(grid[80], 0, SWT.RIGHT);
        solveData.top = new FormAttachment(grid[80], DEFAULT_MARGIN);
        solveBtn.setLayoutData(solveData);
    }

    private void createResetButton(String sudokuName, Composite tabComposite) {
        resetBtn = new Button(tabComposite, SWT.PUSH);
        resetBtn.setText(RESET_BUTTON_TEXT);
        resetBtn.setData(sudokuName);
        resetBtn.setEnabled(false);
        FormData resetData = new FormData();
        resetData.left = new FormAttachment();
        resetData.top = new FormAttachment(solveBtn, DEFAULT_MARGIN);
        resetData.right = new FormAttachment(solveBtn, 0, SWT.RIGHT);
        resetBtn.setLayoutData(resetData);
    }

    private Combo getSolverSelector(String sudokuName, Composite tabComposite, Label messageLabel) {
        Combo solverSelector = new Combo(tabComposite, SWT.DROP_DOWN | SWT.BORDER);
        solverSelector.addListener(SWT.Selection, event -> {
            SolverType solverType = getSelectedSolver();

            boolean custom = false;
            if (solverType == SolverType.CustomSolver) {
                custom = true;
            }
            setGridEnabled(sudokuName, custom);
            controller.getSudoku(sudokuName).resetSolver(solverType);
            resetGrid(sudokuName, solverType);
            messageLabel.setText("");
        });
        for (SolverType type : SolverType.values()) {
            solverSelector.add(type.toString());
        }
        solverSelector.select(0);

        FormData solverData = new FormData();
        solverData.top = new FormAttachment(messageLabel, DEFAULT_MARGIN);
        solverData.left = new FormAttachment();
        solverSelector.setLayoutData(solverData);
        return solverSelector;
    }

    private void setGridEnabled(String sudokuName, boolean enabled) {
        display.syncExec(() -> {
            for (Text cell : grids.get(sudokuName)) {
                if ((boolean) cell.getData() || !enabled) {
                    cell.setEnabled(enabled);
                } else {
                    cell.setEnabled(!enabled);
                }
            }
            solveBtn.setEnabled(!enabled);
            resetBtn.setEnabled(enabled);
        });
    }

    private Label getMessageLabel(String sudokuName, Composite tabComposite) {
        Label messageLabel = new Label(tabComposite, SWT.LEFT | SWT.WRAP);
        messageLabel.setText(sudokuName);
        messageLabel.setForeground(new Color(display, 0, 0, 0));
        FormData messageData = new FormData();
        messageData.top = new FormAttachment();
        messageData.left = new FormAttachment();
        messageLabel.setLayoutData(messageData);
        return messageLabel;
    }

    private Composite getTabComposite() {
        Composite tabComposite = new Composite(tabFolder, SWT.BORDER);
        tabComposite.setLayout(new FormLayout());
        FormData compositeData = new FormData();
        compositeData.top = new FormAttachment(0, DEFAULT_MARGIN);
        compositeData.left = new FormAttachment(0, DEFAULT_MARGIN);
        compositeData.right = new FormAttachment(100, DEFAULT_MARGIN);
        compositeData.bottom = new FormAttachment(100, DEFAULT_MARGIN);
        tabComposite.setLayoutData(compositeData);
        return tabComposite;
    }

    private Text getCell(SudokuModel sudokuModel, Composite tabComposite, Text[] grid, int row, int col) {
        Text cell = new Text(tabComposite, SWT.CENTER);
        cell.setData(sudokuModel.isFieldEditable(row, col));
        cell.setEnabled(false);
        cell.setBackground(new Color(display, 0, 0, 0));
        int number = sudokuModel.getNumber((SolverType) solverSelector.getData(), row, col);
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
        return cell;
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
                solveBtn.setEnabled(true);
                resetBtn.setEnabled(false);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
    }

    private void resetGrid(String sudokuName, SolverType solverType) {
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
                    resetBtn.setEnabled(true);
                case PostMessage:
                    messageLabels.get(event.getSudoku().getName()).setText(event.getMessage());
                    break;
                case SetNumber:
                    resetHighlightedColor(event.getSudoku().getName());
                    Text[] grid = grids.get(event.getSudoku().getName());
                    highlightedRow = event.getRow();
                    highlightedCol = event.getCol();
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
