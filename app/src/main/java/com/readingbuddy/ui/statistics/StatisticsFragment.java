package com.readingbuddy.ui.statistics;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.readingbuddy.Book;
import com.readingbuddy.BookManager;
import com.readingbuddy.ProgressBarAnimation;
import com.readingbuddy.R;
import com.readingbuddy.goalsManager;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;

    TextView Day_pages_read;
    ProgressBar Day_pages_read_progress_bar;

    ProgressBar Month_pages_read_progress_bar;
    TextView Month_pages_read;
    TextView Month_ppm;
    TextView Month_Time_Read;

    ProgressBar Month_Books_Read_ProgressBar;
    TextView Month_Books_Read;

    TextView Month_favourite_Book1_Title;
    ImageView Month_favourite_Book1;
    RatingBar Month_favourite_Book1_rating;
    TextView Month_favourite_Book2_Title;
    ImageView Month_favourite_Book2;
    RatingBar Month_favourite_Book2_rating;
    TextView Month_favourite_Book3_Title;
    ImageView Month_favourite_Book3;
    RatingBar Month_favourite_Book3_rating;

    RatingBar Month_avg_rating;



    ProgressBar Year_progress_bar_pages_read;
    TextView Year_pages_read;
    TextView Year_ppm;
    TextView Year_Time_Read;

    ProgressBar Year_Books_Read_ProgressBar;
    TextView Year_Books_Read;


    TextView Year_favourite_Book1_Title;
    ImageView Year_favourite_Book1;
    RatingBar Year_favourite_Book1_rating;
    TextView Year_favourite_Book2_Title;
    ImageView Year_favourite_Book2;
    RatingBar Year_favourite_Book2_rating;
    TextView Year_favourite_Book3_Title;
    ImageView Year_favourite_Book3;
    RatingBar Year_favourite_Book3_rating;

    RatingBar Year_avg_rating;

    TextView Total_Books_read;
    TextView Total_Pages_read;
    TextView Total_Time_read;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_statistics, container,false);
        Context currentContext = getActivity();

        Day_pages_read = currentView.findViewById(R.id.Day_pages_read);
        Day_pages_read_progress_bar = currentView.findViewById(R.id.Day_pages_read_progress_bar);
        Month_pages_read_progress_bar = currentView.findViewById(R.id.Month_pages_read_progress_bar);
        Month_pages_read = currentView.findViewById(R.id.Month_pages_read);
        Month_ppm = currentView.findViewById(R.id.Month_ppm);
        Month_Time_Read = currentView.findViewById(R.id.Month_Time_Read);
        Month_Books_Read_ProgressBar = currentView.findViewById(R.id.Month_Books_Read_ProgressBar);
        Month_Books_Read = currentView.findViewById(R.id.Month_Books_Read);
        Month_favourite_Book1_Title = currentView.findViewById(R.id.Month_favourite_Book1_Title);
        Month_favourite_Book1 = currentView.findViewById(R.id.Month_favourite_Book1);
        Month_favourite_Book1_rating = currentView.findViewById(R.id.Month_favourite_Book1_rating);
        Month_favourite_Book2_Title = currentView.findViewById(R.id.Month_favourite_Book2_Title);
        Month_favourite_Book2 = currentView.findViewById(R.id.Month_favourite_Book2);
        Month_favourite_Book2_rating = currentView.findViewById(R.id.Month_favourite_Book2_rating);
        Month_favourite_Book3_Title = currentView.findViewById(R.id.Month_favourite_Book3_Title);
        Month_favourite_Book3 = currentView.findViewById(R.id.Month_favourite_Book3);
        Month_favourite_Book3_rating = currentView.findViewById(R.id.Month_favourite_Book3_rating);
        Month_avg_rating = currentView.findViewById(R.id.Month_avg_rating);
        Year_progress_bar_pages_read = currentView.findViewById(R.id.Year_progress_bar_pages_read);
        Year_pages_read = currentView.findViewById(R.id.Year_pages_read);
        Year_ppm = currentView.findViewById(R.id.Year_ppm);
        Year_Time_Read = currentView.findViewById(R.id.Year_Time_Read);
        Year_Books_Read_ProgressBar = currentView.findViewById(R.id.Year_Books_Read_ProgressBar);
        Year_Books_Read = currentView.findViewById(R.id.Year_Books_Read);
        Year_favourite_Book1_Title = currentView.findViewById(R.id.Year_favourite_Book1_Title);
        Year_favourite_Book1 = currentView.findViewById(R.id.Year_favourite_Book1);
        Year_favourite_Book1_rating = currentView.findViewById(R.id.Year_favourite_Book1_rating);
        Year_favourite_Book2_Title = currentView.findViewById(R.id.Year_favourite_Book2_Title);
        Year_favourite_Book2 = currentView.findViewById(R.id.Year_favourite_Book2);
        Year_favourite_Book2_rating = currentView.findViewById(R.id.Year_favourite_Book2_rating);
        Year_favourite_Book3_Title = currentView.findViewById(R.id.Year_favourite_Book3_Title);
        Year_favourite_Book3 = currentView.findViewById(R.id.Year_favourite_Book3);
        Year_favourite_Book3_rating = currentView.findViewById(R.id.Year_favourite_Book3_rating);
        Year_avg_rating = currentView.findViewById(R.id.Year_avg_rating);
        Total_Books_read = currentView.findViewById(R.id.Total_Books_read);
        Total_Pages_read = currentView.findViewById(R.id.Total_Pages_read);
        Total_Time_read = currentView.findViewById(R.id.Total_Time_read);


        //get values

        goalsManager GM = new goalsManager(currentContext);
        BookManager BM = new BookManager();
        List<Book> books = BM.getBooks(currentContext);

        int pagesReadDy = 0;

        int booksReadTot = 0;
        int pagesReadTot = 0;
        int timeReadTot = 0;
        int totalRatTot = 0;

        int booksReadYr = 0;
        int pagesReadYr = 0;
        int timeReadYr = 0;
        int totalRatYr = 0;

        int booksReadMt = 0;
        int pagesReadMt = 0;
        int timeReadMt = 0;
        int totalRatMt = 0;

        Book emptyBook = new Book("Title", 1, "", "", Book.State.TO_READ, 0, 0);
        Book favMt1 = emptyBook;
        Book favMt2 = emptyBook;
        Book favMt3 = emptyBook;

        Book favYr1 = emptyBook;
        Book favYr2 = emptyBook;
        Book favYr3 = emptyBook;

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        for (int i = 0; i < books.size(); i++) {
            Book B = books.get(i);

            pagesReadYr += B.getPagesOnDate(timeStamp.substring(6));
            timeReadYr += B.getTimeOnDate(timeStamp.substring(6));

            pagesReadTot += B.getPages();
            timeReadTot += B.getTimeOnDate("-");

            pagesReadMt += B.getPagesOnDate(timeStamp.substring(3));
            timeReadMt += B.getTimeOnDate(timeStamp.substring(3));

            pagesReadDy += B.getPagesOnDate(timeStamp);

            if (B.CurrentState == Book.State.FINISHED)
            {
                booksReadTot += 1;
                totalRatTot += B.Rating;
                if (B.getLastReadDate().contains(timeStamp.substring(6))) {
                    booksReadYr += 1;
                    totalRatYr += B.Rating;

                    if (B.getLastReadDate().contains(timeStamp.substring(3))) {
                        booksReadMt += 1;
                        totalRatMt += B.Rating;
                        // 5, 4, 3
                        // 5, 4, 6
                        // 5, 6, 4
                        //
                        if (B.Rating > favMt1.Rating) { favMt3 = favMt2; favMt2 = favMt1; favMt1 = B; }
                        else if (B.Rating > favMt2.Rating) { favMt3 = favMt2; favMt2 = B; }
                        else if (B.Rating > favMt3.Rating) { favMt3 = B; }
                    }

                    if (B.Rating > favYr1.Rating) { favYr3 = favYr2; favYr2 = favYr1; favYr1 = B; }
                    else if (B.Rating > favYr2.Rating) { favYr3 = favYr2; favYr2 = B; }
                    else if (B.Rating > favYr3.Rating) { favYr3 = B; }
                }
            }
        }

        Day_pages_read.setText(pagesReadDy + "/" + GM.DailyPages);
        Day_pages_read_progress_bar.startAnimation(setAnimationProgressbar(Day_pages_read_progress_bar, pagesReadDy * Day_pages_read_progress_bar.getMax() / GM.DailyPages));

        Month_pages_read_progress_bar.startAnimation(setAnimationProgressbar(Month_pages_read_progress_bar, pagesReadMt * Month_pages_read_progress_bar.getMax() / GM.MonthPages));
        Month_pages_read.setText(pagesReadMt + "/" + GM.MonthPages);
        Month_ppm.setText(String.format("%.2f p/m", (double) pagesReadMt / timeReadMt));
        Month_Time_Read.setText(timeToString(timeReadMt));
        Month_Books_Read_ProgressBar.startAnimation(setAnimationProgressbar(Month_Books_Read_ProgressBar, booksReadMt * Month_pages_read_progress_bar.getMax() / GM.MonthBooks));
        Month_Books_Read.setText("" + booksReadMt);
        Month_favourite_Book1_Title.setText(favMt1.Title);
        setImage(Month_favourite_Book1, favMt1);
        Month_favourite_Book1_rating.setProgress(favMt1.Rating);
        Month_favourite_Book2_Title.setText(favMt2.Title);
        setImage(Month_favourite_Book2, favMt2);
        Month_favourite_Book2_rating.setProgress(favMt2.Rating);
        Month_favourite_Book3_Title.setText(favMt3.Title);
        setImage(Month_favourite_Book3, favMt3);
        Month_favourite_Book3_rating.setProgress(favMt3.Rating);
        if (booksReadMt > 0)
        {
            Month_avg_rating.setProgress(totalRatMt / booksReadMt);
        }else Month_avg_rating.setProgress(0);



        Year_progress_bar_pages_read.startAnimation(setAnimationProgressbar(Year_progress_bar_pages_read, pagesReadYr * Year_Books_Read_ProgressBar.getMax() / GM.YearPages));
        Year_pages_read.setText(pagesReadYr + "/" + GM.YearPages);
        Year_ppm.setText(String.format("%.2f p/m", (double) pagesReadYr / timeReadYr));
        Year_Time_Read.setText(timeToString(timeReadYr));
        Year_Books_Read_ProgressBar.startAnimation((setAnimationProgressbar(Year_Books_Read_ProgressBar, booksReadYr * Year_Books_Read_ProgressBar.getMax() / GM.YearBooks)));
        Year_Books_Read.setText("" + booksReadYr);
        Year_favourite_Book1_Title.setText(favYr1.Title);
        setImage(Year_favourite_Book1, favYr1);
        Year_favourite_Book1_rating.setProgress(favYr1.Rating);
        Year_favourite_Book2_Title.setText(favYr2.Title);
        setImage(Year_favourite_Book2, favYr2);
        Year_favourite_Book2_rating.setProgress(favYr2.Rating);
        Year_favourite_Book3_Title.setText(favYr3.Title);
        setImage(Year_favourite_Book3, favYr3);
        Year_favourite_Book3_rating.setProgress(favYr3.Rating);
        if (booksReadYr > 0)
        {
            Year_avg_rating.setProgress(totalRatYr / booksReadYr);
        }else Year_avg_rating.setProgress(0);

        Total_Books_read.setText("" + booksReadTot);
        Total_Pages_read.setText("" + pagesReadTot);
        Total_Time_read.setText(timeToString(timeReadTot));


        return currentView;
    }

    ProgressBarAnimation setAnimationProgressbar(ProgressBar pb, int progress)
    {
        ProgressBarAnimation animation = new ProgressBarAnimation(pb, pb.getProgress(), progress);
        animation.setDuration(1000);
        return animation;
    }

    void setImage(ImageView v, Book b)
    {
        File file = new File(URI.create(b.CoverPath).getPath());
        if (file.exists()) {
            v.setImageURI(Uri.parse(b.CoverPath));
            v.setColorFilter(Color.argb(0, 255, 255, 255));
        }
    }

    String timeToString(long time)
    {
        String t = "";
        long h  = time / 3600;
        int m = (int)(time / 60) % 60;
        int s = (int)time % 60;

        if (h < 10) { t += "0"; }
        t += h + "h ";
        if (m < 10) { t += "0"; }
        t += m + "m ";
        if (s < 10) { t += "0"; }
        t += s + "s";

        return t;
    }
}