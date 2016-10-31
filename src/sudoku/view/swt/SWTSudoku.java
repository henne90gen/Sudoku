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

/**
 * Created by henne on 31.10.16.
 */
class SWTSudoku {

    private final SudokuModel sudoku;

    private final Display display;

    private final Text[] grid;

    private final Label messageLabel;

    private final Combo solverSelector;

    private final Button solveBtn;

    private final Button resetBtn;

    private int highlightedRow;

    private int highlightedCol;

    SWTSudoku(SudokuModel sudoku, Display display, Composite tabComposite) {
        this.sudoku = sudoku;
        this.display = display;

        messageLabel = SWTHelper.INSTANCE.getMessageLabel(display, tabComposite);

        Listener solverSelectorListener = event -> {
            sudoku.resetSolver(getSelectedSolver());
            resetGrid();
            messageLabel.setText("");
        };
        solverSelector = SWTHelper.INSTANCE.getSolverSelector(tabComposite, messageLabel,
                solverSelectorListener);

        // create grid of text components
        grid = new Text[81];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Text cell = SWTHelper.INSTANCE.getCell(sudoku, getSelectedSolver(), display,
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
        solveBtn = SWTHelper.INSTANCE.createSolveButton(tabComposite, grid);

        resetBtn = SWTHelper.INSTANCE.createResetButton(tabComposite, solveBtn);

        addListeners(solveBtn, resetBtn);
    }

    private void addListeners(Button solveBtn, Button resetBtn) {
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
                sudoku.resetSolver(getSelectedSolver());
                resetGrid();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
    }

    void resetGrid() {
        solveBtn.setEnabled(true);
        resetBtn.setEnabled(false);

        highlightedRow = 0;
        highlightedCol = 0;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row * 9 + col].setBackground(new Color(display, 0, 0, 0));
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
        display.syncExec(() -> grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(display, 0, 0, 0)));
    }

    void handleSudokuEvent(SudokuEvent event) {
        display.syncExec(() -> {
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
                case PostMessage:
                    messageLabel.setText(event.getMessage());
                    break;
                case SetNumber:
                    resetHighlightedCell();
                    highlightedRow = event.getPosition().getRow();
                    highlightedCol = event.getPosition().getCol();
                    grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(display, 0, 255, 0));
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
