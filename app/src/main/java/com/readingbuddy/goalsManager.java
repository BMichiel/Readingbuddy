package com.readingbuddy;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class goalsManager {

    private static final String FILE_NAME = "Goals.txt";
    private Context currentContext;

    public int YearBooks;
    public int YearPages;
    public int MonthBooks;
    public int MonthPages;
    public int DailyPages;

    public int notificationTime;
    public boolean notifyMonday;
    public boolean notifyTuesday;
    public boolean notifyWednesday;
    public boolean notifyThursday;
    public boolean notifyFriday;
    public boolean notifySaturday;
    public boolean notifySunday;

    public enum GOALS{YR_BOOKS, YR_PAGES, MT_BOOKS, MT_PAGES, DY_PAGES}


    public goalsManager(Context currentContext)
    {
        this.currentContext = currentContext;
        FileInputStream fis = null;
        try
        {
            fis = currentContext.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String GoalsStr;
            if ((GoalsStr = br.readLine()) != null) //Only read one line
            {
                deserialize(GoalsStr);
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String timeToStr(int time)
    {
        int timeHr = (time / 3600) % 24;
        int timeMn = (time / 60) % 60;
        int timeSc = time % 60;
        String timeStr = "";
        if (timeHr < 10) {timeStr += "0";}
        timeStr += timeHr + ":";
        if (timeMn < 10) {timeStr += "0";}
        timeStr += timeMn + ":";
        if (timeSc < 10) {timeStr += "0";}
        timeStr += timeSc;

        return timeStr;
    }


    public int timeFromStr(String timeStr)
    {
        try {
            int timeHr = Integer.parseInt(timeStr.substring(0, timeStr.indexOf(":")));
            timeStr = timeStr.substring(timeStr.indexOf(":") + 1);
            int timeMn = Integer.parseInt(timeStr.substring(0, timeStr.indexOf(":")));
            timeStr = timeStr.substring(timeStr.indexOf(":") + 1);
            int timeSc = Integer.parseInt(timeStr);

            return (timeHr * 3600) + (timeMn * 60) + timeSc;
        }catch (Exception e){}
        return 0;
    }

    private void deserialize(String serializedGoals)
    {
        try {
            YearBooks = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1)));
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            YearPages = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1)));
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            MonthBooks = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1)));
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            MonthPages = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1)));
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            DailyPages = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1)));
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);

            notificationTime = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1)));
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);

            notifyMonday = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1))) == 1;
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            notifyTuesday = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1))) == 1;
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            notifyWednesday = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1))) == 1;
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            notifyThursday = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1))) == 1;
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            notifyFriday = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1))) == 1;
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            notifySaturday = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1))) == 1;
            serializedGoals = serializedGoals.substring(serializedGoals.indexOf("-") + 1);
            notifySunday = Integer.parseInt(serializedGoals.substring(0, serializedGoals.indexOf("-", 1))) == 1;

        } catch (Exception e)
        {
            //THIS IS NOT A BOOK
        }
    }

    private String serialize()
    {
        return  YearBooks + "-" +
                YearPages + "-" +
                MonthBooks + "-" +
                MonthPages + "-" +
                DailyPages + "-" +
                notificationTime + "-" +
                (notifyMonday ? 1 : 0) + "-" +
                (notifyTuesday ? 1 : 0) + "-" +
                (notifyWednesday ? 1 : 0) + "-" +
                (notifyThursday ? 1 : 0) + "-" +
                (notifyFriday ? 1 : 0) + "-" +
                (notifySaturday ? 1 : 0) + "-" +
                (notifySunday ? 1 : 0) + System.lineSeparator();

    }

    public void saveGoals()
    {
        FileOutputStream fos = null;
        try
        {
            fos = currentContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(serialize().getBytes());

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
