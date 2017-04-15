package sudoku.controller;

import java.util.*;

import sudoku.controller.event.SudokuEvent;
import sudoku.controller.event.SudokuEventType;
import sudoku.controller.listener.SudokuListener;
import sudoku.controller.solver.Solver;
import sudoku.controller.solver.SolverFactory;
import sudoku.controller.solver.SolverType;
import sudoku.model.SudokuModel;
import sudoku.view.View;

public class SudokuController implements ISudokuController {

	private final List<View> views;
	private final Map<String, SudokuModel> models;
	private final Map<String, Map<SolverType, Solver>> solvers;
	private final Map<String, List<SudokuListener>> channelListeners;
	private final List<SudokuListener> listeners;

	public SudokuController() {
		views = new ArrayList<>();
		models = new LinkedHashMap<>();
		solvers = new LinkedHashMap<>();
		listeners = new ArrayList<>();
		channelListeners = new LinkedHashMap<>();
	}

	public void openAllViews() {
		views.forEach(View::open);
	}

	public boolean hasOpenViews() {
		for (View view : views) {
			if (view.isOpen()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public SudokuModel getSudoku(String name) {
		return models.get(name);
	}

	@Override
	public void addView(View view) {
		views.add(view);
	}

	@Override
	public void addModel(SudokuModel sudoku) {
		models.put(sudoku.getName(), sudoku);
		solvers.put(sudoku.getName(), SolverFactory.getAllSolvers(this));
	}

	@Override
	public Set<String> getSudokuNames() {
		return models.keySet();
	}

	@Override
	public void fireEvent(SudokuEvent event) {
		Objects.requireNonNull(event);
		listeners.stream()
				.forEach(l -> l.handleEvent(event));
	}

	@Override
	public void fireEvent(SudokuModel sudoku, SudokuEvent event) {
		Objects.requireNonNull(event);
		Objects.requireNonNull(sudoku);

		if (event.getType() == SudokuEventType.FinishedSolving) {
			solvers.get(sudoku.getName())
					.put(event.getSolverType(), SolverFactory.getSolver(this, event.getSolverType()));
		}

		List<SudokuListener> channelList = channelListeners.get(sudoku.getName());

		channelList.stream()
				.forEach(l -> l.handleEvent(event));

		fireEvent(event);
	}

	@Override
	public void addListener(SudokuListener listener) {
		Objects.requireNonNull(listener);
		listeners.add(listener);
	}

	@Override
	public void addListener(SudokuModel sudoku, SudokuListener listener) {
		Objects.requireNonNull(sudoku);
		Objects.requireNonNull(listener);
		List<SudokuListener> channelList = channelListeners.get(sudoku.getName());
		if (channelList == null) {
			channelList = new ArrayList<>();
		}
		channelList.add(listener);
		channelListeners.put(sudoku.getName(), channelList);
	}

	@Override
	public void startSolver(SudokuModel sudoku, SolverType solverType) {
		Solver solver = solvers.get(sudoku.getName())
				.get(solverType);
		if (solver == null) {
			solver = SolverFactory.getSolver(this, solverType);
			solvers.get(sudoku.getName())
					.put(solverType, solver);
		}
		solver.solve(sudoku);
	}

	public void waitForSolver(SudokuModel sudoku, SolverType solverType) {
		Solver solver = solvers.get(sudoku.getName())
				.get(solverType);
		if (solver != null) {
			solver.waitFor();
			solvers.get(sudoku.getName())
					.put(solverType, null);
		}
	}

	@Override
	public boolean isSolverRunning(SudokuModel sudoku, SolverType solverType) {
		Solver solver = solvers.get(sudoku.getName())
				.get(solverType);
		if (solver != null) {
			return solver.isSolving();
		}
		return false;
	}

	public void stopSolver(SudokuModel sudoku, SolverType solverType) {
		Solver solver = solvers.get(sudoku.getName())
				.get(solverType);
		if (solver != null) {
			solver.stop();
		}
	}
}
