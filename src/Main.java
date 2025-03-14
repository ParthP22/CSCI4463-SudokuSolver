import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter filename");
        String fileName = scanner.nextLine();
        int[][] sudoku = SudokuParser.parseSudokuFile(fileName);
        SudokuParser.printSudoku(sudoku);
    }
}
