import java.io.*;

/**
 * This file is used to parse the Sudoku text files and generate corresponding
 * 9x9 matrices, as well as print out Sudokus from their matrix format.
 *
 * @author Parth Patel, Ahmed Malik, James Calabrese
 */
public class SudokuParser {

    /**
     * Converts a Sudoku text file into a 9x9 matrix.
     *
     * @param filename the filename with .txt extension of a Sudoku text file
     * @return a 9x9 matrix containing the Sudoku puzzle
     * @throws IOException
     */
    public static int[][] parseSudokuFile(String filename) throws IOException {
        if(filename == null || filename.isEmpty()){
            throw new FileNotFoundException();
        }

        FileReader fileReader = new FileReader("./sudokus/"+filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);



        int[][] sudoku = new int[9][9];

        String line = bufferedReader.readLine();
        int row = 0;
        while(line != null){

            for(int col = 0; col < line.length(); col += 2){
                sudoku[row][col/2] = (line.charAt(col)) - '0';
            }
            row++;
            line = bufferedReader.readLine();
        }
        fileReader.close();
        bufferedReader.close();

        return sudoku;
    }

    /**
     * Prints out a 9x9 Sudoku puzzle in space-delimited form
     *
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     */
    public static void printSudoku(int[][] sudoku){
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                System.out.print(sudoku[row][col] + " ");
            }
            System.out.println();
        }

    }
}
