package com.readingbuddy;
import android.os.Handler;

import com.readingbuddy.secondPassedTimer;

public class readingTimer
{
    private boolean running = false;
    private long runningTotalTime = 0;
    private long lastStartEpochTime = 0;

    private Handler myHandler;
    private secondPassedTimer secondFunc;
    private Runnable timerTask;

    public readingTimer()
    {
        myHandler = new Handler();
    }

    public long getTime()
    {
        if (running)
        {
            long time = runningTotalTime + System.currentTimeMillis() - lastStartEpochTime;
            return time /1000;
        }
        return runningTotalTime / 1000;
    }

    public String getTimeStr() {
        long time = getTime();

        long hours = time / 60 / 60;
        int minutes = (int) (time / 60) % 60;
        int seconds = (int) time % 60;

        String readTimeStr = "";
        if (hours < 10)
        {
            readTimeStr += "0";
        }readTimeStr += hours + ":";
        if (minutes < 10)
        {
            readTimeStr += "0";
        }readTimeStr += minutes + ":";
        if (seconds < 10)
        {
            readTimeStr += "0";
        }readTimeStr += seconds;
        return readTimeStr;
    }

    public void start()
    {
        timerTask = new Runnable() {
            @Override
            public void run() {

                if (secondFunc != null)
                {
                    secondFunc.onSecondPassed();
                }
                if (running)
                {
                    myHandler.postDelayed(timerTask, 50);
                }
            }
        };

        if (!running)
        {
            myHandler.removeCallbacks(timerTask);
            myHandler.postDelayed(timerTask, 50);
            lastStartEpochTime = System.currentTimeMillis();
            running = true;

            secondFunc.onSecondPassed();
        }
    }

    public void stop()
    {
        runningTotalTime = 0;
        lastStartEpochTime = 0;
        running = false;
        myHandler.removeCallbacks(timerTask);
    }

    public void pause()
    {
        running = false;

        long currentEpochTime = System.currentTimeMillis();
        runningTotalTime += currentEpochTime - lastStartEpochTime;

        myHandler.removeCallbacks(timerTask);
        secondFunc.onSecondPassed();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setTimerPassedFunction(secondPassedTimer func)
    {
        secondFunc = func;
    }
}
