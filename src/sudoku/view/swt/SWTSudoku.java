package sudoku.view.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.*;
import sudoku.model.SudokuModel;
import sudoku.solver.SolverType;
import sudoku.view.event.SudokuEvent;

import java.io.File;

/**
 * Created by henne on 31.10.16.
 */
class SWTSudoku {

    private final SudokuModel sudoku;

    private final SWTView view;

    private final Text[] grid;

    private final Label messageLabel;

    private final Label timeLabel;

    private final Label operationsLabel;

    private final Combo solverSelector;

    private final Button solveBtn;

    private final Button resetBtn;

    private final Button printBtn;

    private int highlightedRow;

    private int highlightedCol;

    SWTSudoku(SWTView view, SudokuModel sudoku, Composite tabComposite) {
        this.sudoku = sudoku;
        this.view = view;

        messageLabel = SWTHelper.INSTANCE.getLabel(view.getDisplay(), tabComposite, null, "");

        timeLabel = SWTHelper.INSTANCE.getLabel(view.getDisplay(), tabComposite, messageLabel, SWTConstants
                .TIME_LABEL_TEXT);

        operationsLabel = SWTHelper.INSTANCE.getLabel(view.getDisplay(), tabComposite, timeLabel, SWTConstants
                .OPERATIONS_LABEL_TEXT);

        Listener solverSelectorListener = event -> reset();
        solverSelector = SWTHelper.INSTANCE.getSolverSelector(tabComposite, operationsLabel,
                solverSelectorListener);

        // create grid of text components
        grid = new Text[81];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Text cell = SWTHelper.INSTANCE.getCell(sudoku, getSelectedSolver(), view.getDisplay(),
                        tabComposite, grid, row, col);
                if (row == 0) {
                    FormData cellData = (FormData) cell.getLayoutData();
                    cellData.top = new FormAttachment(solverSelector, SWTConstants.DEFAULT_MARGIN);
                }
                grid[row * 9 + col] = cell;
            }
        }

        // Attach message label to the right edge of the grid
        ((FormData) messageLabel.getLayoutData()).right = new FormAttachment(grid[8], 0, SWT.RIGHT);

        // TODO maybe place buttons next to each other
        solveBtn = SWTHelper.INSTANCE.createButton(tabComposite, grid[80], SWTConstants.SOLVE_BUTTON_TEXT, true);

        resetBtn = SWTHelper.INSTANCE.createButton(tabComposite, solveBtn, SWTConstants.RESET_BUTTON_TEXT, false);

        printBtn = SWTHelper.INSTANCE.createButton(tabComposite, resetBtn, SWTConstants.PRINT_BUTTON_TEXT, true);

        addListeners(solveBtn, resetBtn, printBtn);
    }

    private void addListeners(Button solveBtn, Button resetBtn, Button printBtn) {
        solveBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                sudoku.solveUsingSolver(getSelectedSolver());
                solveBtn.setEnabled(false);
                solverSelector.setEnabled(false);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        resetBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                reset();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        printBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog fileDialog = new FileDialog(view.getShell(), SWT.SAVE);
                String filename = fileDialog.open();
                if (filename == null) {
                    return;
                }
                File file = new File(filename);
                sudoku.printToFile(file);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
    }

    void reset() {
        sudoku.resetSolver(getSelectedSolver());

        solveBtn.setEnabled(true);
        resetBtn.setEnabled(false);

        messageLabel.setText("");
        timeLabel.setText(SWTConstants.TIME_LABEL_TEXT);
        operationsLabel.setText(SWTConstants.OPERATIONS_LABEL_TEXT);

        highlightedRow = 0;
        highlightedCol = 0;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row * 9 + col].setBackground(new Color(view.getDisplay(), 0, 0, 0));
                int number = sudoku.getNumber(getSelectedSolver(), row, col);
                if (number > 0) {
                    grid[row * 9 + col].setText("" + number);
                } else {
                    grid[row * 9 + col].setText("");
                }
            }
        }
    }

    private SolverType getSelectedSolver() {
        return SolverType.valueOf(solverSelector.getText());
    }

    private void resetHighlightedCell() {
        view.getDisplay().syncExec(() -> grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(view
                .getDisplay(), 0,
                0, 0)));
    }

    void handleSudokuEvent(SudokuEvent event) {
        view.getDisplay().syncExec(() -> {
            switch (event.getType()) {
                case FinishedSolving:
                    resetBtn.setEnabled(true);
                    solverSelector.setEnabled(true);
                    resetHighlightedCell();
                    for (int row = 0; row < 9; row++) {
                        for (int col = 0; col < 9; col++) {
                            grid[row * 9 + col].setText("" + sudoku.getNumber(getSelectedSolver(), row, col));
                        }
                    }
                    timeLabel.setText(SWTConstants.TIME_LABEL_TEXT + event.getTime());
                    operationsLabel.setText(SWTConstants.OPERATIONS_LABEL_TEXT + event.getOperations());
                case PostMessage:
                    messageLabel.setText(event.getMessage());
                    break;
                case SetNumber:
                    resetHighlightedCell();
                    highlightedRow = event.getPosition().getRow();
                    highlightedCol = event.getPosition().getCol();
                    grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(view.getDisplay(), 0, 255, 0));
                    if (event.getNewNumber() != 0) {
                        grid[highlightedRow * 9 + highlightedCol].setText("" + event.getNewNumber());
                    } else {
                        grid[highlightedRow * 9 + highlightedCol].setText("");
                    }
                    break;
            }
        });
    }
}