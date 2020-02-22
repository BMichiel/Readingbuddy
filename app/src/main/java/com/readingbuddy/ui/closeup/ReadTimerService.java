package com.readingbuddy.ui.closeup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.readingbuddy.Book;
import com.readingbuddy.BookManager;
import com.readingbuddy.MainActivity;
import com.readingbuddy.NotificationReturnHandler;
import com.readingbuddy.R;
import com.readingbuddy.readingTimer;
import com.readingbuddy.secondPassedTimer;

import java.io.File;
import java.net.URI;

public class ReadTimerService extends Service {
    private static Book_Closeup BC;
    private static Context currentContext;
    private static Book currentBook;
    private static NotificationReturnHandler CurrentNotificationReturnHandler;
    public static readingTimer readTimer;

    private static PendingIntent pauseNotificationPendingIntent;
    private static PendingIntent playNotificationPendingIntent;

    private final static String CHANNEL_NAME = "Reading book channel";
    private final static String CHANNEL_DESCRIPTION = "The notification channel used while reading a book";
    private final static String CHANNEL_ID = "1";
    private final static int NOTIFICATION_ID = 1;


    public static Intent newLauncherIntent(Context context) {
        final Intent x = new Intent(context, MainActivity.class);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        x.setAction(Intent.ACTION_MAIN);
        x.addCategory(Intent.CATEGORY_LAUNCHER);
        return x;
    }

    private static NotificationCompat.Builder CreateNotification()
    {
        BookManager bm = new BookManager();
        currentBook = bm.getBook(currentContext, currentBook.RandomId);

        Intent pauseIntent = new Intent(currentContext, NotificationReturnHandler.class);
        pauseIntent.putExtra("Button", "Pause");
        Intent playIntent = new Intent(currentContext, NotificationReturnHandler.class);
        playIntent.putExtra("Button", "Play");

        pauseNotificationPendingIntent = PendingIntent.getBroadcast(currentContext, 1, pauseIntent, 0);
        playNotificationPendingIntent = PendingIntent.getBroadcast(currentContext, 2, playIntent, 0);
        PendingIntent clickNotificationPendingIntent = PendingIntent.getActivity(currentContext, 0, newLauncherIntent(currentContext), PendingIntent.FLAG_UPDATE_CURRENT);

        File file = new File(URI.create(currentBook.CoverPath).getPath());
        Bitmap bitmap = BitmapFactory.decodeResource(currentContext.getResources(), R.drawable.book);
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getPath());
        }

        TypedValue typedValue = new TypedValue();
        currentContext.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = ContextCompat.getColor(currentContext, typedValue.resourceId);

        NotificationCompat.Builder not_builder = new NotificationCompat.Builder(currentContext, CHANNEL_ID)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)

                .setContentIntent(clickNotificationPendingIntent)

                .setLargeIcon(bitmap)
                .setColor(color)
                .setSmallIcon(R.drawable.book);
        return not_builder;
    }

    private static void setNotificationManager()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = currentContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void updateNotification(NotificationCompat.Builder not_builder, boolean running, String current_timer_time)
    {
        NotificationCompat.Builder current_not_builder = not_builder;
        current_not_builder.mActions.clear();
        if (running)
        {
            current_not_builder.addAction(R.drawable.pause, "Pause", pauseNotificationPendingIntent);
        }
        else
        {
            current_not_builder.addAction(R.drawable.play, "Continue", playNotificationPendingIntent);
        }

        current_not_builder.setContentTitle(currentBook.Title);
        current_not_builder.setContentText("Reading for: " + current_timer_time);

        current_not_builder.setChannelId(CHANNEL_ID);

        Notification notification = current_not_builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, notification);
        }
        else
        {
            NotificationManager nMgr = (NotificationManager) currentContext.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.notify(NOTIFICATION_ID, notification);
        }
    }

    public void Pause_timer(boolean pause)
    {
        if (pause)
        {
            readTimer.pause();
        }
        else
        {
            readTimer.start();
        }
    }

    public ReadTimerService() {
        super();
    }

    public void SetReadTimeServiceValues(Book_Closeup BC, Context currentContext, Book currentBook) {
        this.BC = BC;
        this.currentContext = currentContext;
        this.currentBook = currentBook;

        CurrentNotificationReturnHandler = new NotificationReturnHandler();
        CurrentNotificationReturnHandler.setBookCloseup(BC);
    }

    @Override
    public void onCreate() {

        setNotificationManager();
        NotificationCompat.Builder not_builder = CreateNotification();

        readTimer = new readingTimer();

        readTimer.setTimerPassedFunction(new secondPassedTimer() {
            @Override
            public void onSecondPassed() {
                String timeStr = readTimer.getTimeStr();

                updateNotification(not_builder, readTimer.isRunning(), timeStr);
                BC.setTimeUI(timeStr);
            }
        });
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        readTimer.start();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        readTimer.stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
