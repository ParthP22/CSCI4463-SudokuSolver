import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter filename");
        //String fileName = scanner.nextLine();
        int[][] sudoku = SudokuParser.parseSudokuFile("s01b.txt");
        SudokuParser.printSudoku(sudoku);
        System.out.println("\n\n");
//        BruteForceSolver.bruteForceSolver(sudoku);
//        SudokuParser.printSudoku(sudoku);
        System.out.print("Consec String: ");
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(sudoku[i][j]);
            }
        }

        System.out.println();

        SudokuSolver.solveSudoku(sudoku);
        SudokuParser.printSudoku(sudoku);
    }
}
