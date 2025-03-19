import java.util.*;

public class SudokuSolver {

    private static HashSet<Integer>[][] availabilitySets;
    private static HashSet<Integer>[][] usedOrRemoved;
    private static HashSet<Integer>[][] constraints;
    private static int sudoku[][];
    private static PriorityQueue<int[]> constrained;

    public static void solveSudoku(int[][] sudoku){
        generateConstraints();
        generateAvailabilitySets();

        constraintPropagation();




    }

    private static void generateConstraints(){
        //First row is all the constraints of the rows
        //Second row is all the constraints of the columns
        //Third row is all the constraints of the boxes
        constraints = new HashSet[3][9];

        for(int i = 0; i < constraints.length; i++){
            for(int j = 0; j < constraints[i].length; j++){
                constraints[i][j] = new HashSet();
            }
        }

        for(int i = 0; i < sudoku.length; i++){
            for(int j = 0; j < sudoku[i].length; j++){
                if(sudoku[i][j] != 0){
                    constraints[0][i].add(sudoku[i][j]);
                    constraints[1][j].add(sudoku[i][j]);
                    constraints[2][i].add(sudoku[i / 3][j % 3]);
                }
            }
        }

    }

    private static void generateAvailabilitySets(){
        availabilitySets = new HashSet[9][9];
        usedOrRemoved = new HashSet[9][9];
        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){
                availabilitySets[i][j] = new HashSet();
                usedOrRemoved[i][j] = new HashSet();
                if(sudoku[i][j] == 0){
                    for(int k = 1; k <= 9; k++){
                        if(!constraints[i][j].contains(k)){
                            availabilitySets[i][j].add(k);
                        }
                    }
                }
            }
        }

    }

    private static void constraintPropagation(){
        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){
                if(availabilitySets[i][j].size() == 1){
                    int onlyElement = availabilitySets[i][j].iterator().next();

                    //Iterate through the box that this element is in
                    for(int k = 0; k < 3 * (i / 3) + 3; k++){
                        for(int l = 0; l < 3 * (j % 3) + 3; l++){
                            availabilitySets[k][l].remove(onlyElement);
                            usedOrRemoved[k][l].add(onlyElement);
                        }
                    }

                    //Iterate through the column that this element is in
                    for(int k = 0; k < 9; k++){
                        availabilitySets[k][j].remove(onlyElement);
                        usedOrRemoved[k][j].add(onlyElement);
                    }

                    //Iterate through the row that this element is in
                    for(int k = 0; k < 9; k++){
                        availabilitySets[i][k].remove(onlyElement);
                        usedOrRemoved[i][k].add(onlyElement);
                    }
                }
            }
        }
    }

    private static void constraintPropagation(int[] indices){
        int onlyElement = availabilitySets[indices[0]][indices[1]].iterator().next();

        //Iterate through the box that this element is in
        for(int k = 0; k < 3 * (indices[0] / 3) + 3; k++){
            for(int l = 0; l < 3 * (indices[1] % 3) + 3; l++){
                availabilitySets[k][l].remove(onlyElement);
                usedOrRemoved[k][l].add(onlyElement);
            }
        }

        //Iterate through the column that this element is in
        for(int k = 0; k < 9; k++){
            availabilitySets[k][indices[1]].remove(onlyElement);
            usedOrRemoved[k][indices[1]].add(onlyElement);
        }

        //Iterate through the row that this element is in
        for(int k = 0; k < 9; k++){
            availabilitySets[indices[0]][k].remove(onlyElement);
            usedOrRemoved[indices[0]][k].add(onlyElement);
        }
    }


    private static int[] mostConstrainedVariable(){
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

    private static int leastConstrainedValue(int[] indices){
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


}
