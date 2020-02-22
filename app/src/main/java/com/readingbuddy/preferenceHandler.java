package com.readingbuddy;

import android.content.Context;
import android.content.SharedPreferences;

public class preferenceHandler {


    private Context currentContext;

    public preferenceHandler(Context c)
    {
        currentContext = c;
    }

    public Themes getModePreference()
    {
        SharedPreferences sp = currentContext.getSharedPreferences("Dark_Mode", Context.MODE_PRIVATE);
        return Themes.fromInteger(sp.getInt("Store_Key", 0));
    }

    public void setModePreference(Themes value)
    {
        SharedPreferences.Editor sp = currentContext.getSharedPreferences("Dark_Mode", Context.MODE_PRIVATE).edit();

        sp.putInt("Store_Key", value.getInt());
        sp.commit();
    }
}
