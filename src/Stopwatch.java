import java.util.ArrayList;

public class Stopwatch {
    private long startTime;
    private ArrayList<Long> laps;
    private long stopTime;
    private final boolean onlyMilliseconds;

    public Stopwatch(boolean onlyMilliseconds){
        startTime = now();
        laps = new ArrayList<>();
        this.onlyMilliseconds = onlyMilliseconds;
    }
    public Stopwatch(){
        this(true);
    }

    public Object getLap(){
        long current = now() - startTime;
        laps.add(current);
        long lap;
        if(laps.size() > 1) lap = current - laps.get(1);
        else lap = current;
        return onlyMilliseconds ? lap : getTime(lap);
    }

    public Object stopTime(){
        stopTime = now();
        long time = stopTime - startTime;
        laps.add(time);
        return onlyMilliseconds ? time : getTime(time);
    }

    public void resetTime(){
        startTime = now();
    }

    /**
     * returns time in a easy to read format
     * @param time time to translate
     * @return String with days, hours, minutes and seconds
     */
    public String getTime(long time){
        int second = 1000;
        int minute = second*60;
        int hour = minute*60;
        int day = hour*24;

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

    private long now(){
        return System.currentTimeMillis();
    }
}
