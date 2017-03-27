package sudoku.view.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.*;

import sudoku.controller.event.SudokuEvent;
import sudoku.model.SudokuModel;
import sudoku.model.solver.SolverType;

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

        messageLabel = SWTHelper.INSTANCE.getLabel(Display.getCurrent(), tabComposite, null, "");

        timeLabel = SWTHelper.INSTANCE.getLabel(Display.getCurrent(), tabComposite, messageLabel, SWTConstants
                .TIME_LABEL_TEXT);

        operationsLabel = SWTHelper.INSTANCE.getLabel(Display.getCurrent(), tabComposite, timeLabel, SWTConstants
                .OPERATIONS_LABEL_TEXT);

        Listener solverSelectorListener = event -> reset();
        solverSelector = SWTHelper.INSTANCE.getSolverSelector(tabComposite, operationsLabel,
                solverSelectorListener);

        // create grid of text components
        grid = new Text[81];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Text cell = SWTHelper.INSTANCE.getCell(sudoku, getSelectedSolver(), Display.getCurrent(),
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

    void reset() {
        sudoku.resetSolver(getSelectedSolver());

        solveBtn.setText(SWTConstants.SOLVE_BUTTON_TEXT);
        solveBtn.setEnabled(true);
        resetBtn.setEnabled(false);

        messageLabel.setText("");
        timeLabel.setText(SWTConstants.TIME_LABEL_TEXT);
        operationsLabel.setText(SWTConstants.OPERATIONS_LABEL_TEXT);

        highlightedRow = 0;
        highlightedCol = 0;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row * 9 + col].setBackground(new Color(Display.getCurrent(), 0, 0, 0));
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

    private void addListeners(Button solveBtn, Button resetBtn, Button printBtn) {
        solveBtn.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SolverType selectedSolver = getSelectedSolver();
                if (sudoku.isSolverRunning(selectedSolver)) {
                    sudoku.stopSolver(selectedSolver);
                    reset();
                } else {
                    sudoku.startSolver(selectedSolver);
                    solveBtn.setText(SWTConstants.STOP_BUTTON_TEXT);
                }
                solverSelector.setEnabled(!solverSelector.isEnabled());
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

    void handleSudokuEvent(SudokuEvent event) {
        Display.getCurrent().syncExec(() -> {
            switch (event.getType()) {
                case FinishedSolving:
                    handleFinishedSolvingEvent(event);
                    break;
                case PostMessage:
                    handlePostMessageEvent(event);
                    break;
                case SetNumber:
                    handleSetNumberEvent(event);
                    break;
            }
        });
    }

    private void handleFinishedSolvingEvent(SudokuEvent event) {
        resetBtn.setEnabled(true);
        solveBtn.setEnabled(false);
        solveBtn.setText(SWTConstants.SOLVE_BUTTON_TEXT);
        solverSelector.setEnabled(true);
        resetHighlightedCell();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int currentNumber = sudoku.getNumber(getSelectedSolver(), row, col);
//                if (currentNumber != 0) {
                grid[row * 9 + col].setText("" + currentNumber);
//                } else {
//                    grid[row * 9 + col].setText("");
//                }
            }
        }
        timeLabel.setText(SWTConstants.TIME_LABEL_TEXT + event.getTime());
        operationsLabel.setText(SWTConstants.OPERATIONS_LABEL_TEXT + event.getOperations());
        handlePostMessageEvent(event);
    }

    private void handlePostMessageEvent(SudokuEvent event) {
        messageLabel.setText(event.getMessage());
    }

    private void handleSetNumberEvent(SudokuEvent event) {
        resetHighlightedCell();
        highlightedRow = event.getPosition().getRow();
        highlightedCol = event.getPosition().getCol();
        grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(Display.getCurrent(), 0, 255, 0));
        if (event.getNewNumber() != 0) {
            grid[highlightedRow * 9 + highlightedCol].setText("" + event.getNewNumber());
        } else {
            grid[highlightedRow * 9 + highlightedCol].setText("");
        }
    }

    private void resetHighlightedCell() {
        Display.getCurrent().syncExec(() -> grid[highlightedRow * 9 + highlightedCol].setBackground(new Color(Display.getCurrent(), 0, 0, 0)));
    }
}
