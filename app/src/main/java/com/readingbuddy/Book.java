package com.readingbuddy;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Book
{
    public enum State
    {
        TO_READ(0),CURRENTLY_READING(1), FINISHED(2), DNF(3);

        private int i;

        State(int i)
        {
            this.i = i;
        }
        public int getInt()
        {
            return i;
        }
        public static State fromInteger(int x) {
            switch(x) {
                case 0:
                    return TO_READ;
                case 1:
                    return CURRENTLY_READING;
                case 2:
                    return FINISHED;
                case 3:
                    return DNF;
            }
            return null;
        }
        //same as res/values/strings/book_states
    };

    public String Title;
    public long RandomId;
    public String Author;
    public String CoverPath;
    public State CurrentState;
    public int TotalPages;
    public int Rating; //0-10 display as [0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5]
    public ArrayList<readMoment> readMoments = new ArrayList<readMoment>();


    public Book(String Title, long RandomId, String Author, String CoverPath, State CurrentState, int TotalPages, int Rating)
    {
        this.Title = Title;
        this.RandomId = RandomId;
        this.Author = Author;
        this.CoverPath = CoverPath;
        this.CurrentState = CurrentState;
        this.TotalPages = TotalPages;
        this.Rating = Rating;
    }

    public Book(String serialBook)
    {
        try {
            Title = serialBook.substring(serialBook.indexOf("[") + 1, serialBook.indexOf(","));
            serialBook = serialBook.substring(serialBook.indexOf(",", 1));
            RandomId = Long.parseLong(serialBook.substring(serialBook.indexOf(",") + 1, serialBook.indexOf(",", 1)));
            serialBook = serialBook.substring(serialBook.indexOf(",", 1));
            Author = serialBook.substring(serialBook.indexOf(",") + 1, serialBook.indexOf(",", 1));
            serialBook = serialBook.substring(serialBook.indexOf(",", 1));
            CoverPath = serialBook.substring(serialBook.indexOf(",") + 1, serialBook.indexOf(",", 1));
            serialBook = serialBook.substring(serialBook.indexOf(",", 1));
            CurrentState = State.valueOf(serialBook.substring(serialBook.indexOf(",") + 1, serialBook.indexOf(",", 1)));
            serialBook = serialBook.substring(serialBook.indexOf(",", 1));
            TotalPages = Integer.parseInt(serialBook.substring(serialBook.indexOf(",") + 1, serialBook.indexOf(",", 1)));
            serialBook = serialBook.substring(serialBook.indexOf(",", 1));
            Rating = Integer.parseInt(serialBook.substring(serialBook.indexOf(",") + 1, serialBook.indexOf(",", 1)));
            serialBook = serialBook.substring(serialBook.indexOf(",", 1));

            String allReadMoments =serialBook.substring(serialBook.indexOf(",") + 1, serialBook.indexOf("]"));
            while (allReadMoments.length() > 0) // "]"
            {
                String readMomentStr = allReadMoments.substring(allReadMoments.indexOf("{"), allReadMoments.indexOf("}") + 1);
                allReadMoments = allReadMoments.substring(allReadMoments.indexOf("}") + 1);

                readMoments.add(new readMoment(readMomentStr));
            }
        } catch (Exception e)
        {
            //THIS IS NOT A BOOK
        }
    }

    public String serialize()
    {
        if (CurrentState == null)
        {
            CurrentState = State.TO_READ;
        }
        String bookStr = ("[" + Title + "," +
                RandomId + "," +
                Author + "," +
                CoverPath + "," +
                CurrentState.name() + "," +
                TotalPages + "," +
                Rating + ",");

        for (int i = 0 ; i < readMoments.size(); i ++)
        {
            bookStr += readMoments.get(i).serialize();
        }

        bookStr  += "]";
        return bookStr;
    }

    public void addReadMoment(readMoment r)
    {
        readMoments.add(r);
    }

    public ArrayList<readMoment> getMoments()
    {
        return readMoments;
    }

    public void removeMoment(readMoment r)
    {
        readMoments.remove(r);
    }

    public readMoment editReadMoment(readMoment oldReadMoment, readMoment newReadMoment)
    {
        int index = readMoments.indexOf(oldReadMoment);
        readMoments.set(index, newReadMoment);
        return readMoments.get(index);
    }

    public String getLastReadDate()
    {
        if (readMoments.size() < 1)
        {
            return "No date";
        }
        return (readMoments.get(readMoments.size() -1)).readDate;
    }

    public String getStartDate()
    {
        if (readMoments.size() < 1)
        {
            return "No date";
        }
        return (readMoments.get(0)).readDate;
    }



    public readMoment getLastSession()
    {
        if (readMoments.size() < 1)
        {
            return null;
        }
        return (readMoments.get(readMoments.size() -1));
    }


    public int getPagesOnDate(String Date)
    {
        int pages = 0;
        for (int i = 0; i < readMoments.size(); i++)
        {
            readMoment mom = readMoments.get(i);
            if (mom.readDate.contains(Date))
            {
                pages += readMoments.get(i).readPages;
            }
        }
        return pages;
    }


    public int getTimeOnDate(String Date)
    {
        int time = 0;
        for (int i = 0; i < readMoments.size(); i++)
        {
            readMoment mom = readMoments.get(i);
            if (mom.readDate.contains(Date))
            {
                time += readMoments.get(i).readTime;
            }
        }
        return time;
    }

    public int getPages()
    {
        int pages = 0;
        for (int i = 0; i < readMoments.size(); i++)
        {
            pages += readMoments.get(i).readPages;
        }
        return pages;
    }

    public double getAverigePPM()
    {
        int pages = getPages();
        long time = 0;
        for (int i = 0; i < readMoments.size(); i++)
        {
            readMoment mom = readMoments.get(i);
            time += mom.readTime;
        }
        double min = ((double)time) / 60.0;
        double returnval = pages / min;

        if (Double.isNaN(returnval))
        {
            return 0;
        }
        return returnval;
    }


    public String getTotalReadTime()
    {
        long readTime = 0;
        for (int i = 0; i < readMoments.size(); i++)
        {
            readTime += readMoments.get(i).readTime;
        }

        long hours = readTime / 60 / 60;
        int minutes = (int) (readTime / 60) % 60;
        int seconds = (int) readTime % 60;

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
}
