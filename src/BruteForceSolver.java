import java.util.HashSet;

public class BruteForceSolver {

    public static int iterations = 0;

    public static void bruteForceSolver(int[][] sudoku) {
        dfs(sudoku,0);

    }

    public static void printSudoku(int[][] sudoku){
        for(int i = 0; i < sudoku.length; i++){
            for(int j = 0; j < sudoku[i].length; j++){
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean dfs(int[][] sudoku, int index){
        iterations++;
//        sudoku[i][j] = val;
//        for(int[] dir : directions){
//            if(checkValue(sudoku,val,i + dir[0],j + dir[1])) {
//                if(dfs(sudoku, i + dir[0], j + dir[1], val)){
//                    return true;
//                }
//                sudoku[i][j] = 0;
//            }
//        }
//        return false;

//        if(row < 0 || row >= sudoku.length || col < 0 || col >= sudoku[0].length){
//            return true;
//        }
//
//        int[][] directions = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
//
//        if(sudoku[row][col] == 0) {
//            for (int num = 1; num <= 9; num++) {
//
//                if (checkValue(sudoku, row, col, num)) {
//                    System.out.println("Assigning ("+row+","+col+") to "+num);
//                    sudoku[row][col] = num;
//                    for(int[] dir : directions){
//                        if (dfs(sudoku, row + dir[0], col + dir[1])) {
//                            return true;
//                        }
//                    }
//
//                    sudoku[row][col] = 0;
//                }
//
//            }
//            return false;
//        }
//        else{
//            for(int[] dir : directions){
//                if (dfs(sudoku, row + dir[0], col + dir[1])) {
//                    return true;
//                }
//            }
//
//        }
//        return true;

        // Iterating with one for-loop like this will allow me to start at the exact row
        // and column that the previous iteration left off at.
        for (int i = index; i < 81; i++) {
                if(sudoku[i / 9][i % 9] == 0) {
                    for (int num = 1; num <= 9; num++) {

                        if (checkValue(sudoku, i / 9, i % 9, num)) {
                            //System.out.println("Assigning ("+i+","+j+") to "+num);
                            sudoku[i / 9][i % 9] = num;
                            if (dfs(sudoku, i+1)) {
                                return true;
                            }


                        }

                    }
                    sudoku[i / 9][i % 9] = 0;
                    return false;
                }
            }

        return true;



    }

    public static boolean check(int[][] sudoku){
        boolean[][][] exists = new boolean[3][9][9];

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(sudoku[i][j] == 0 || sudoku[j][i] == 0 || exists[0][i][sudoku[i][j] - 1] || exists[1][i][sudoku[j][i] - 1]) {
                    System.out.println("i: " + i + ", j: " + j);
                    return false;
                }
                else{
                    exists[0][i][sudoku[i][j] - 1] = true;
                    exists[1][i][sudoku[j][i] - 1] = true;
                }
            }
        }



        for(int i = 0; i < 9; i++){
            for(int j = (i / 3) * 3; j < (i / 3) * 3 + 3; j++){
                for(int k = (i / 3) * 3; k < (i / 3) * 3 + 3; k++){
                    if(sudoku[j][k] == 0 || exists[2][i][sudoku[j][k] - 1]) {
                        return false;
                    }
                    else{
                        exists[2][i][sudoku[j][k] - 1] = true;
                    }
                }
            }
        }

        return true;

    }

    public static boolean checkValue(int[][] sudoku, int row, int col, int val){

        for(int i = 0; i < 9; i++){
            if(sudoku[row][i] == val || sudoku[i][col] == val || sudoku[3*(row / 3) + i / 3][3*(col / 3) + i % 3] == val){
                return false;
            }
        }
        return true;
    }





}
