package sudoku.controller.event;

import sudoku.controller.solver.SolverType;
import sudoku.model.SudokuPosition;

public abstract class SudokuEvent {

	private final SudokuEventType eventType;

	protected String message;

	protected SudokuPosition position;

	protected int newNumber;

	protected float time;

	protected int operations;

	protected SolverType solverType;

	SudokuEvent(SudokuEventType eventType) {
		this.eventType = eventType;
	}

	public SudokuEventType getType() {
		return eventType;
	}

	public String getMessage() {
		return message;
	}

	public SudokuPosition getPosition() {
		return position;
	}

	public int getNewNumber() {
		return newNumber;
	}

	public float getTime() {
		return time;
	}

	public int getOperations() {
		return operations;
	}

	public SolverType getSolverType() {
		return solverType;
	}
}
