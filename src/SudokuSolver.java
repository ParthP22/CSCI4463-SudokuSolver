import java.util.*;

/**
 * This class contains the solveSudoku method, which will solve
 * any given 9x9 Sudoku puzzle in matrix form using DFS with
 * constraint propagation
 *
 * @author Parth Patel, Ahmed Malik, James Calabrese
 */
public class SudokuSolver {

    /**
     * Takes in a 9x9 Sudoku matrix and solves it by calling constraint
     * propagation first and then runs DFS w/ constraint propagation
     * @param sudokuInput a 9x9 matrix that contains the Sudoku puzzle
     */
    public static void solveSudoku(int[][] sudokuInput){
        int[][] sudoku = sudokuInput;

        // First, generate all constraints for each element of the 9x9
        // matrix. The constraints are basically what each element CANNOT
        // be.
        HashSet<Integer>[][] constraints = generateConstraints(sudoku);

        // Once the constraints are defined, you will generate each element's
        // availability sets.
        HashSet<Integer>[][] availabilitySets = generateAvailabilitySets(constraints,sudoku);

        // Prior to DFS, run constraint propagation. This alone will actually solve
        // most of the easy-difficulty puzzles.
        constraintPropagation(availabilitySets, constraints, sudoku);

        // Finally, run DFS, which has constraint propagation implemented as well
        dfs(sudoku, availabilitySets, constraints);
    }

    /**
     * Generates the constraining values for each row, column, and box of the Sudoku matrix.
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     * @return a 3x9 matrix of HashSets containing the constraining values for each row, each column and each box
     */
    private static HashSet<Integer>[][] generateConstraints(int[][] sudoku){
        // First row is all the constraints of the rows
        // Second row is all the constraints of the columns
        // Third row is all the constraints of the boxes
        HashSet<Integer>[][] constraints = new HashSet[3][9];

        // Initialize each HashSet
        for(int i = 0; i < constraints.length; i++){
            for(int j = 0; j < constraints[i].length; j++){
                constraints[i][j] = new HashSet();
            }
        }

        // Traverse the entire Sudoku matrix. If the element is not
        // 0, then it will be a constraint for its specific row,
        // column, or box.
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

        // Return the constraints
        return constraints;
    }

    /**
     * Generates the availability sets for each element in the Sudoku puzzle.
     *
     * @param constraints a 3x9 matrix of HashSets containing the constraining values for each row, column, and box
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     * @return a 9x9 matrix of HashSets containing the availability sets of each element in the Sudoku puzzle
     */
    private static HashSet<Integer>[][] generateAvailabilitySets(HashSet<Integer>[][] constraints, int[][] sudoku){

        // 9x9 matrix of HashSets that will be used as the availability sets for each
        // element of the Sudoku puzzle
        HashSet<Integer>[][] availabilitySets = new HashSet[9][9];

        // Initialize and fill each availability set
        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){
                availabilitySets[i][j] = new HashSet();

                // Calculate which 3x3 box this is in
                int boxRow = i/3;
                int boxCol = j/3;

                // There are nine 3x3 boxes in a 9x9 Sudoku puzzle.
                // From left to right, top to bottom, the boxes are
                // numbered as: 0,1,2,3,4,5,6,7,8.
                // Below is the corresponding index of which box we
                // are in.
                int boxIndex = boxRow*3 + boxCol;

                // We will only fill the availability sets of unassigned elements.
                if(sudoku[i][j] == 0){
                    for(int k = 1; k <= 9; k++){
                        // Check all values from 1 through 9.
                        // If it isn't a constraint, then it can safely
                        // be added in this current element's availability set.
                        if(!constraints[0][i].contains(k) &&
                                !constraints[1][j].contains(k) &&
                                !constraints[2][boxIndex].contains(k)){

                            availabilitySets[i][j].add(k);
                        }
                    }
                }
            }
        }

        // Return the availability sets at the end
        return availabilitySets;

    }

    /**
     * This method will perform constraint propagation on the Sudoku puzzle
     * by traversing the entire Sudoku itself. If there is an element containing
     * only one element, then we will remove that element from all other availability
     * sets in that element's row, column, and box.
     *
     * @param availabilitySets the 9x9 matrix of HashSets that contain the availability sets of each element in the Sudoku puzzle
     * @param constraints a 3x9 matrix of HashSets containing the constraining values for each row, column, and box
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     * @return false if backtracking is required (an element's availability set has been fully emptied), true otherwise
     */
    private static boolean constraintPropagation(HashSet<Integer>[][] availabilitySets, HashSet<Integer>[][] constraints, int[][] sudoku){

        // Count the number of changes that have been made.
        // (Basically, the number of times a value was removed any
        // element's availability set).
        int changes = 0;

        // Iterate over ALL availability sets
        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){

                // If there is an element containing only one value in
                // its availability set, then remove this element from
                // the availability sets of all elements in its row, column,
                // and box.
                if(availabilitySets[i][j].size() == 1){

                    // This will grab the only remaining value in the current
                    // element's availability set.
                    int onlyElement = availabilitySets[i][j].iterator().next();

                    // Calculate which 3x3 box this is in
                    int boxRow = i/3;
                    int boxCol = j/3;

                    // Iterate through the box that this element is in
                    for(int k = 3*boxRow; k < 3*boxRow + 3; k++){
                        for(int l = 3*boxCol; l < 3*boxCol + 3; l++){

                            // We make sure that we are not checking the current element at index (i,j)
                            // (we don't want to remove this value from the element that we are literally checking),
                            // and we also make sure that the availability set we are removing from actually does
                            // have the current value, and we also make sure that the element is unassigned (we don't
                            // care about removing this value from the availability set of an already assigned value).
                            if(!(i == k && j == l) && availabilitySets[k][l].contains(onlyElement) && sudoku[k][l] == 0) {
                                availabilitySets[k][l].remove(onlyElement);

                                // Add this element to the constraints
                                constraints[2][3*boxRow + boxCol].add(onlyElement);

                                // If removing this element results in an empty availability set, then we must
                                // backtrack because we won't find the correct answer in this path of DFS.
                                if(availabilitySets[k][l].isEmpty()){
                                    return false;
                                }

                                // Since an element has been removed, we increment the changes counter
                                changes++;
                            }
                        }
                    }

                    // Iterate through the column that this element is in
                    for(int k = 0; k < 9; k++){
                        if(i != k && availabilitySets[k][j].contains(onlyElement)  && sudoku[k][j] == 0) {

                            // We make sure that we are not checking the current element at index (i,j)
                            // (we don't want to remove this value from the element that we are literally checking),
                            // and we also make sure that the availability set we are removing from actually does
                            // have the current value, and we also make sure that the element is unassigned (we don't
                            // care about removing this value from the availability set of an already assigned value).
                            availabilitySets[k][j].remove(onlyElement);

                            // Add this element to the constraints
                            constraints[1][j].add(onlyElement);

                            // If removing this element results in an empty availability set, then we must
                            // backtrack because we won't find the correct answer in this path of DFS.
                            if(availabilitySets[k][j].isEmpty()){
                                return false;
                            }

                            // Since an element has been removed, we increment the changes counter
                            changes++;
                        }
                    }

                    //Iterate through the row that this element is in
                    for(int k = 0; k < 9; k++){
                        if(j != k && availabilitySets[i][k].contains(onlyElement)  && sudoku[i][k] == 0) {

                            // We make sure that we are not checking the current element at index (i,j)
                            // (we don't want to remove this value from the element that we are literally checking),
                            // and we also make sure that the availability set we are removing from actually does
                            // have the current value, and we also make sure that the element is unassigned (we don't
                            // care about removing this value from the availability set of an already assigned value).
                            availabilitySets[i][k].remove(onlyElement);

                            // Add this element to the constraints
                            constraints[0][i].add(onlyElement);

                            // If removing this element results in an empty availability set, then we must
                            // backtrack because we won't find the correct answer in this path of DFS.
                            if(availabilitySets[i][k].isEmpty()){
                                return false;
                            }

                            // Since an element has been removed, we increment the changes counter
                            changes++;
                        }
                    }
                }
            }
        }

        // If any changes were made, then we must run constraint propagation again.
        if(changes > 0){
            return constraintPropagation(availabilitySets, constraints, sudoku);
        }

        // We will return true, because this means no backtracking is required
        return true;
    }

    /**
     * This constraint propagation is only performed whenever we are picking
     * a specific value from the availability set of an element (this is why
     * it has <code>index</code> and <code>value</code> parameters).
     *
     * Unlike the other constraint propagation method, we will only be removing
     * the <code>value</code> from the availability sets of all elements in the same
     * row, column, and box as the element at <code>index</code> (we aren't iterating
     * over the entire 9x9 matrix to find availability sets with one remaining element,
     * because that is a task for the other constraint propagation method).
     *
     * @param index an <code>int</code> from [0,80] representing an index of an element in a 9x9 matrix
     * @param value an <code>int</code> from [1,9] that we have selected during DFS for this element
     * @param availabilitySets the 9x9 matrix of HashSets that contain the availability sets of each element in the Sudoku puzzle
     * @param constraints a 3x9 matrix of HashSets containing the constraining values for each row, column, and box
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     * @return false if backtracking is required (an element's availability set has been fully emptied), true otherwise
     */
    private static boolean constraintPropagation(int index, int value, HashSet<Integer>[][] availabilitySets, HashSet<Integer>[][] constraints, int[][] sudoku){

        // Count the number of changes that have been made.
        // (Basically, the number of times a value was removed any
        // element's availability set).
        int changes = 0;

        // We can derive the row and column indices of this current element
        // based on "index" by doing this:
        int rowIndex = index / 9;
        int colIndex = index % 9;

        //Iterate through the box that this element is in
        int boxRow = rowIndex / 3;
        int boxCol = colIndex / 3;

        for(int k = 3*boxRow; k < 3 * boxRow + 3; k++){
            for(int l = 3*boxCol; l < 3 * boxCol + 3; l++){

                if(!(rowIndex == k && colIndex == l) && availabilitySets[k][l].contains(value)  && sudoku[k][l] == 0) {

                    // We make sure that we are not checking the current element at index (i,j)
                    // (we don't want to remove this value from the element that we are literally checking),
                    // and we also make sure that the availability set we are removing from actually does
                    // have the current value, and we also make sure that the element is unassigned (we don't
                    // care about removing this value from the availability set of an already assigned value).
                    availabilitySets[k][l].remove(value);

                    // If removing this element results in an empty availability set, then we must
                    // backtrack because we won't find the correct answer in this path of DFS.
                    if(availabilitySets[k][l].isEmpty()){
                        return false;
                    }

                    // Since an element has been removed, we increment the changes counter
                    changes++;
                }

            }
        }

        //Iterate through the column that this element is in
        for(int k = 0; k < 9; k++){
            if(rowIndex != k && availabilitySets[k][colIndex].contains(value) && sudoku[k][colIndex] == 0) {

                // We make sure that we are not checking the current element at index (i,j)
                // (we don't want to remove this value from the element that we are literally checking),
                // and we also make sure that the availability set we are removing from actually does
                // have the current value, and we also make sure that the element is unassigned (we don't
                // care about removing this value from the availability set of an already assigned value).
                availabilitySets[k][colIndex].remove(value);

                // If removing this element results in an empty availability set, then we must
                // backtrack because we won't find the correct answer in this path of DFS.
                if(availabilitySets[k][colIndex].isEmpty()){
                    return false;
                }

                // Since an element has been removed, we increment the changes counter
                changes++;
            }

        }

        //Iterate through the row that this element is in
        for(int k = 0; k < 9; k++){
            if(colIndex != k && availabilitySets[rowIndex][k].contains(value)  && sudoku[rowIndex][k] == 0) {

                // We make sure that we are not checking the current element at index (i,j)
                // (we don't want to remove this value from the element that we are literally checking),
                // and we also make sure that the availability set we are removing from actually does
                // have the current value, and we also make sure that the element is unassigned (we don't
                // care about removing this value from the availability set of an already assigned value).
                availabilitySets[rowIndex][k].remove(value);

                // If removing this element results in an empty availability set, then we must
                // backtrack because we won't find the correct answer in this path of DFS.
                if(availabilitySets[rowIndex][k].isEmpty()){
                    return false;
                }

                // Since an element has been removed, we increment the changes counter
                changes++;
            }

        }

        // If any changes were made, then we must run constraint propagation again.
        // However, we will be running the other constraint propagation algorithm
        // (we only call this method when we are assigning a new value in our DFS.
        // Since we have already assigned the value, we will just call the other
        // constraint propagation to go over the entire 9x9 matrix and see if there
        // are any availability sets with one element remaining).
        if(changes > 0){
            return constraintPropagation(availabilitySets, constraints, sudoku);
        }

        // We will return true, because this means no backtracking is required
        return true;

    }

    /**
     * Creates and returns a PriorityQueue of all remaining variables in the Sudoku puzzle
     * first sorted from most constrained to least constrained, and ties are broken using
     * most constraining variable heuristic.
     *
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     * @param availabilitySets the 9x9 matrix of HashSets that contain the availability sets of each element in the Sudoku puzzle
     * @param constraints a 3x9 matrix of HashSets containing the constraining values for each row, column, and box
     * @return <code>PriorityQueue</code> containing the remaining variables sorted from most constrained to least constrained
     * and ties are broken with most constraining variable heuristic
     */
    private static PriorityQueue<int[]> mostConstrainedVariable(int[][] sudoku, HashSet<Integer>[][] availabilitySets, HashSet<Integer>[][] constraints){
        PriorityQueue<int[]> constrained = new PriorityQueue<>((a,b) -> (availabilitySets[a[0]][a[1]].size() == availabilitySets[b[0]][b[1]].size()) ?
                                                                        mostConstrainingVariable(constraints,a,b) :
                                                                        availabilitySets[a[0]][a[1]].size() - availabilitySets[b[0]][b[1]].size());

        for(int i = 0; i < availabilitySets.length; i++){
            for(int j = 0; j < availabilitySets[i].length; j++){
                if(availabilitySets[i][j].size() >= 1 && sudoku[i][j] == 0){
                    constrained.offer(new int[]{i, j});
                }
            }
        }

        return constrained;

    }

    /**
     * Compares two variables in the Sudoku puzzle, and determines which one is more constraining
     * on other variables.
     *
     * @param constraints a 3x9 matrix of HashSets containing the constraining values for each row, column, and box
     * @param indicesA the row and column indices of element A
     * @param indicesB the row and column indices of element B
     * @return a positive integer if element B is to have higher priority, a negative integer if element A
     * is to have higher priority, or zero if it's a tie
     */
    private static int mostConstrainingVariable(HashSet<Integer>[][] constraints, int[] indicesA, int[] indicesB){

        // Obtain the indices for the row, col, and box for element A
        int aRow = indicesA[0];
        int aCol = indicesA[1];
        int aBoxRow = aRow/3;
        int aBoxCol = aCol/3;
        int aBoxIndex = aBoxRow*3 + aBoxCol;

        // Obtain the indices for the row, col, and box for element B
        int bRow = indicesB[0];
        int bCol = indicesB[1];
        int bBoxRow = bRow/3;
        int bBoxCol = bCol/3;
        int bBoxIndex = bBoxRow*3 + bBoxCol;

        // Add up how many unassigned variables each A and B share constraints with.
        // We subtract them from 8, because the constraints[][].size() will give the number of assigned
        // elements, so we are just finding the opposite of that number; it's like taking the complement
        // (also, we do 8 and not 9, because we are not considering the current element when calculating this
        // value).
        int aConstrains = (8 - constraints[0][aRow].size()) + (8 - constraints[1][aCol].size()) + (8 - constraints[2][aBoxIndex].size());
        int bConstrains = (8 - constraints[0][bRow].size()) + (8 - constraints[1][bCol].size()) + (8 - constraints[2][bBoxIndex].size());

        // Return this, which will allow the Priority Queue to sort from most constraining to least constraining
        // in order to break ties
        return bConstrains - aConstrains;

    }

    /**
     * Returns a PriorityQueue containing the values of a certain variable in the Sudoku puzzle,
     * sorted from least constraining to most constraining
     * @param indices the row and column indices of the given element
     * @param availabilitySets the 9x9 matrix of HashSets that contain the availability sets of each element in the Sudoku puzzle
     * @return <code>PriorityQueue</code> containing the values of this element sorted from least constraining to most constraining
     */
    private static PriorityQueue<int[]> leastConstrainingValue(int[] indices, HashSet<Integer>[][] availabilitySets){

        // Obtain the row and column indices for the current element
        int rowIndex = indices[0];
        int colIndex = indices[1];

        // Obtain the current element's availability set
        HashSet<Integer> currAvailabilitySet = availabilitySets[rowIndex][colIndex];

        // Use this HashMap to track how many variables each value in this availability
        // set is also found in other availability sets
        HashMap<Integer,Integer> freq = new HashMap<>();

        int boxRow = rowIndex / 3;
        int boxCol = colIndex / 3;
        for(int num : currAvailabilitySet) {
            // Iterate through the box that this element is in
            for (int k = 3*boxRow; k < 3 * boxRow + 3; k++) {
                for (int l = 3*boxCol; l < 3 * boxCol + 3; l++) {
                    // if the element contains this value in its availability set, then we
                    // increment the counter for this value.
                    if(availabilitySets[k][l].contains(num)){
                        freq.put(num, freq.getOrDefault(num,0) + 1);
                    }
                }
            }

            // Iterate through the column that this element is in
            for (int k = 0; k < 9; k++) {
                // if the element contains this value in its availability set, then we
                // increment the counter for this value.
                if(availabilitySets[k][colIndex].contains(num)){
                    freq.put(num, freq.getOrDefault(num,0) + 1);
                }
            }

            // Iterate through the row that this element is in
            for (int k = 0; k < 9; k++) {
                // if the element contains this value in its availability set, then we
                // increment the counter for this value.
                if(availabilitySets[rowIndex][k].contains(num)){
                    freq.put(num, freq.getOrDefault(num,0) + 1);
                }
            }
        }

        // Create a Priority Queue that will sort as a minheap based on least constraining values
        PriorityQueue<int[]> values = new PriorityQueue<>((a,b) -> a[1] - b[1]);

        // Add all the HashMap elements into the PriorityQueue
        for(Map.Entry<Integer,Integer> entry : freq.entrySet()){
            values.offer(new int[]{entry.getKey(), entry.getValue()});
        }

        return values;


    }

    /**
     * Performs DFS on the 9x9 Sudoku puzzle and also utilizes constraint propagation
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     * @param oldAvailabilitySets the 9x9 matrix of HashSets that contain the availability sets of each element in the Sudoku puzzle
     * @param oldConstraints a 3x9 matrix of HashSets containing the constraining values for each row, column, and box
     * @return true if DFS is complete and the Sudoku is solved, false otherwise
     */
    private static boolean dfs(int[][] sudoku, HashSet<Integer>[][] oldAvailabilitySets, HashSet<Integer>[][] oldConstraints){

        // Obtain the remaining variables in the order of the most constrained to the least constrained
        PriorityQueue<int[]> constrained = mostConstrainedVariable(sudoku, oldAvailabilitySets, oldConstraints);

        // Iterate over all the variables in the given order
        while(!constrained.isEmpty()){

            // Obtain the index of the current element
            int[] currIndex = constrained.poll();
            int i = currIndex[0];
            int j = currIndex[1];

            // Obtain the values of this element in the order of least constraining to most constraining
            PriorityQueue<int[]> values = leastConstrainingValue(currIndex,oldAvailabilitySets);
            for (int[] num : values) {

                // Set the element's value to this current value from its availability set
                sudoku[i][j] = num[0];


                // Create a deep copy of the current availability set and constraints
                HashSet<Integer>[][] newAvailabilitySets = copyAvailabilitySets(oldAvailabilitySets);
                HashSet<Integer>[][] newConstraints = copyConstraints(oldConstraints);

                // Add the newly assigned value to the constraints for its row, column, and box
                newConstraints[0][i].add(num[0]);
                newConstraints[1][j].add(num[0]);

                int boxRow = i / 3;
                int boxCol = j / 3;
                int boxIndex = boxRow*3 + boxCol;
                newConstraints[2][boxIndex].add(num[0]);


                // Run constraint propagation on this element and its new value.
                // If it returns false, then this value won't work, so we must try a different
                // value.
                if(!constraintPropagation(9*i + j, num[0], newAvailabilitySets, newConstraints, sudoku)){
                    continue;
                }

                // If constraint propagation worked, then we will call DFS on the next element.
                // If DFS returned true, then that means DFS was able to successfully complete
                // the search, so we return true.
                if (dfs(sudoku, newAvailabilitySets, newConstraints)) {
                    return true;
                }

            }

            // If current unassigned element was not able to assign a new value, then we
            // must backtrack, so we set this element back to 0 and return false.
            sudoku[i][j] = 0;
            return false;

        }

        // Return true if the DFS search is complete (only happens if we finish the for-loop
        // which means we have gone through every element in the 9x9 Sudoku matrix).
        return true;

    }

    /**
     * This method creates a deep copy of the availability sets. When calling DFS to enter
     * another state, we need to pass in a copy of the current state. So, we copy our current
     * availability sets to create new ones, and then we pass those new ones into the next DFS call.
     * @param oldAvailabilitySets the 9x9 matrix of HashSets that contain the availability sets of each element in the Sudoku puzzle
     * @return a deep copy of the availability sets that were passed in as input
     */
    private static HashSet<Integer>[][] copyAvailabilitySets(HashSet<Integer>[][] oldAvailabilitySets) {
        // The new 9x9 matrix of availability sets
        HashSet<Integer>[][] newAvailabilitySets = new HashSet[9][9];

        // Traverse all elements of the input and add all the elements into our copy.
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                newAvailabilitySets[i][j] = new HashSet<>();
                for(int num : oldAvailabilitySets[i][j]){
                    newAvailabilitySets[i][j].add(num);
                }
            }
        }

        // Return the copy
        return newAvailabilitySets;

    }

    /**
     * This method creates a deep copy of the constraint sets. When calling DFS to enter
     * another state, we need to pass in a copy of the current state. So, we copy our current
     * constraint sets to create new ones, and then we pass those new ones into the next DFS call.
     * @param oldConstraints a 3x9 matrix of HashSets containing the constraining values for each row, column, and box
     * @return a deep copy of the constraints that were passed in as input
     */
    private static HashSet<Integer>[][] copyConstraints(HashSet<Integer>[][] oldConstraints){
        // The new 3x9 matrix of constraint sets
        HashSet<Integer>[][] newConstraints = new HashSet[3][9];

        // Traverse all elements of the input and add all the elements into our copy.
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                newConstraints[i][j] = new HashSet<>();
                for(int num : oldConstraints[i][j]){
                    newConstraints[i][j].add(num);
                }
            }
        }

        // Return the copy
        return newConstraints;

    }

    /**
     * Checks to see if a Sudoku has been solved or not
     *
     * @param sudoku a 9x9 matrix that contains the Sudoku puzzle
     * @return true if the Sudoku puzzle has been correctly solved, false otherwise
     */
    public static boolean checkSudoku(int[][] sudoku){

        // Three 9x9 matrices.
        // The first 9x9 matrix: each row corresponds to each
        // row in the Sudoku puzzle, each column corresponds to
        // values 1 to 9. If a value exists in a row, then
        // it's corresponding value will be updated to true.

        // The second 9x9 matrix: each row corresponds to each
        // column in the Sudoku puzzle, each column corresponds to
        // values 1 to 9. If a value exists in a column, then
        // it's corresponding value will be updated to true.

        // The third 9x9 matrix: each row corresponds to each
        // box in the Sudoku puzzle, each column corresponds to
        // values 1 to 9. If a value exists in a box, then
        // it's corresponding value will be updated to true.

        boolean[][][] exists = new boolean[3][9][9];

        // General Idea: traverse over all rows, columns, and boxes. If
        // there is an unassigned element (its value is 0), or a value
        // has already been marked in its row, column, or box (if it is
        // marked true in the "exists" matrix), then you return false
        // because it means the Sudoku is either incomplete or incorrect.

        // We traverse each row and column at the same time with this, and
        // we mark the values accordingly.
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

        // We traverse each box with this and we mark the values accordingly.
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

        // If we haven't return false yet, then it must mean the Sudoku is correct,
        // so we return true.
        return true;

    }

}
