package Utilities;



import java.util.ArrayList;
import java.util.Random;

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

    public static <T> boolean isInList(T obj, ArrayList<T> list) { return isIn(obj, list.toArray()); }

    public static <T> boolean isIn(T obj, T... list) {
        if(list != null) {
            for (T ele : list) {
                if(obj == ele) return true;
            }
        }
        return false;
    }

    public static <T> T getOneAtRandom(T... objects){
        Random random = new Random();
        if(objects.length > 0) {
            return objects[random.nextInt(objects.length)];
        } else {
            return null;
        }
    }
}
