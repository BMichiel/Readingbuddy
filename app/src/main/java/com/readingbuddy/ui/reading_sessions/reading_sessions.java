package com.readingbuddy.ui.reading_sessions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.readingbuddy.Book;
import com.readingbuddy.BookManager;
import com.readingbuddy.R;
import com.readingbuddy.Themes;
import com.readingbuddy.readMoment;

public class reading_sessions extends AppCompatActivity implements popup_edit_session.editSessionListener{

    private Toolbar tb;
    Context currentContext;
    ListView sessions;
    sessionsListAdapter listAdapter;

    Book currentBook;
    boolean editState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final Themes currentTheme = Themes.fromInteger(intent.getIntExtra("Theme", 0));

        currentBook = new Book(intent.getStringExtra("Book"));
        editState = intent.getBooleanExtra("EditMode", false);

        switch(currentTheme)
        {
            case LIGHT_BLUE_THEME:
                setTheme(R.style.LightThemeBlue);
                break;
            case LIGHT_GREEN_THEME:
                setTheme(R.style.LightThemeGreen);
                break;
            case LIGHT_ORANGE_THEME:
                setTheme(R.style.LightThemeOrange);
                break;
            case LIGHT_PINK_THEME:
                setTheme(R.style.LightThemePink);
                break;
            case LIGHT_RED_THEME:
                setTheme(R.style.LightThemeRed);
                break;
            case LIGHT_PURPLE_THEME:
                setTheme(R.style.LightThemePurple);
                break;

            case DARK_BLUE_THEME:
                setTheme(R.style.DarkThemeBlue);
                break;
            case DARK_GREEN_THEME:
                setTheme(R.style.DarkThemeGreen);
                break;
            case DARK_ORANGE_THEME:
                setTheme(R.style.DarkThemeOrange);
                break;
            case DARK_PINK_THEME:
                setTheme(R.style.DarkThemePink);
                break;
            case DARK_RED_THEME:
                setTheme(R.style.DarkThemeRed);
                break;
            case DARK_PURPLE_THEME:
                setTheme(R.style.DarkThemePurple);
                break;
        }
        setContentView(R.layout.activity_sessions);
        currentContext = this;

        tb = findViewById(R.id.myToolbar_Sessions);
        sessions = findViewById(R.id.list_reading_sessions);


        listAdapter = new sessionsListAdapter();
        sessions.setAdapter(listAdapter);
        sessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popup_edit_session pes = new popup_edit_session(currentBook.readMoments.get(position), position);
                pes.show(getSupportFragmentManager(), "edit reading session");
            }
        });
        updateView();
    }

    private void updateView()
    {
        tb.setTitle(currentBook.Title);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditWindowClosed(readMoment newMoment, int position) {
        currentBook.readMoments.set(position, newMoment);
        BookManager bm = new BookManager();
        bm.editBook(currentContext, currentBook);
        updateView();
    }

    @Override
    public void onDeleteButtonPressed(int position) {
        currentBook.readMoments.remove(position);
        BookManager bm = new BookManager();
        bm.editBook(currentContext, currentBook);
        updateView();
    }


    class sessionsListAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return currentBook.readMoments.size();
        }

        @Override
        public Object getItem(int position) {
            return currentBook.readMoments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.reading_session_list_item,null);
            readMoment mom =  currentBook.readMoments.get(position);

            TextView date = view.findViewById(R.id.readingSession_date);
            TextView pages = view.findViewById(R.id.readingSession_pages);
            TextView time = view.findViewById(R.id.readingSession_time);
            TextView ppm = view.findViewById(R.id.pages_per_minute);

            date.setText(mom.readDate);
            pages.setText(String.valueOf(mom.readPages));
            time.setText(mom.getTimeString());
            ppm.setText(String.format("%.2f", mom.getPPM()));

            return view;
        }
    }

}
