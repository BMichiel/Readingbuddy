package com.readingbuddy.ui.credits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.readingbuddy.R;
import com.readingbuddy.Themes;

public class Credits extends AppCompatActivity {

    Context currentContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_credits);
        currentContext = this;

        Intent intent = getIntent();
        final Themes currentTheme = Themes.fromInteger(intent.getIntExtra("Theme", 0));

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

        setContentView(R.layout.activity_credits);

        Toolbar tb = findViewById(R.id.myToolbar);
        tb.setTitle("Credits");
    }
}
