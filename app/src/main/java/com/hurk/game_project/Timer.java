package com.hurk.game_project;

/* Code base taken from course literature */
public class Timer {
    private long start;
    private long stopWatchStart;

    public Timer() {
        start = System.currentTimeMillis();
        stopWatchStart = 0;
    }

    public long getElapsed() {
        return System.currentTimeMillis() - start;
    }

    public void rest(int ms) {
        long startTime = getElapsed();
        while (startTime + ms > getElapsed()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    public void resetStopWatch() {
        stopWatchStart = getElapsed();
    }

    public boolean stopWatch(long ms) {
        if (getElapsed() > stopWatchStart + ms) {
            resetStopWatch();
            return true;
        } else {
            return false;
        }
    }
}
