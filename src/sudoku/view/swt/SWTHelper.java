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

    Label getLabel(Display display, Composite tabComposite, Control centerUnder, String text) {
        Label label = new Label(tabComposite, SWT.LEFT | SWT.WRAP);
        label.setForeground(new Color(display, 0, 0, 0));
        label.setText(text);
        FormData labelData = new FormData();
        if (centerUnder == null) {
            labelData.top = new FormAttachment();
        } else {
            labelData.top = new FormAttachment(centerUnder, SWTConstants.DEFAULT_MARGIN);
            labelData.right = new FormAttachment(centerUnder, 0, SWT.RIGHT);
        }
        labelData.left = new FormAttachment();
        label.setLayoutData(labelData);
        return label;
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

    Button createButton(Composite tabComposite, Control centerUnder, String text, boolean enabled) {
        Button button = new Button(tabComposite, SWT.PUSH);
        button.setText(text);
        button.setEnabled(enabled);
        FormData buttonData = new FormData();
        buttonData.left = new FormAttachment();
        buttonData.top = new FormAttachment(centerUnder, SWTConstants.DEFAULT_MARGIN);
        buttonData.right = new FormAttachment(centerUnder, 0, SWT.RIGHT);
        button.setLayoutData(buttonData);
        return button;
    }

    void createAddSudokuTab() {
    }
}
