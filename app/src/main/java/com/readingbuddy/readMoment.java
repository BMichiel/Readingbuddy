package com.readingbuddy;

public class readMoment
{
    public String readDate;
    public long readTime;
    public int readPages;

    public readMoment(String readDate, long readTime, int readPages)
    {
        this.readDate = readDate;
        this.readTime = readTime;
        this.readPages = readPages;
    }

    public readMoment(String serialBook)
    {
        try {
            readDate = serialBook.substring(serialBook.indexOf("{") + 1, serialBook.indexOf("|"));
            serialBook = serialBook.substring(serialBook.indexOf("|", 1));

            readTime = Long.parseLong(serialBook.substring(serialBook.indexOf("|") + 1, serialBook.indexOf("|", 1)));
            serialBook = serialBook.substring(serialBook.indexOf("|", 1));
            readPages = Integer.parseInt(serialBook.substring(serialBook.indexOf("|") + 1, serialBook.indexOf("}", 1)));
        } catch (Exception e)
        {
            //THIS IS NOT A reading moment
        }
    }

    public String serialize()
    {
        return ("{" + readDate + "|" +
                readTime + "|" +
                readPages + "}");
    }

    public String getTimeString()
    {
        String timeString = "";
        long Hr = readTime / 3600;
        int Mn = (int) (readTime / 60) % 60;
        int Sc = (int) readTime % 60;

        timeString += Hr + "h ";
        timeString += Mn + "m ";
        timeString += Sc + "s";
        return timeString;
    }

    public double getPPM()
    {
        double min = readTime / 60.0;
        if (min > 0 && readPages > 0)
        {
            return readPages / min;
        }
        return 0.0;
    }

    public double getMPP()
    {
        double min = readTime / 60.0;
        if (min > 0 && readPages > 0)
        {
            return min / readPages;
        }
        return 0.0;
    }

}
