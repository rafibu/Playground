package Sudoku;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuField {
    private SudokuSquare[][] field;
    private boolean firstTry;
    private Difficulty difficulty;

    public SudokuField(){
        initzalizeField();
    }

    private void initzalizeField() {
        field = new SudokuSquare[9][9];
        for(int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                field[i][j] = new SudokuSquare(this, i, j);
            }
        }
    }

    private void setNumber(SNumber number, int x, int y){
        field[x][y].setDefinitiveNumber(number);
    }

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

    public void solveField(boolean bruteForce){
        while(!isFinished()){
            int i = 0;
            for (SudokuSquare[] row : field) {
                for (SudokuSquare square : row) {
                    if(solveSquare(square)) {
                        i++;
                    }
                }
            }
            if(i == 0) break;
        }
        firstTry = true;
    }

    private boolean solveSquare(SudokuSquare square){
        return square.getDefinitiveNumber() == null && (square.solveObvious() != null || solvePacks(square.getX(), square.getY()));
    }


    public void solveOne() {
        for (SudokuSquare[] row : field) {
            boolean br = false;
            for (SudokuSquare square : row) {
                if(solveSquare(square)){
                    br = true;
                    break;
                }
            }
            if(br) break;
        }
    }

    private boolean solvePacks(int x, int y) {
        SudokuSquare[] row = new SudokuSquare[9];
        SudokuSquare[] vertical = new SudokuSquare[9];
        for(int i = 0; i < 9; i++) {
            row[i] = field[i][y];
            vertical[i] = field[x][i];
        }
        if(checkOnePossibleSquare(row)) return true;
        if(checkOnePossibleSquare(vertical)) return true;
        return solveCompactSquare(x, y);
    }

    private boolean solveCompactSquare(int x, int y) {
        SudokuSquare[] squares = new SudokuSquare[9];
        int s = 0;
        int sx = x/3;
        int sy = y/3;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                squares[s] = field[sx*3 +i][sy*3+j];
                s++;
            }
        }
        return checkOnePossibleSquare(squares);
    }

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

    boolean isFinished(){
        for(SudokuSquare[] row: field){
            for(SudokuSquare square: row){
                if(square.getDefinitiveNumber() == null) return false;
            }
        }
        return true;
    }

    private void bruteForceField(){
        //TODO: Write Bruteforce solving Algorithm
    }

    public boolean isFirstTry() { return firstTry; }

    public SNumber getNumber(int x, int y){
        return field[x][y].getDefinitiveNumber();
    }

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

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public void copy(SudokuField toCopy){
        initzalizeField();
        this.difficulty = toCopy.getDifficulty();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                setNumber(toCopy.getNumber(i,j), i, j);
            }
        }
    }

    public SudokuSquare getSquare(int x, int y) {
        return field[x][y];
    }

    public boolean isSolvable(){
        SudokuField copy = new SudokuField();
        copy.copy(this);
        copy.solveField(false);
        return copy.isFinished();
    }

    public SudokuSquare[] getSquares(){
        ArrayList<SudokuSquare> squares = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                squares.add(this.getSquare(i, j));
            }
        }
        return squares.toArray(new SudokuSquare[9*9]);
    }

    public int getFilled() {
        int res = 0;
        for(SudokuSquare square: getSquares()){
            if(square.getDefinitiveNumber() != null) res++;
        }
        return res;
    }
}
