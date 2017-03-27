package sudoku.view.swt;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import sudoku.controller.ISudokuController;
import sudoku.controller.event.SudokuEvent;
import sudoku.controller.listener.ISudokuListener;
import sudoku.model.SudokuModel;
import sudoku.view.View;

public class SWTView extends View {

	private Map<String, SWTSudoku> sudokus;
	private Shell shell;
	private TabFolder tabFolder;

	public SWTView(ISudokuController controller) {
		super(controller);
	}

	@Override
	public void open() {

		sudokus = new LinkedHashMap<>();

		Display.getCurrent().syncExec(() -> {
			shell = new Shell(Display.getCurrent(), SWTConstants.SHELL_OPTIONS);
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

		addSudokuTabs();
		addManagementTab();

		Display.getCurrent().syncExec(() -> {

			shell.pack();
			shell.open();

			while (!shell.isDisposed()) {
				if (!Display.getCurrent().readAndDispatch()) {
					Display.getCurrent().sleep();
				}
			}
			Display.getCurrent().dispose();
			close();
		});
	}

	private void addSudokuTabs() {
		Display.getCurrent().syncExec(() -> {
			Set<String> sudokuNames = controller.getSudokuNames();
			for (String sudokuName : sudokuNames) {

				Composite tabComposite = SWTHelper.INSTANCE.getTabComposite(tabFolder);

				TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
				tabItem.setText(sudokuName);
				tabItem.setControl(tabComposite);

				SudokuModel sudokuModel = controller.getSudoku(sudokuName);
				SWTSudoku swtSudoku = new SWTSudoku(this, sudokuModel, tabComposite);
				sudokus.put(sudokuName, swtSudoku);
			}
		});

		controller.addListener(new ISudokuListener() {

			@Override
			public void handleEvent(SudokuEvent event) {
				sudokus.get(event.getSudoku().getName()).handleSudokuEvent(event);
			}
		});
	}

	private void addManagementTab() {
		Display.getCurrent().syncExec(() -> {
			Composite tabComposite = SWTHelper.INSTANCE.getTabComposite(tabFolder);

			TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
			tabItem.setText("Management");
			tabItem.setControl(tabComposite);

			Label title = new Label(tabComposite, SWT.LEFT);
			title.setText("Manage your sudokus");
			title.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
			FormData titleData = new FormData();
			titleData.left = new FormAttachment();
			titleData.top = new FormAttachment();
			title.setLayoutData(titleData);

			Button loadSudokuBtn = SWTHelper.INSTANCE.createButton(tabComposite, title, "Load Sudoku", true);

			Label someLabel = new Label(tabComposite, SWT.LEFT);
			someLabel.setText("Hello World");
			someLabel.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
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

	Shell getShell() {
		return shell;
	}
}
