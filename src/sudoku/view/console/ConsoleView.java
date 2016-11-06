package sudoku.view.console;

import sudoku.ISudokuController;
import sudoku.model.SudokuModel;
import sudoku.solver.SolverType;
import sudoku.view.View;
import sudoku.view.event.SudokuEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by henne on 16.10.16.
 */
public class ConsoleView extends View {


    private static final String SOLVE_HELP = "solve [Sudoku Number] [Solver Number]";
    private static final String PRINT_SUDOKU_HELP = "print [Sudoku Number]";
    private static final String PRINT_SOLUTION_HELP = "prints [Sudoku Number] [Solver Number]";

    private BufferedReader br;

    public ConsoleView(ISudokuController controller) {
        super(controller);
    }

    @Override
    public void open() {
        new Thread(() -> {
            try {
                br = new BufferedReader(new InputStreamReader(System.in));

                boolean running = true;
                while (running) {
                    print("[ConsoleView] $ ", false);
                    String command = br.readLine();

                    running = handleCommand(command);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void handleSudokuEvent(SudokuEvent event) {
        switch (event.getType()) {
            case PostMessage:
            case FinishedSolving:
                println(event.getSudoku().getName(), event.getMessage());
                break;
            case SetNumber:
                break;
        }
    }

    private void println(String sudokuName, String line) {
        println(sudokuName + ": " + line);
    }

    private void println(String line) {
        print(line, true);
    }

    private void print(String line, boolean addLineBreak) {
        if (line == null || line.isEmpty()) {
            System.out.println();
        } else {
            if (addLineBreak) {
                System.out.println(line);
            } else {
                System.out.print(line);
            }
        }
    }

    private boolean handleCommand(String command) {
        String[] arguments = command.split(" ");
        switch (arguments[0]) {
            default:
                println("Command not recognized. Try h for help.");
                break;
            case "q":
                println("Goodbye.");
                return false;
            case "h":
                help();
                break;
            case "ls":
                listAllSudokus();
                break;
            case "print":
                try {
                    Integer sudokuNumber = Integer.parseInt(arguments[1]);
                    printSudoku(sudokuNumber);
                } catch (NumberFormatException e) {
                    println("Usage: " + PRINT_SUDOKU_HELP);
                }
                break;
            case "prints":
                try {
                    Integer sudokuNumber = Integer.parseInt(arguments[1]);
                    Integer solverNumber = Integer.parseInt(arguments[2]);
                    printSolution(sudokuNumber, solverNumber);
                } catch (NumberFormatException e) {
                    println("Usage: " + PRINT_SOLUTION_HELP);
                }
                break;
            case "solvers":
                listAllSolvers();
                break;
            case "solve":
                try {
                    Integer sudokuNumber = Integer.parseInt(arguments[1]);
                    Integer solverNumber = Integer.parseInt(arguments[2]);
                    solveCmd(sudokuNumber, solverNumber);
                } catch (NumberFormatException e) {
                    println("Usage: " + SOLVE_HELP);
                }
                break;
        }
        return true;
    }

    private void help() {
        println("Available commands are:");
        println("h - help");
        println("q - quit");
        println("ls - list all sudokus");
        println(PRINT_SUDOKU_HELP + " - print sudoku");
        println(PRINT_SOLUTION_HELP + " - print solution of sudoku from that solver");
        println("solvers - list all available solvers");
        println(SOLVE_HELP + " - solve a sudoku with the specified solver");
    }

    private void solveCmd(int sudokuNumber, int solverNumber) {
        String sudokuName = getSudokuName(sudokuNumber);

        SudokuModel sudoku = controller.getSudoku(sudokuName);
        SolverType solverType = SolverType.values()[solverNumber];

        println(sudokuName, "Running " + solverType);

        boolean success = sudoku.startSolver(solverType);
        if (!success) {
            println(sudokuName, "Solving failed");
        } else {
            printSolution(sudokuNumber, solverNumber);
        }
    }

    private String getSudokuName(int sudokuNumber) {
        Set<String> sudokuNames = controller.getSudokuNames();
        final AtomicInteger indexHolder = new AtomicInteger();
        return sudokuNames.stream().filter(name -> indexHolder.getAndIncrement() == sudokuNumber)
                .findFirst().orElse(null);
    }

    private void listAllSudokus() {
        Set<String> sudokuNames = controller.getSudokuNames();
        final AtomicInteger indexHolder = new AtomicInteger();
        sudokuNames.forEach(name -> System.out.println(indexHolder.getAndIncrement() + ": " + name));
    }

    private void listAllSolvers() {
        SolverType[] solvers = SolverType.values();
        int counter = 0;
        for (SolverType solver : solvers) {
            println(counter++ + ": " + solver.toString());
        }
    }

    private void printSudoku(int sudokuNumber) {
        String sudokuName = getSudokuName(sudokuNumber);
        SudokuModel sudoku = controller.getSudoku(sudokuName);
        Integer[] grid = sudoku.getGridCopy();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                print(grid[row * 9 + col] + " ", false);
            }
            println("");
        }
    }

    private void printSolution(int sudokuNumber, int solverNumber) {
        String sudokuName = getSudokuName(sudokuNumber);
        SudokuModel sudoku = controller.getSudoku(sudokuName);
        Integer[] grid = sudoku.getSolution(SolverType.values()[solverNumber]);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                print(grid[row * 9 + col] + " ", false);
            }
            println("");
        }
    }
}
