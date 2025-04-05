import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * The main method that is run when you wish to solve
 * a Sudoku puzzle.
 *
 * The user is prompted for input where they must enter the name
 * of the text file from the "sudokus" directory containing the
 * Sudoku puzzle that they wish to solve.
 *
 * Afterward, the solved Sudoku is printed.
 *
 * @author Parth Patel, Ahmed Malik, James Calabrese
 */
public class Main {
    public static void main(String[] args) throws IOException {

        // To obtain user input (required by the assignment instructions)
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter filename with .txt (must be from the \"sudokus\" directory)");
        System.out.println("Example: \"s01a.txt\" (but don't enter quotes, just do the filename)");
        String fileName = scanner.nextLine();

        int[][] sudoku = SudokuParser.parseSudokuFile(fileName);

        SudokuSolver.solveSudoku(sudoku);

        SudokuParser.printSudoku(sudoku);



    }
}
