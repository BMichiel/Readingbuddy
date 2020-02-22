package com.readingbuddy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.readingbuddy.ui.closeup.Book_Closeup;

public class NotificationReturnHandler extends BroadcastReceiver {
    private static Book_Closeup book_closeup;


    public void setBookCloseup(Book_Closeup book_closeup)
    {
        this.book_closeup = book_closeup;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String buttonStr = intent.getStringExtra("Button");
        if (book_closeup == null)
        {
            return;
        }

        if (buttonStr.equals("Play"))
        {
            book_closeup.handlePlay();
        }
        else if (buttonStr.equals("Pause"))
        {
            book_closeup.handlePause();
        }
    }
}
