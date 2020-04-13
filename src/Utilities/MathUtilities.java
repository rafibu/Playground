package Utilities;


import java.util.ArrayList;

public class MathUtilities {

    public static boolean isBetween(int number, int bound1, int bound2){
        return isBetween((float)number, (float)bound1, (float) bound2);
    }

    public static boolean isBetween(float number, float bound1, float bound2){
        return (number <= bound1 && number >= bound2) || (number >= bound1 && number <= bound2);
    }

    public static boolean probability(double prob){
        assert prob <= 1;
        return prob >= Math.random();
    }

    public static boolean isInList(Object obj, ArrayList<Object> list) {
        return isIn(obj, list.toArray());
    }

    public static boolean isIn(Object obj, Object... list) {
        if(list != null) {
            for (Object ele : list) {
                if(obj == ele) return true;
            }
        }
        return false;
    }
}
