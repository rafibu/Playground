package Sudoku;

import java.util.Arrays;
import java.util.Random;

import static Utilities.MathUtilities.getOneAtRandom;

public class SudokuGenerator {

    public SudokuField generateEasy(){ return generate(40); }
    public SudokuField generateMedium(){ return generate(30); }
    public SudokuField generateHard(){ return generate(24); }
    public SudokuField generateImpossible(){ return generate(17); }

    private SudokuField generate(int maxFilled) {
        SudokuField field = lasVegasFill();
        //TODO: make better algorithm to get to 17
        if(removeRandom(field, maxFilled)) {
            Arrays.stream(field.getSquares()).forEach(sq -> {
                if(sq.getDefinitiveNumber() != null) sq.setInitial(true);
            });
            return field;
        } else {
            return new SudokuField();
        }
    }

    /**
     * extends fillRandom to a Las Vegas algorithm
     * @return filled field
     */
    private SudokuField lasVegasFill() {
        SudokuField field;
        do {
            field = new SudokuField();
            fillRandom(field);
            field.solveField();
        } while(!field.isFinished());
        return field;
    }

    /**
     * Monte Carlo Algorithm to fill a SudokuField with random numbers
     * field is completely filled in approx. 18.4% of the cases
     * @param field field to fill
     */
    private void fillRandom(SudokuField field){
        SudokuSquare[] squares = field.getSquares();
        for(SudokuSquare square: squares) {
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    if (field.getNumber(x, y) == null && field.getPossibilities(x, y).size() == 1) {
                        field.setNumber(field.getPossibilities(x, y).get(0).getNumber() + "", x, y);
                    }
                }
            }
            SNumber number = ((SNumber) getOneAtRandom(square.getPossibilities().toArray()));
            square.setDefinitiveNumber(number);
        }
    }

    /**
     * removes random tiles until at least it has less then maxFilled
     * @param field field to alternate
     * @param maxFilled number of tiles which still can be filled
     * @return true if removal was successful, false if couldn't remove as much
     */
    private boolean removeRandom(SudokuField field, int maxFilled){
        Random random = new Random();
        SudokuField copy = new SudokuField();
        copy.copy(field);
        int tries = 0;
        int multiplicator = 10;
        while(field.getFilled() > maxFilled) {
            if(tries > maxFilled*multiplicator){
                field.copy(copy);
                tries = 0;
                if(multiplicator > 100) return false;
                multiplicator *= 2;
            }
            SNumber number;
            SudokuSquare square;
            do {
                do {
                    square = field.getSquare(random.nextInt(9), random.nextInt(9));
                } while (square.getDefinitiveNumber() == null);
                number = square.getDefinitiveNumber();
                square.setDefinitiveNumber(null);
            } while (field.isSolvable());
            square.setDefinitiveNumber(number);
            tries++;
        }
        return true;
    }
}
