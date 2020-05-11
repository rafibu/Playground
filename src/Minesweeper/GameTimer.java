package Minesweeper;

import Utilities.Stopwatch;

public class GameTimer extends Stopwatch {

    GameTimer(){
        super(false);
    }

    public String getTime(){
        long time = now() - startTime;
        final int second = 1000;
        final int minute = second*60;

        long min = time/minute;
        long sec = (time - minute*min)/second;

        String m = min > 9 ? min +"": "0" + min;
        String s = sec > 9 ? sec +"": "0" + sec;

        return m + ":" + s;
    }
}
