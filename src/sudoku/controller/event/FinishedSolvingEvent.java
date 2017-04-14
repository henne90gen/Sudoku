package sudoku.controller.event;

import sudoku.controller.solver.SolverType;

public class FinishedSolvingEvent extends SudokuEvent {

	public FinishedSolvingEvent(SolverType solverType, float time, int operations) {
		super(SudokuEventType.FinishedSolving);
		this.time = time;
		this.operations = operations;
		this.solverType = solverType;
	}
}
