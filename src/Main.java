import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter filename");
        //String fileName = scanner.nextLine();
//        int[][] sudoku = SudokuParser.parseSudokuFile("s01c.txt");
//        SudokuParser.printSudoku(sudoku);
//        System.out.println("\n\n");
////        BruteForceSolver.bruteForceSolver(sudoku);
////        SudokuParser.printSudoku(sudoku);
//        System.out.print("Consec String: ");
//        for(int i = 0; i < 9; i++){
//            for(int j = 0; j < 9; j++){
//                System.out.print(sudoku[i][j]);
//            }
//        }
//
//        System.out.println();
//
//        SudokuSolver.solveSudoku(sudoku);
//        SudokuParser.printSudoku(sudoku);

//        String[] easy = new String[]{"s01a.txt","s01b.txt","s01c.txt",
//                                    "s02a.txt","s02b.txt","s02c.txt",
//                                    "s06a.txt","s06b.txt","s06c.txt",
//                                    "s10a.txt","s10b.txt","s10c.txt",
//                                    "s13a.txt","s13b.txt","s13c.txt"};
//        int correct = 0;
//        for(String filename : easy){
//            int[][] sudoku = SudokuParser.parseSudokuFile(filename);
//            SudokuSolver.solveSudoku(sudoku);
//            System.out.println(filename + " is " + ((BruteForceSolver.check(sudoku)) ? "correct" : "incorrect"));
//            correct += (BruteForceSolver.check(sudoku)) ? 1 : 0;
//        }
//
//        System.out.println("Correct: " + correct + "/15");
//
//        int[][] sudoku = SudokuParser.parseSudokuFile("s02a.txt");
//        SudokuParser.printSudoku(sudoku);
//        System.out.print("Consec String: ");
//        for(int i = 0; i < 9; i++){
//            for(int j = 0; j < 9; j++){
//                System.out.print(sudoku[i][j]);
//            }
//        }
//
//        System.out.println();
//        System.out.println("\n\n");
//        SudokuSolver.solveSudoku(sudoku);
//        SudokuParser.printSudoku(sudoku);
//
//        System.out.print("Consec String: ");
//        for(int i = 0; i < 9; i++){
//            for(int j = 0; j < 9; j++){
//                System.out.print(sudoku[i][j]);
//            }
//        }
//
//        System.out.println();


        File sudokuDir = new File("C:\\Users\\bapa7\\Documents\\Java Stuff\\CSCI4463-SudokuSolver\\sudokus");
        File[] sudokuFiles;
        if(sudokuDir.isDirectory()){
            System.out.println("Sudoku Directory");
            sudokuFiles = sudokuDir.listFiles();
            int correct = 0;
            long startTime = System.currentTimeMillis();
            for(File file : sudokuFiles){
                int[][] sudoku = SudokuParser.parseSudokuFile(file.getName());
                BruteForceSolver.bruteForceSolver(sudoku);
                correct += (BruteForceSolver.check(sudoku)) ? 1 : 0;
            }
            long endTime = System.currentTimeMillis();

            System.out.println("Brute Force Solver");
            System.out.println("Correct: " + correct + "/" + sudokuFiles.length);
            System.out.println("Time: " + (endTime - startTime) + "ms");

            sudokuFiles = sudokuDir.listFiles();
            correct = 0;
            startTime = System.currentTimeMillis();
            for(File file : sudokuFiles){
                int[][] sudoku = SudokuParser.parseSudokuFile(file.getName());
                SudokuSolver.solveSudoku(sudoku);
                correct += (BruteForceSolver.check(sudoku)) ? 1 : 0;
            }
            endTime = System.currentTimeMillis();

            System.out.println("Heuristic Solver");
            System.out.println("Correct: " + correct + "/" + sudokuFiles.length);
            System.out.println("Time: " + (endTime - startTime) + "ms");

        }





//        String[] easy = new String[]{"s01a.txt","s01b.txt","s01c.txt",
//                                    "s02a.txt","s02b.txt","s02c.txt",
//                                    "s06a.txt","s06b.txt","s06c.txt",
//                                    "s10a.txt","s10b.txt","s10c.txt",
//                                    "s13a.txt","s13b.txt","s13c.txt"};
//        int correct = 0;
//        for(String filename : easy){
//            int[][] sudoku = SudokuParser.parseSudokuFile(filename);
//            SudokuSolver.solveSudoku(sudoku);
//            System.out.println(filename + " is " + ((BruteForceSolver.check(sudoku)) ? "correct" : "incorrect"));
//            correct += (BruteForceSolver.check(sudoku)) ? 1 : 0;
//        }
//
//        System.out.println("Correct: " + correct + "/15");

//        int[][] sudoku = SudokuParser.parseSudokuFile("s02a.txt");
//        SudokuParser.printSudoku(sudoku);
//        System.out.print("Consec String: ");
//        for(int i = 0; i < 9; i++){
//            for(int j = 0; j < 9; j++){
//                System.out.print(sudoku[i][j]);
//            }
//        }
//        System.out.println();
//        System.out.println("\n\n");
//        SudokuSolver.solveSudoku(sudoku);
//        SudokuParser.printSudoku(sudoku);
//        System.out.println("Correct: " + BruteForceSolver.check(sudoku));
//        System.out.println(BruteForceSolver.iterations);


    }
}
