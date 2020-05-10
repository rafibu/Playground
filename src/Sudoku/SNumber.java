package Sudoku;

public enum SNumber {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9);
    private int num;
    SNumber(int num){
        this.num = num;
    }

    public int getNumber() { return num; }

    public static SNumber getNumber(int i){
        switch (i){
            case 1: return ONE;
            case 2: return TWO;
            case 3: return THREE;
            case 4: return FOUR;
            case 5: return FIVE;
            case 6: return SIX;
            case 7: return SEVEN;
            case 8: return EIGHT;
            case 9: return NINE;
            default: throw new IllegalStateException("Number not between 0-9");
        }
    }
}
