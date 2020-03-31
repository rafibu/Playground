package Utilities;

import Utilities.Stopwatch;
import org.junit.Test;

public class StopwatchTest {
    @Test
    public void stopwatchQuickTest(){
        Stopwatch stopwatch1 = new Stopwatch(false);
        Stopwatch stopwatch2 = new Stopwatch();

        try {
            Thread.sleep(10*1000);
        } catch (Exception e){}
        String firstLap1 = (String)stopwatch1.getLap();
        long firstLap2 = (long)stopwatch2.getLap();
        try {
            Thread.sleep(60*1000);
        } catch (Exception e){}
        String secondLap1 = (String)stopwatch1.getLap();
        long secondLap2 = (long)stopwatch2.getLap();
        try {
            Thread.sleep(60*1000 + 10);
        } catch (Exception e){}
        String thirdLap1 = (String)stopwatch1.stopTime();
        long thirdLap2 = (long)stopwatch2.getLap();
        stopwatch2.resetTime();
        try {
            Thread.sleep(10);
        } catch (Exception e){}
        long lastLap = (long)stopwatch2.stopTime();
        assert thirdLap2 > lastLap;
        assert stopwatch1.getLaps().size() == 3;
        stopwatch1.getLaps().forEach(l -> System.out.println(stopwatch1.getTime(l)));
        assert firstLap1.equals("10s");
        assert thirdLap1.contains("2min 10");
        assert !secondLap1.equals(String.valueOf(secondLap2));
    }
}