import java.util.*;

public class SudokuSolver {

    //private HashSet<Integer>[][] availabilitySets;
    //private HashSet<Integer>[][] usedOrRemoved;
    //private HashSet<Integer>[][] constraints;
    //private int sudoku[][];
    //private PriorityQueue<int[]> constrained;
//    private int rowSize;
//    private int colSize;

//    public SudokuSolver(int rowSize, int colSize){
//        this.rowSize = rowSize;
//        this.colSize = colSize;
//    }


    public static void solveSudoku(int[][] sudokuInput){
        int[][] sudoku = sudokuInput;
        HashSet<Integer>[][] constraints = generateConstraints(sudoku);
        HashSet<Integer>[][] availabilitySets = generateAvailabilitySets(constraints,sudoku);
        //used for debugging
        //        System.out.println("set: " + availabilitySets[3][2].toString());
        constraintPropagation(availabilitySets, sudoku,1);
        dfs(sudoku,0,availabilitySets);
        //used for debugging
        //System.out.println("set: " + availabilitySets[0][1].toString());
//        for(int i = 0; i < sudoku.length; i++){
//            for(int j = 0; j < sudoku[i].length; j++){
//                if(sudoku[i][j] == 0 && !availabilitySets[i][j].isEmpty()){
//                    sudoku[i][j] = availabilitySets[i][j].iterator().next();
//                }
//            }
//        }


    }

    private static HashSet<Integer>[][] generateConstraints(int[][] sudoku){
        //First row is all the constraints of the rows
        //Second row is all the constraints of the columns
        //Third row is all the constraints of the boxes
        HashSet<Integer>[][] constraints = new HashSet[3][9];

        for(int i = 0; i < constraints.length; i++){
            for(int j = 0; j < constraints[i].length; j++){
                constraints[i][j] = new HashSet();
            }
        }

        for(int i = 0; i < sudoku.length; i++){
            for(int j = 0; j < sudoku[i].length; j++){
                if(sudoku[i][j] != 0){
                    //Calculate which 3x3 box this is in
                    int boxRow = i/3;
                    int boxCol = j/3;
                    int boxIndex = boxRow*3 + boxCol;

                    constraints[0][i].add(sudoku[i][j]);
                    constraints[1][j].add(sudoku[i][j]);
                    constraints[2][boxIndex].add(sudoku[i][j]);
                }
            }
        }
        return constraints;
    }

    private static HashSet<Integer>[][] generateAvailabilitySets(HashSet<Integer>[][] constraints, int[][] sudoku){
        HashSet<Integer>[][] availabilitySets = new HashSet[9][9];
        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){
                availabilitySets[i][j] = new HashSet();

                //Calculate which 3x3 box this is in
                int boxRow = i/3;
                int boxCol = j/3;
                int boxIndex = boxRow*3 + boxCol;

                if(sudoku[i][j] == 0){
                    for(int k = 1; k <= 9; k++){
                        if(!constraints[0][i].contains(k) &&
                                !constraints[1][j].contains(k) &&
                                !constraints[2][boxIndex].contains(k)){

                            availabilitySets[i][j].add(k);
                        }
                    }
                }
            }
        }
        return availabilitySets;

    }

    private static boolean constraintPropagation(HashSet<Integer>[][] availabilitySets, int[][] sudoku, int iteration){
        int changes = 0;
        //System.out.println("Constraint Prop Iteration " + iteration);
        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){
                if(availabilitySets[i][j].size() == 1){
                    int onlyElement = availabilitySets[i][j].iterator().next();

                    //Calculate which 3x3 box this is in
                    int boxRow = i/3;
                    int boxCol = j/3;

                    //Iterate through the box that this element is in
                    for(int k = 3*boxRow; k < 3*boxRow + 3; k++){
                        for(int l = 3*boxCol; l < 3*boxCol + 3; l++){
                            if(!(i == k && j == l) && availabilitySets[k][l].contains(onlyElement) && sudoku[k][l] == 0) {
                                //used for debugging
//                                if(k == 0 && l == 4){
//                                    System.out.println("indices: " + i + ", " + j + ", " + k + ", " + l);
//                                    System.out.println("set: box" + availabilitySets[0][4].toString());
//                                }
                                availabilitySets[k][l].remove(onlyElement);
                                //System.out.println("removed " + onlyElement  + "from ("+k+","+l+") because (" + i + "," + j + ") only has this element");
                                if(availabilitySets[k][l].isEmpty()){
//                                    System.out.println("box: value " + onlyElement   + "from (" + i + "," + j + ") conflicts with index ("+k+","+l+") during constraint prop");
//                                    System.out.println("set["+i+","+j+"]: " + availabilitySets[i][j].toString());
//                                    System.out.println("set["+k+","+l+"]: " + availabilitySets[k][l].toString());
                                    return false;
                                }
                                changes++;
                            }
                                //usedOrRemoved[k][l].add(onlyElement);
                        }
                    }

                    //Iterate through the column that this element is in
                    for(int k = 0; k < 9; k++){
                        if(i != k && availabilitySets[k][j].contains(onlyElement)  && sudoku[k][j] == 0) {
//                            //used for debugging
//                            if(k == 0 && j == 4){
//                                System.out.println("set: col" + availabilitySets[0][4].toString());
//                            }
                            availabilitySets[k][j].remove(onlyElement);
                            //System.out.println("removed " + onlyElement  + "from ("+k+","+j+") because (" + i + "," + j + ") only has this element");
                            if(availabilitySets[k][j].isEmpty()){
//                                System.out.println("col: value " + onlyElement   + "from (" + i + "," + j + ") conflicts with index ("+k+","+j+") during constraint prop");
//                                System.out.println("set["+i+","+j+"]: " + availabilitySets[i][j].toString());
//                                System.out.println("set["+k+","+j+"]: " + availabilitySets[k][j].toString());

                                return false;
                            }
                            changes++;
                        }
                            //usedOrRemoved[k][j].add(onlyElement);
                    }

                    //Iterate through the row that this element is in
                    for(int k = 0; k < 9; k++){
                        if(j != k && availabilitySets[i][k].contains(onlyElement)  && sudoku[i][k] == 0) {
//                            //used for debugging
//                            if(i == 0 && k == 4){
//                                System.out.println("set: row" + availabilitySets[0][4].toString());
//                            }
                            availabilitySets[i][k].remove(onlyElement);
                            //System.out.println("removed " + onlyElement  + "from ("+i+","+k+") because (" + i + "," + j + ") only has this element");
                            if(availabilitySets[i][k].isEmpty()){
//                                System.out.println("row: value " + onlyElement  + "from (" + i + "," + j + ") conflicts with index ("+i+","+k+") during constraint prop");
//                                System.out.println("set["+i+","+j+"]: " + availabilitySets[i][j].toString());
//                                System.out.println("set["+i+","+k+"]: " + availabilitySets[i][k].toString());

                                return false;
                            }
                            changes++;
                        }
                        //usedOrRemoved[i][k].add(onlyElement);
                    }
                }
            }
        }
        if(changes > 0){
            //System.out.println("changes: " + changes);
            return constraintPropagation(availabilitySets, sudoku, iteration+1);
        }
        return true;
    }

    private static boolean constraintPropagation(int index, int value, HashSet<Integer>[][] availabilitySets, int[][] sudoku, int iteration){
        int changes = 0;
        int rowIndex = index / 9;
        int colIndex = index % 9;
        //System.out.println("Constraint Prop Iteration " + iteration);
        //Iterate through the box that this element is in
        int boxRow = rowIndex / 3;
        int boxCol = colIndex / 3;
        for(int k = 3*boxRow; k < 3 * boxRow + 3; k++){
            for(int l = 3*boxCol; l < 3 * boxCol + 3; l++){
                if(!(rowIndex == k && colIndex == l) && availabilitySets[k][l].contains(value)  && sudoku[k][l] == 0) {
                    //used for debugging
//                                if(k == 0 && l == 4){
//                                    System.out.println("indices: " + i + ", " + j + ", " + k + ", " + l);
//                                    System.out.println("set: box" + availabilitySets[0][4].toString());
//                                }
                    availabilitySets[k][l].remove(value);
                    //System.out.println("removed " + value  + "from ("+k+","+l+") because (" + rowIndex + "," + colIndex + ") has this value");
                    if(availabilitySets[k][l].isEmpty()){
//                        System.out.println("box: value " + value  + "from (" + rowIndex + "," + colIndex + ") conflicts with index ("+k+","+l+") during constraint prop");
//                        System.out.println("set[" + rowIndex + "," + colIndex + "]: " + availabilitySets[index/9][index%9].toString());
//                        System.out.println("set["+k+","+l+"]: " + availabilitySets[k][l].toString());
                        return false;
                    }
                    changes++;
                }

                //usedOrRemoved[k][l].add(onlyElement);
            }
        }

        //Iterate through the column that this element is in
        for(int k = 0; k < 9; k++){
            if(rowIndex != k && availabilitySets[k][colIndex].contains(value) && sudoku[k][colIndex] == 0) {
//                            //used for debugging
//                if(k == 0 && j == 4){
//                    System.out.println("set: col" + availabilitySets[0][4].toString());
//                }
                availabilitySets[k][colIndex].remove(value);
                //System.out.println("removed " + value  + "from ("+k+","+colIndex+") because (" + rowIndex + "," + colIndex + ") has this value");
                if(availabilitySets[k][colIndex].isEmpty()){
//                    System.out.println("col: value " + value  + "from (" + rowIndex + "," + colIndex + ") conflicts with index ("+k+","+colIndex+") during constraint prop");
//                    System.out.println("set[" + rowIndex + "," + colIndex + "]: " + availabilitySets[index/9][index%9].toString());
//                    System.out.println("set["+k+","+colIndex+"]: " + availabilitySets[k][colIndex].toString());
                    return false;
                }
                changes++;
            }

            //usedOrRemoved[k][indices[1]].add(onlyElement);
        }

        //Iterate through the row that this element is in
        for(int k = 0; k < 9; k++){
            if(colIndex != k && availabilitySets[rowIndex][k].contains(value)  && sudoku[rowIndex][k] == 0) {
//                            //used for debugging
//                if(i == 0 && k == 4){
//                    System.out.println("set: row" + availabilitySets[0][4].toString());
//                }
                availabilitySets[rowIndex][k].remove(value);
                //System.out.println("removed " + value  + "from ("+rowIndex+","+k+") because (" + rowIndex + "," + colIndex + ") has this value");
                if(availabilitySets[rowIndex][k].isEmpty()){

//                    System.out.println("row: value " + value + "from (" + rowIndex + "," + colIndex + ") conflicts with index ("+rowIndex+","+k+") during constraint prop");
//                    System.out.println("set[" + rowIndex + "," + colIndex + "]: " + availabilitySets[index/9][index%9].toString());
//                    System.out.println("set["+rowIndex+","+k+"]: " + availabilitySets[k][colIndex].toString());

                    return false;
                }
                changes++;
            }

            //usedOrRemoved[indices[0]][k].add(onlyElement);
        }

        if(changes > 0){
            return constraintPropagation(availabilitySets, sudoku, iteration+1);
        }
        return true;

    }


    private static int[] mostConstrainedVariable(HashSet<Integer>[][] availabilitySets){
        PriorityQueue<int[]> constrained = new PriorityQueue<>((a,b) -> availabilitySets[a[0]][a[1]].size() - availabilitySets[b[0]][b[1]].size());

        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){
                if(availabilitySets[i][j].size() >= 1){
                    constrained.offer(new int[]{i, j});
                }
            }
        }

        return constrained.peek();

    }

    private static int leastConstrainedValue(int[] indices, HashSet<Integer>[][] availabilitySets){
        HashSet<Integer> currAvailabilitySet = availabilitySets[indices[0]][indices[1]];
        HashMap<Integer,Integer> freq = new HashMap<>();


        for(int num : currAvailabilitySet) {
            //Iterate through the box that this element is in
            for (int k = 0; k < 3 * (indices[0] / 3) + 3; k++) {
                for (int l = 0; l < 3 * (indices[1] % 3) + 3; l++) {
                    if(availabilitySets[k][l].contains(num)){
                        freq.put(num, freq.getOrDefault(num,0) + 1);
                    }
                }
            }

            //Iterate through the column that this element is in
            for (int k = 0; k < 9; k++) {
                if(availabilitySets[k][indices[1]].contains(num)){
                    freq.put(num, freq.getOrDefault(num,0) + 1);
                }
            }

            //Iterate through the row that this element is in
            for (int k = 0; k < 9; k++) {
                if(availabilitySets[indices[0]][k].contains(num)){
                    freq.put(num, freq.getOrDefault(num,0) + 1);
                }
            }
        }

        //index 0 is the number, index 1 is the frequency
        int[] leastConstrainingValue = new int[2];

        for(Map.Entry<Integer,Integer> entry : freq.entrySet()){
            if(entry.getValue() < leastConstrainingValue[1]){
                leastConstrainingValue[0] = entry.getKey();
            }
        }

        return leastConstrainingValue[0];

    }

    private static boolean dfs(int[][] sudoku, int index, HashSet<Integer>[][] oldAvailabilitySets){
        for (int i = index; i < 81; i++) {
            if(sudoku[i / 9][i % 9] == 0) {
                for (int num : oldAvailabilitySets[i / 9][i % 9]) {
                    sudoku[i / 9][i % 9] = num;

                    HashSet<Integer>[][] newAvailabilitySets = copyAvailabilitySets(oldAvailabilitySets);
                    //printAvailabilitySets(newAvailabilitySets);
//                    System.out.println("set["+(i / 9)+","+(i % 9)+"] = " + newAvailabilitySets[i / 9][i % 9].toString());
//                    System.out.println("Assigning " + num + " to ("+(i / 9)+","+(i % 9)+")");
                    if(!constraintPropagation(i, num, newAvailabilitySets, sudoku,1)){
                        //System.out.println("value " + num + " failed at index: (" +(i / 9)+","+(i % 9)+ "); backtracking\n");
                        continue;
                    }


                    if (dfs(sudoku, i+1, newAvailabilitySets)) {
                        return true;
                    }




                }
                sudoku[i / 9][i % 9] = 0;
                return false;
            }
        }

        return true;

    }

    private static HashSet<Integer>[][] copyAvailabilitySets(HashSet<Integer>[][] oldAvailabilitySets) {
        HashSet<Integer>[][] newAvailabilitySets = new HashSet[9][9];

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                newAvailabilitySets[i][j] = new HashSet<>();
                for(int num : oldAvailabilitySets[i][j]){
                    newAvailabilitySets[i][j].add(num);
                }
            }
        }

        return newAvailabilitySets;

    }

    public static void printAvailabilitySets(HashSet<Integer>[][] availabilitySets){
        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[0].length; j++){
                int[] set = new int[9];
                int k = 0;
                for(int num : availabilitySets[i][j]){
                    set[k++] = num;
                }
                System.out.printf("(%d,%d): -%d -%d -%d -%d -%d -%d -%d -%d -%d         \n",i,j,set[0],set[1],set[2],
                                            set[3],set[4],set[5],
                        set[6],set[7],set[8]);
            }
        }
    }


//    private static void recurse(){
//
//    }
}
