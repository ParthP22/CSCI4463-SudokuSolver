import java.io.*;

public class SudokuParser {

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

    public static void printSudoku(int[][] sudoku){
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                System.out.print(sudoku[row][col] + " ");
            }
            System.out.println();
        }

    }
}
