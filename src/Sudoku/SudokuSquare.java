package Sudoku;

import java.util.ArrayList;

public class SudokuSquare {
    private SNumber definitiveNumber;
    private boolean initial;
    private final int x;
    private final int y;
    private final SudokuField field;

    public SudokuSquare(SudokuField field, int x, int y){
        this.field = field;
        this.x = x;
        this.y = y;
    }

    public SNumber getDefinitiveNumber() { return definitiveNumber; }
    public void setDefinitiveNumber(SNumber definitiveNumber) { this.definitiveNumber = definitiveNumber; }

    private boolean isFinal() {
        return getDefinitiveNumber() != null;
    }

    public boolean canSolve(){
        return !isFinal() && field.getPossibilities(x,y).size() == 1;
    }

    public SNumber solveObvious(){
        if(field.getPossibilities(x,y).size() == 1) {
            SNumber solution = field.getPossibilities(x, y).remove(0);
            setDefinitiveNumber(solution);
            return solution;
        }
        return null;
    }

    public ArrayList<SNumber> getPossibilities(){ return getDefinitiveNumber() == null ? field.getPossibilities(getX(), getY()) : new ArrayList<>(); }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isInitial() { return initial; }
    public void setInitial(boolean initial) { this.initial = initial; }
}
