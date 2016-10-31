package sudoku.view.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import sudoku.model.SudokuModel;
import sudoku.solver.SolverType;

/**
 * Created by henne on 23.10.16.
 */
class SWTHelper {

    public static final SWTHelper INSTANCE = new SWTHelper();

    private SWTHelper() {
    }

    Composite getTabComposite(TabFolder tabFolder) {
        Composite tabComposite = new Composite(tabFolder, SWT.BORDER);
        tabComposite.setLayout(new FormLayout());
        FormData compositeData = new FormData();
        compositeData.top = new FormAttachment(0, SWTConstants.DEFAULT_MARGIN);
        compositeData.left = new FormAttachment(0, SWTConstants.DEFAULT_MARGIN);
        compositeData.right = new FormAttachment(100, SWTConstants.DEFAULT_MARGIN);
        compositeData.bottom = new FormAttachment(100, SWTConstants.DEFAULT_MARGIN);
        tabComposite.setLayoutData(compositeData);
        return tabComposite;
    }

    Label getMessageLabel(Display display, Composite tabComposite) {
        Label messageLabel = new Label(tabComposite, SWT.LEFT | SWT.WRAP);
        messageLabel.setForeground(new Color(display, 0, 0, 0));
        FormData messageData = new FormData();
        messageData.top = new FormAttachment();
        messageData.left = new FormAttachment();
        messageLabel.setLayoutData(messageData);
        return messageLabel;
    }

    Combo getSolverSelector(Composite tabComposite, Label messageLabel, Listener listener) {
        Combo solverSelector = new Combo(tabComposite, SWT.DROP_DOWN | SWT.BORDER);
        solverSelector.addListener(SWT.Selection, listener);
        for (SolverType type : SolverType.values()) {
            solverSelector.add(type.toString());
        }
        solverSelector.select(0);

        FormData solverData = new FormData();
        solverData.top = new FormAttachment(messageLabel, SWTConstants.DEFAULT_MARGIN);
        solverData.left = new FormAttachment();
        solverSelector.setLayoutData(solverData);
        return solverSelector;
    }

    Text getCell(SudokuModel sudokuModel, SolverType solverType, Display display, Composite tabComposite,
                 Text[] grid, int row, int col) {
        Text cell = new Text(tabComposite, SWT.CENTER);
        cell.setData(sudokuModel.isFieldEditable(row, col));
        cell.setEnabled(false);
        cell.setBackground(new Color(display, 0, 0, 0));
        int number = sudokuModel.getNumber(solverType, row, col);
        if (number > 0) {
            cell.setText("" + number);
        }

        FormData numberData = new FormData();
        if (row > 0) numberData.top = new FormAttachment(grid[(row - 1) * 9 + col]);
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

    Button createSolveButton(Composite tabComposite, Text[] grid) {
        Button solveBtn = new Button(tabComposite, SWT.PUSH);
        solveBtn.setText(SWTConstants.SOLVE_BUTTON_TEXT);
        FormData solveData = new FormData();
        solveData.left = new FormAttachment();
        solveData.right = new FormAttachment(grid[80], 0, SWT.RIGHT);
        solveData.top = new FormAttachment(grid[80], SWTConstants.DEFAULT_MARGIN);
        solveBtn.setLayoutData(solveData);
        return solveBtn;
    }

    Button createResetButton(Composite tabComposite, Button solveBtn) {
        Button resetBtn = new Button(tabComposite, SWT.PUSH);
        resetBtn.setText(SWTConstants.RESET_BUTTON_TEXT);
        resetBtn.setEnabled(false);
        FormData resetData = new FormData();
        resetData.left = new FormAttachment();
        resetData.top = new FormAttachment(solveBtn, SWTConstants.DEFAULT_MARGIN);
        resetData.right = new FormAttachment(solveBtn, 0, SWT.RIGHT);
        resetBtn.setLayoutData(resetData);
        return resetBtn;
    }

    void createAddSudokuTab() {
    }
}
