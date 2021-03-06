package Utilities;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;

public class Stopwatch{
    protected long startTime;
    private ArrayList<Long> laps;
    private long stopTime;
    private boolean stopped = false;
    private final boolean onlyMilliseconds;
    private long lastMeasure; //Used to look up if there is more then a second passed;

    public Stopwatch(boolean onlyMilliseconds){
        startTime = now();
        laps = new ArrayList<>();
        this.onlyMilliseconds = onlyMilliseconds;
        this.lastMeasure = now();
    }
    public Stopwatch(){
        this(true);
    }

    public Object getLap(){
        long current = now() - startTime;
        laps.add(current);
        long lap;
        if(laps.size() > 1) lap = current - laps.get(laps.size()-2);
        else lap = current;
        return onlyMilliseconds ? lap : getTime(lap);
    }

    public Object stopTime(){
        if(!hasStopped()) {
            stopTime = now();
            stopped = true;
        }
        long time = stopTime - startTime;
        laps.add(time);
        return onlyMilliseconds ? time : getTime(time);
    }

    public long getTime(){
        return hasStopped() ? stopTime - startTime : now() - startTime;
    }

    public void resetTime(){
        startTime = now();
    }

    /**
     * returns time in a easy to read format
     * @param time time to translate
     * @return String with days, hours, minutes and seconds
     */
    public static String getTime(long time){
        final int second = 1000;
        final int minute = second*60;
        final int hour = minute*60;
        final int day = hour*24;

        long days = time/day;
        long hours = (time - day*days)/hour;
        long minutes = (time - day*days - hour*hours)/minute;
        long seconds = (time - day*days - hour*hours - minute*minutes)/second;
        long milliseconds = time - day*days - hour*hours - minute*minutes - second*seconds;
        long deciseconds = milliseconds/10;
        String dm = deciseconds != 0 ? deciseconds > 9 ? "." + deciseconds : ".0" + deciseconds : "";

        StringBuilder sb = new StringBuilder();
        if(seconds > 0){
            sb.append(seconds).append(dm).append("s ");
            if(minutes > 0){
                sb.insert(0, minutes + "min " );
                if(hours > 0){
                    sb.insert(0, hours + "h " );
                    if(days > 0){
                        sb.insert(0, days + "d " );
                    }
                }
            }
        } else sb.append(milliseconds).append(" milliseconds");

        return sb.toString().trim();
    }

    public ArrayList<Long> getLaps(){ return laps; }


    public boolean isSecondPassed(){
        return isSecondPassed(1);
    }
    public boolean isSecondPassed(int factor){
        return isXMilliSecondPassed(factor, 1000);
    }

    protected long now(){
        return System.currentTimeMillis();
    }

    public boolean isXMilliSecondPassed(int factor, int x) {
        if(factor != 0 && x > 0) {
            if ((now() - lastMeasure) >= x / factor) {
                lastMeasure = now();
                return true;
            }
        }
        return false;
    }

    public boolean hasStopped(){ return stopped; }
}
