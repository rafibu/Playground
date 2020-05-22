package Sudoku;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuField {
    private SudokuSquare[][] field;
    private Difficulty difficulty;

    public SudokuField(){
        initalizeField();
    }

    /**
     * checks the field until one square is solved
     */
    public void solveOne() {
        for(SudokuSquare square: getSquares()){
            if(solveSquare(square)) break;
        }
    }

    /**
     * tries to set the number, only does so if it's a possible number
     * @return true if number could be set
     */
    public boolean setNumber(String number, int x, int y){
        if(number.matches("[1-9]")) {
            int i = Integer.parseInt(number);
            while (i > 9) i = i % 10;
            SNumber si = SNumber.getNumber(i);
            if(getPossibilities(x, y).contains(si)) {
                setNumber(si, x, y);
                return true;
            }
        } else if (number.equals("")){
            setNumber((SNumber)null, x, y);
            return true;
        }
        return false;
    }

    /**
     * solves whole field if possible, stops if no field can be solved anymore
     */
    public void solveField(){
        while(!isFinished()){
            int i = 0;
            for (SudokuSquare square : getSquares()) {
                if(solveSquare(square)) {
                    i++;
                }
            }
            if(i == 0) break;
        }
    }

    /**
     * checks if the square can be solved and solves it
     * @return true if one square (might not be this one) is solved
     */
    private boolean solveSquare(SudokuSquare square){
        return square.getDefinitiveNumber() == null && (square.solveObvious() != null || solvePacks(square.getX(), square.getY()));
    }

    /**
     * solves the vertical and horizontal rows in which square x,y is in
     * @return true if one has been solved
     */
    private boolean solvePacks(int x, int y) {
        SudokuSquare[] row = new SudokuSquare[9];
        SudokuSquare[] vertical = new SudokuSquare[9];
        for(int i = 0; i < 9; i++) {
            row[i] = getSquare(i,y);
            vertical[i] = getSquare(x,i);
        }
        if(checkOnePossibleSquare(row)) return true;
        if(checkOnePossibleSquare(vertical)) return true;
        return solveCompactSquare(x, y);
    }

    /**
     * solves the 3*3 field of squares in which square x,y is in
     * @return true if one has been solved
     */
    private boolean solveCompactSquare(int x, int y) {
        SudokuSquare[] squares = new SudokuSquare[9];
        int s = 0;
        int sx = x/3;
        int sy = y/3;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                squares[s] = getSquare(sx*3 +i, sy*3+j);
                s++;
            }
        }
        return checkOnePossibleSquare(squares);
    }

    /**
     * checks if one of the squares and solves it
     * @param squares squares to check
     * @return true if a square has been solved
     */
    private boolean checkOnePossibleSquare(SudokuSquare[] squares) {
        ArrayList<SNumber> possibilities = getPossibilities(squares);
        if(possibilities == null) return true;
        for(SNumber possibility: possibilities){
            int s = 0;
            for (SudokuSquare square: squares){
                if(square.getPossibilities().contains(possibility)){
                    s++;
                }
                if(s > 1) break;
            }
            if(s == 1){
                for (SudokuSquare square: squares) {
                    if (square.getPossibilities().contains(possibility)) {
                        square.setDefinitiveNumber(possibility);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * gets possibilities for an Array of squares
     * @param squares Array of squares
     * @return all still possible numbers in at least one of the squares, null if one has been solved
     */
    private ArrayList<SNumber> getPossibilities(SudokuSquare[] squares) {
        ArrayList<SNumber> possibilities = new ArrayList<>();
        ArrayList<SNumber> nonStarters = new ArrayList<>();
        for(SudokuSquare square: squares){
            if(square.getDefinitiveNumber() == null) {
                ArrayList<SNumber> newPoss = square.getPossibilities();
                if (newPoss.size() == 1) {
                    if (square.solveObvious() != null) return null;
                    else throw new IllegalStateException("Somethings wrong");
                }
                newPoss.forEach(p -> {
                    if(!possibilities.contains(p)) possibilities.add(p);
                });
            } else {
                nonStarters.add(square.getDefinitiveNumber());
            }
        }
        nonStarters.forEach(possibilities::remove);
        return possibilities;
    }

    /**
     * initalizes an empty field of squares
     */
    private void initalizeField() {
        field = new SudokuSquare[9][9];
        for(int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                field[i][j] = new SudokuSquare(this, i, j);
            }
        }
    }

    /**
     * @return Arraylist of all still possible numbers at square x,y
     * TODO: Set instead of Arraylist
     */
    public ArrayList<SNumber> getPossibilities(int x, int y) {
        ArrayList<SNumber> possibilities = new ArrayList<>(9);
        if(getNumber(x, y) == null) {
            possibilities.addAll(Arrays.asList(SNumber.values()));
            for (int i = 0; i < 9; i++) {
                possibilities.remove(getNumber(x, i));
                possibilities.remove(getNumber(i, y));
            }
            int sx = x / 3;
            int sy = y / 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    possibilities.remove(getNumber(sx * 3 + i, sy * 3 + j));
                }
            }
        }
        return possibilities;
    }

    /**
     * copies a fields parameter onto this
     * @param toCopy field which should be copied from
     */
    public void copy(SudokuField toCopy){
        initalizeField();
        this.difficulty = toCopy.getDifficulty();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                setNumber(toCopy.getNumber(i,j), i, j);
            }
        }
    }

    /**
     * checks if this field is solvable without ambiguity
     * @return true if field can be solved
     */
    public boolean isSolvable(){
        SudokuField copy = new SudokuField();
        copy.copy(this);
        copy.solveField();
        return copy.isFinished();
    }

    public boolean isFinished(){
        for(SudokuSquare[] row: field){
            for(SudokuSquare square: row){
                if(square.getDefinitiveNumber() == null) return false;
            }
        }
        return true;
    }

    /**
     * @return all squares of the field as an array
     */
    public SudokuSquare[] getSquares(){
        ArrayList<SudokuSquare> squares = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                squares.add(this.getSquare(i, j));
            }
        }
        return squares.toArray(new SudokuSquare[9*9]);
    }

    /**
     * @return number of already filled fields
     */
    public int getFilled() {
        int res = 0;
        for(SudokuSquare square: getSquares()){
            if(square.getDefinitiveNumber() != null) res++;
        }
        return res;
    }

    public SudokuSquare getSquare(int x, int y) {
        return field[x][y];
    }

    public SNumber getNumber(int x, int y){
        return getSquare(x,y).getDefinitiveNumber();
    }
    private void setNumber(SNumber number, int x, int y){
        getSquare(x,y).setDefinitiveNumber(number);
    }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public void restartField() {
        for(SudokuSquare square: getSquares()){
            if(!square.isInitial()) square.setDefinitiveNumber(null);
        }
    }
}
