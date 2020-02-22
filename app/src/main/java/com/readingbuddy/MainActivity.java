package com.readingbuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.readingbuddy.ui.credits.Credits;
import com.readingbuddy.ui.currentlyReading.CurrentlyReadingFragment;
import com.readingbuddy.ui.books.BooksFragment;
import com.readingbuddy.ui.goals.GoalsFragment;
import com.readingbuddy.ui.statistics.StatisticsFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity implements DrawerLocker{

    private enum CURRENTFRAGMENT {CURRENTLY_READING, BOOKS, STATISTICS, GOALS}
    private CURRENTFRAGMENT currentFragment = CURRENTFRAGMENT.CURRENTLY_READING;

    private BottomNavigationView bottom_navigation_view;
    private preferenceHandler prefHandler;
    private Toolbar tb;

    private Context currentContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb = findViewById(R.id.myToolbar);
        tb.inflateMenu(R.menu.toolbar);
        tb.setOnMenuItemClickListener(toolBarListener);

        currentContext = getBaseContext();

        bottom_navigation_view = findViewById(R.id.bottom_navigation);
        bottom_navigation_view.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentlyReadingFragment()).commit();

        prefHandler  = new preferenceHandler(this);
        setColorsOfObjects(prefHandler.getModePreference());
    }


    public void setDrawerEnabled(boolean enabled)
    {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);

        if (enabled)
        {
            nav.setOnNavigationItemSelectedListener(navListener);
        }
        else
        {
            nav.setOnNavigationItemSelectedListener(null);
        }
    }

    private void refreshUI()
    {
        setContentView(R.layout.activity_main);

        tb = findViewById(R.id.myToolbar);
        tb.inflateMenu(R.menu.toolbar);
        tb.setOnMenuItemClickListener(toolBarListener);

        bottom_navigation_view = findViewById(R.id.bottom_navigation);
        bottom_navigation_view.setOnNavigationItemSelectedListener(navListener);
    }

    private void setColorsOfObjects(Themes currentTheme) {

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
        refreshUI();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (currentTheme == Themes.DARK_GREEN_THEME||
                    currentTheme == Themes.DARK_BLUE_THEME ||
                    currentTheme == Themes.DARK_ORANGE_THEME ||
                    currentTheme == Themes.DARK_PINK_THEME ||
                    currentTheme == Themes.DARK_RED_THEME ||
                    currentTheme == Themes.DARK_PURPLE_THEME)
            {
                getWindow().setStatusBarColor(getResources().getColor(R.color.Dark_Grey));
            }else if (currentTheme == Themes.LIGHT_GREEN_THEME ||
                        currentTheme == Themes.LIGHT_BLUE_THEME ||
                        currentTheme == Themes.LIGHT_PINK_THEME ||
                        currentTheme == Themes.LIGHT_RED_THEME ||
                        currentTheme == Themes.LIGHT_ORANGE_THEME ||
                        currentTheme == Themes.LIGHT_PURPLE_THEME)
            {
                getWindow().setStatusBarColor(getResources().getColor(R.color.Light_Grey));
            }
        }


        Drawable icon = tb.getOverflowIcon();
        if (icon != null) {
            icon = DrawableCompat.wrap(icon);

            TypedValue tv = new TypedValue();
            this.getTheme().resolveAttribute(R.attr.colorAccent, tv, true);
            DrawableCompat.setTint(icon.mutate(), tv.data);
            tb.setOverflowIcon(icon);
        }

        Fragment frag;
        switch (currentFragment) {
            default:
            case CURRENTLY_READING:
                frag = new CurrentlyReadingFragment();
                bottom_navigation_view.setSelectedItemId(R.id.currently_reading_action);
                break;
            case BOOKS:
                frag = new BooksFragment();
                bottom_navigation_view.setSelectedItemId(R.id.Books_action);
                break;
            case STATISTICS:
                frag = new StatisticsFragment();
                bottom_navigation_view.setSelectedItemId(R.id.Stats_action);
                break;
            case GOALS:
                frag = new GoalsFragment();
                bottom_navigation_view.setSelectedItemId(R.id.Goals_action);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
    }


    private Toolbar.OnMenuItemClickListener toolBarListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.lightTheme:
                    prefHandler.setModePreference(Themes.LIGHT_GREEN_THEME);
                    setColorsOfObjects(Themes.LIGHT_GREEN_THEME);
                    return true;

                case R.id.lightBlue:
                    prefHandler.setModePreference(Themes.LIGHT_BLUE_THEME);
                    setColorsOfObjects(Themes.LIGHT_BLUE_THEME);
                    return true;

                case R.id.lightPink:
                    prefHandler.setModePreference(Themes.LIGHT_PINK_THEME);
                    setColorsOfObjects(Themes.LIGHT_PINK_THEME);
                    return true;

                case R.id.lightPurple:
                    prefHandler.setModePreference(Themes.LIGHT_PURPLE_THEME);
                    setColorsOfObjects(Themes.LIGHT_PURPLE_THEME);
                    return true;

                case R.id.lightOrange:
                    prefHandler.setModePreference(Themes.LIGHT_ORANGE_THEME);
                    setColorsOfObjects(Themes.LIGHT_ORANGE_THEME);
                    return true;

                case R.id.lightRed:
                    prefHandler.setModePreference(Themes.LIGHT_RED_THEME);
                    setColorsOfObjects(Themes.LIGHT_RED_THEME);
                    return true;


                case R.id.darkTheme:
                    prefHandler.setModePreference(Themes.DARK_GREEN_THEME);
                    setColorsOfObjects(Themes.DARK_GREEN_THEME);
                    return true;

                case R.id.darkPink:
                    prefHandler.setModePreference(Themes.DARK_PINK_THEME);
                    setColorsOfObjects(Themes.DARK_PINK_THEME);
                    return true;

                case R.id.darkBlue:
                    prefHandler.setModePreference(Themes.DARK_BLUE_THEME);
                    setColorsOfObjects(Themes.DARK_BLUE_THEME);
                    return true;

                case R.id.darkRed:
                    prefHandler.setModePreference(Themes.DARK_RED_THEME);
                    setColorsOfObjects(Themes.DARK_RED_THEME);
                    return true;

                case R.id.darkPurple:
                    prefHandler.setModePreference(Themes.DARK_PURPLE_THEME);
                    setColorsOfObjects(Themes.DARK_PURPLE_THEME);
                    return true;

                case R.id.darkOrange:
                    prefHandler.setModePreference(Themes.DARK_ORANGE_THEME);
                    setColorsOfObjects(Themes.DARK_ORANGE_THEME);
                    return true;

                case R.id.CreditsBtn:
                    Intent i = new Intent(currentContext, Credits.class);
                    i.putExtra("Theme", prefHandler.getModePreference().getInt());
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Toolbar tb = findViewById(R.id.myToolbar);
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.currently_reading_action:
                            selectedFragment = new CurrentlyReadingFragment();
                            currentFragment = CURRENTFRAGMENT.CURRENTLY_READING;
                            tb.setTitle("Currently reading");
                            break;

                        case R.id.Books_action:
                            selectedFragment = new BooksFragment();
                            currentFragment = CURRENTFRAGMENT.BOOKS;
                            tb.setTitle("All books");
                            break;

                        case R.id.Stats_action:
                            selectedFragment = new StatisticsFragment();
                            currentFragment = CURRENTFRAGMENT.STATISTICS;
                            tb.setTitle("Statistics");
                            break;

                        case R.id.Goals_action:
                            selectedFragment = new GoalsFragment();
                            currentFragment = CURRENTFRAGMENT.GOALS;
                            tb.setTitle("Goals");
                            break;
                    }
                    if (selectedFragment == null)
                    {
                        Toast.makeText(getApplicationContext(), "This page does not exist. Please report this bug.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
    });
}
