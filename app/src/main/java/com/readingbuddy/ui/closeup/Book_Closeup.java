package com.readingbuddy.ui.closeup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;
import com.readingbuddy.Book;
import com.readingbuddy.BookManager;
import com.readingbuddy.MainActivity;
import com.readingbuddy.NotificationReturnHandler;
import com.readingbuddy.goalsManager;
import com.readingbuddy.ProgressBarAnimation;
import com.readingbuddy.R;
import com.readingbuddy.Themes;
import com.readingbuddy.preferenceHandler;
import com.readingbuddy.readMoment;
import com.readingbuddy.readingTimer;
import com.readingbuddy.secondPassedTimer;
import com.readingbuddy.ui.reading_sessions.reading_sessions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Book_Closeup extends AppCompatActivity implements popup_pages_read.pages_read_dialog_listener, popup_finished.finished_dialog_listener{

    static final int CAMERA = 1001;
    static final int GALLERY = 1002;

    private Intent serviceIntent;
    private Book currentBook;

    ImageButton bookImage;
    TextView bookTitle;
    TextView bookAuthor;
    TextView bookStatus;
    TextView booksStartDate;
    TextView booksLastReadDate;
    ProgressBar bookPagesProgress;
    TextView bookTime;
    RatingBar bookRating;

    TextView bookPages;
    TextView book_ppm;

    ImageButton editButton;
    LinearLayout display_layout;
    LinearLayout edit_layout;
    EditText edit_Title;
    EditText edit_Author;
    Spinner edit_Status;
    EditText edit_pages_total;
    RatingBar edit_rating;

    Button start_button;
    Button dnf_button;
    Button stop_button;
    Button cancel_session_button;
    Button pause_button;
    Button finish_button;

    LinearLayout more_sessions_btn;
    TextView last_session_pages;
    TextView last_session_date;
    TextView last_session_time;
    TextView pages_per_minute;

    boolean editState = false;

    TextView book_time_left;

    Context currentContext;

    private Toolbar tb;
    List<String> bookStates;

    boolean running = false;
    boolean paused = false;

    public void setTimeUI(String time){bookTime.setText(time);}

    public void handlePause() {
        pause_button.callOnClick();
    }

    public void handlePlay() {
        start_button.callOnClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final Themes currentTheme = Themes.fromInteger(intent.getIntExtra("Theme", 0));

        bookStates = new ArrayList<String>();
        bookStates.add("To read");
        bookStates.add("Currently reading");
        bookStates.add("Finished");
        bookStates.add("DNF");

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

        setContentView(R.layout.activity_book_closeup);
        currentContext = this;

        tb = findViewById(R.id.myToolbar_closeup);

        bookImage = findViewById(R.id.books_image);
        bookTitle = findViewById(R.id.books_title);
        bookStatus = findViewById(R.id.books_Status);
        bookAuthor = findViewById(R.id.books_Author);
        booksStartDate = findViewById(R.id.books_startDate);
        booksLastReadDate = findViewById(R.id.books_lastReadDate);
        bookPagesProgress = findViewById(R.id.pages_read_progress_bar);
        bookTime = findViewById(R.id.books_timeRead);
        bookRating = findViewById(R.id.rating);

        bookPages = findViewById(R.id.pages_read_text);
        book_ppm = findViewById(R.id.total_ppm);

        editButton = findViewById(R.id.Edit_Btn);
        display_layout = findViewById(R.id.display_layout);
        edit_layout = findViewById(R.id.editLayout);
        edit_Title = findViewById(R.id.edit_books_title);
        edit_Author = findViewById(R.id.edit_books_Author);
        edit_Status = findViewById(R.id.edit_books_Status);
        edit_pages_total = findViewById(R.id.edit_books_pages_total);
        edit_rating = findViewById(R.id.edit_rating);

        book_time_left = findViewById(R.id.book_time_left);

        start_button = findViewById(R.id.books_start_button);
        dnf_button = findViewById(R.id.DNF_button);
        stop_button = findViewById(R.id.books_stop_button);
        cancel_session_button = findViewById(R.id.books_cancel_session_button);
        pause_button = findViewById(R.id.books_pause_button);
        finish_button = findViewById(R.id.book_finish_button);


        more_sessions_btn = findViewById(R.id.more_sessions);
        last_session_date = findViewById(R.id.readingSession_date);
        last_session_pages = findViewById(R.id.readingSession_pages);
        last_session_time = findViewById(R.id.readingSession_time);
        pages_per_minute = findViewById(R.id.pages_per_minute);

        ReadTimerService RTS = new ReadTimerService();
        RTS.SetReadTimeServiceValues(this, currentContext, currentBook);

        if (currentBook.CurrentState == Book.State.TO_READ)
        {
            start_button.setVisibility(View.VISIBLE);
            dnf_button.setVisibility(View.GONE);
            finish_button.setVisibility(View.GONE);
        }
        else if (currentBook.CurrentState == Book.State.CURRENTLY_READING)
        {

            start_button.setVisibility(View.VISIBLE);
            dnf_button.setVisibility(View.VISIBLE);
            finish_button.setVisibility(View.GONE);
        }
        else
        {
            start_button.setVisibility(View.GONE);
            dnf_button.setVisibility(View.GONE);
            finish_button.setVisibility(View.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, bookStates);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        edit_Status.setAdapter(adapter);
        edit_Status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Book.State currentState = Book.State.fromInteger(position);
                if (currentState != null)
                {
                    currentBook.CurrentState = currentState;
                    if (currentState == Book.State.FINISHED || currentState == Book.State.DNF)
                    {
                        edit_rating.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        currentBook.Rating = 0;
                        edit_rating.setVisibility(View.GONE);
                        updateDisplay();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                edit_Status.setSelection(currentBook.CurrentState.getInt());
            }
        });

        edit_pages_total.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    editButton.callOnClick();
                }
                return false;
            }
        });

        more_sessions_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running)
                {
                    Toast.makeText(currentContext, "You cannot leave this page while reading", Toast.LENGTH_LONG).show();
                    return;
                }
                if (currentBook.readMoments.size() > 0)
                {
                    preferenceHandler prefHandler  = new preferenceHandler(currentContext);
                    Intent intent = new Intent(currentContext, reading_sessions.class);
                    intent.putExtra("Theme", prefHandler.getModePreference().getInt());
                    intent.putExtra("Book", currentBook.serialize());
                    startActivity(intent);
                } else {Toast.makeText(currentContext, "No reading sessions exist yet.", Toast.LENGTH_LONG).show();}
            }
        });

        start_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (currentBook.CurrentState == Book.State.TO_READ)
                {
                    currentBook.CurrentState = Book.State.CURRENTLY_READING;
                    saveBook();
                    updateDisplay();
                }
                serviceIntent = new Intent(currentContext, ReadTimerService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }


                running = true;
                paused = false;

                start_button.setVisibility(View.GONE);
                dnf_button.setVisibility(View.GONE);
                stop_button.setVisibility(View.VISIBLE);
                cancel_session_button.setVisibility(View.VISIBLE);
                pause_button.setVisibility(View.VISIBLE);
                finish_button.setVisibility(View.VISIBLE);

                bookTime.setText("00:00:00");
                pause_button.setText("Pause");
                bookTime.setTextColor(getResources().getColor(R.color.Dark_Red));
            }
        });

        dnf_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                currentBook.CurrentState = Book.State.DNF;
                dnf_button.setVisibility(View.GONE);
                start_button.setVisibility(View.GONE);

                popup_finished pf = new popup_finished(false);
                pf.show(getSupportFragmentManager(), "dialog finished");
            }
        });

        pause_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (running)
                {
                    if (!paused)
                    {
                        ReadTimerService RTS  = new ReadTimerService();
                        RTS.Pause_timer(true);
                        pause_button.setText("Continue");
                    }
                    else
                    {
                        ReadTimerService RTS  = new ReadTimerService();
                        RTS.Pause_timer(false);
                        pause_button.setText("Pause");
                    }
                    paused = !paused;
                }
            }
        });

        stop_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if (running)
                {
                    pause_button.callOnClick();
                }
                popup_pages_read ppr = new popup_pages_read(currentBook.getPages(), currentBook.TotalPages);
                ppr.show(getSupportFragmentManager(), "dialog pages read");
            }
        });

        cancel_session_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                running = false;

                stopService(serviceIntent);

                bookTime.setText(currentBook.getTotalReadTime());

                TypedValue tv = new TypedValue();

                currentContext.getTheme().resolveAttribute(R.attr.TextColor, tv, true);
                bookTime.setTextColor(tv.data);

                start_button.setVisibility(View.VISIBLE);
                dnf_button.setVisibility(View.VISIBLE);
                pause_button.setVisibility(View.GONE);
                stop_button.setVisibility(View.GONE);
                cancel_session_button.setVisibility(View.GONE);
                finish_button.setVisibility(View.GONE);
            }
        });

        finish_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if (running)
                {
                    pause_button.callOnClick();
                }
                popup_finished pf = new popup_finished(true);
                pf.show(getSupportFragmentManager(), "dialog finished");
            }
        });

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (running)
                {
                    return;
                }
                editState = !editState;

                if (!editState)
                {
                    try {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }catch (Exception e){/*keyboard was not open so it didn't need to be closed. closing it crashes the app*/}

                    editBook();
                    saveBook();
                    updateDisplay();
                }
                setMode(editState);
            }
        });

        bookImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        setMode(editState);
    }

    @Override
    public void applyPagesRead(String currentPage) {
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        int page = Integer.parseInt(currentPage);

        if (page == currentBook.TotalPages)
        {
            finish_button.callOnClick();
            return;
        }

        readMoment newReadMoment = new readMoment(timeStamp, ReadTimerService.readTimer.getTime(), page - currentBook.getPages());
        currentBook.addReadMoment(newReadMoment);
        checkPopUps(false, newReadMoment.readPages);

        running = false;
        stopService(serviceIntent);

        TypedValue tv = new TypedValue();
        currentContext.getTheme().resolveAttribute(R.attr.TextColor, tv, true);
        bookTime.setTextColor(tv.data);

        start_button.setVisibility(View.VISIBLE);
        dnf_button.setVisibility(View.VISIBLE);
        pause_button.setVisibility(View.GONE);
        stop_button.setVisibility(View.GONE);
        cancel_session_button.setVisibility(View.GONE);
        finish_button.setVisibility(View.GONE);

        saveBook();
        updateDisplay();
    }

    @Override
    public void applyFinished(int rating, boolean completed) {
        if (completed)
        {
            currentBook.CurrentState = Book.State.FINISHED;
            if (currentBook.getPages() != currentBook.TotalPages)
            {
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                readMoment newReadMoment = new readMoment(timeStamp, ReadTimerService.readTimer.getTime(), currentBook.TotalPages - currentBook.getPages());
                currentBook.addReadMoment(newReadMoment);
                checkPopUps(true, newReadMoment.readPages);
            }
            else {
                checkPopUps(true, 0);
            }
        }
        else
        {
            currentBook.CurrentState = Book.State.DNF;
        }
        currentBook.Rating = rating;

        running = false;
        stopService(serviceIntent);

        TypedValue tv = new TypedValue();
        currentContext.getTheme().resolveAttribute(R.attr.TextColor, tv, true);
        bookTime.setTextColor(tv.data);

        start_button.setVisibility(View.GONE);
        dnf_button.setVisibility(View.GONE);
        finish_button.setVisibility(View.GONE);
        stop_button.setVisibility(View.GONE);
        cancel_session_button.setVisibility(View.GONE);
        pause_button.setVisibility(View.GONE);

        saveBook();
        updateDisplay();
    }

    @Override
    protected void onResume() {
        BookManager bm = new BookManager();
        currentBook = bm.getBook(currentContext, currentBook.RandomId);
        updateDisplay();
        super.onResume();
    }



    @Override
    public void onBackPressed() {
        if (running)
        {
            Toast.makeText(currentContext, "You cannot leave this page while reading", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (editState)
            {
                Toast.makeText(currentContext, "You are still editing this book", Toast.LENGTH_SHORT).show();
            }
            else
            {
                finish();
            }
        }
    }

    private void checkPopUps(boolean finished, int pagesRead)
    {
        goalsManager GM = new goalsManager(currentContext);
        BookManager BM = new BookManager();
        List<Book> books = BM.getBooks(currentContext);
        books.add(currentBook);

        int booksReadYr = 0;
        int pagesReadYr = 0;
        int booksReadMt = 0;
        int pagesReadMt = 0;
        int pagesReadDy = 0;

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        for (int i = 0; i < books.size(); i++) {
            Book B = books.get(i);

            pagesReadYr += B.getPagesOnDate(timeStamp.substring(6));
            pagesReadMt += B.getPagesOnDate(timeStamp.substring(3));

            if (B.getLastReadDate().contains(timeStamp.substring(6))) {
                if (B.CurrentState == Book.State.FINISHED) {
                    booksReadYr += 1;
                }

                if (B.getLastReadDate().contains(timeStamp.substring(3))) {
                    if (B.CurrentState == Book.State.FINISHED) {
                        booksReadMt += 1;
                    }
                }
            }
            pagesReadDy += B.getPagesOnDate(timeStamp);
        }

        if (booksReadYr == GM.YearBooks && finished)
        {
            confetti();
            popup_goal_passed pg = new popup_goal_passed(goalsManager.GOALS.YR_BOOKS, GM.YearBooks);
            pg.show(getSupportFragmentManager(), "dialog goal passed");
        }
        if (pagesReadYr - pagesRead < GM.YearPages && pagesReadYr >= GM.YearPages)
        {
            confetti();
            popup_goal_passed pg = new popup_goal_passed(goalsManager.GOALS.YR_PAGES, GM.YearPages);
            pg.show(getSupportFragmentManager(), "dialog goal passed");
        }
        if (booksReadMt == GM.MonthBooks && finished)
        {
            confetti();
            popup_goal_passed pg = new popup_goal_passed(goalsManager.GOALS.MT_BOOKS, GM.MonthBooks);
            pg.show(getSupportFragmentManager(), "dialog goal passed");
        }
        if (pagesReadMt - pagesRead < GM.MonthPages && pagesReadMt >= GM.MonthPages)
        {
            confetti();
            popup_goal_passed pg = new popup_goal_passed(goalsManager.GOALS.MT_PAGES, GM.MonthPages);
            pg.show(getSupportFragmentManager(), "dialog goal passed");
        }
        if (pagesReadDy - pagesRead < GM.DailyPages && pagesReadDy >= GM.DailyPages)
        {
            confetti();
            popup_goal_passed pg = new popup_goal_passed(goalsManager.GOALS.DY_PAGES, GM.DailyPages);
            pg.show(getSupportFragmentManager(), "dialog goal passed");
        }
    }

    private void confetti()
    {
        new ParticleSystem(this, 80, R.drawable.confetti_blue, 10000)
                .setSpeedModuleAndAngleRange(0f, 0.2f, 180, 180)
                .setRotationSpeed(180)
                .setAcceleration(0.0001f, 90)
                .emit(findViewById(R.id.confettiRight), 7);

        new ParticleSystem(this, 80, R.drawable.confetti_orange, 10000)
                .setSpeedModuleAndAngleRange(0f, 0.2f, 180, 180)
                .setRotationSpeed(180)
                .setAcceleration(0.0001f, 90)
                .emit(findViewById(R.id.confettiRight), 7);

        new ParticleSystem(this, 80, R.drawable.confetti_green, 10000)
                .setSpeedModuleAndAngleRange(0f, 0.2f, 0, 0)
                .setRotationSpeed(180)
                .setAcceleration(0.0001f, 90)
                .emit(findViewById(R.id.confettiLeft), 7);


        new ParticleSystem(this, 80, R.drawable.confetti_pink, 10000)
                .setSpeedModuleAndAngleRange(0f, 0.2f, 0, 0)
                .setRotationSpeed(180)
                .setAcceleration(0.0001f, 90)
                .emit(findViewById(R.id.confettiLeft), 7);
    }

    private void selectImage()
    {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder popup = new AlertDialog.Builder(currentContext);
        popup.setTitle("Book cover");
        popup.setCancelable(true);
        popup.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera"))
                {
                    PackageManager pm = getPackageManager();
                    if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
                    {
                        Toast.makeText(currentContext, "This feature can currently not be used", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (intent.resolveActivity(getPackageManager()) != null)
                    {
                        try{
                            startActivityForResult(intent, CAMERA);
                        }catch(SecurityException e){
                            Toast.makeText(currentContext, "Camera permission denied", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else if (items[which].equals("Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select book cover"), GALLERY);
                }
            }
        });
        popup.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (currentBook.CoverPath.length() > 1)
            {
                File file = new File(URI.create(currentBook.CoverPath).getPath());
                file.delete();
            }

            Uri imageUri = Uri.EMPTY;
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = Bitmap.createBitmap(2, 2, conf);

            if (requestCode == CAMERA)
            {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            else if (requestCode == GALLERY)
            {
                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); //fails with camera. also images are cropped.....


                }catch(IOException e) {
                    Toast.makeText(currentContext, "could not retrieve image from gallary", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                return;
            }

            try {
                Matrix matrix = new Matrix();
                float newHight = 640;
                float scaleRatio = bitmap.getHeight() / newHight;
                float newWidth = bitmap.getWidth() / scaleRatio;

                matrix.postScale(((float)newWidth) /bitmap.getWidth(), ((float)newHight) / bitmap.getHeight());
                //matrix.postRotate(90);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); //fails with camera. also images are cropped.....

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                File pictureFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                FileOutputStream out = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                imageUri = Uri.parse(pictureFile.toString());
                out.close();

                if (!imageUri.equals(Uri.EMPTY)) {
                    File file = new File(imageUri.getPath());
                    if (file.exists()) {
                        currentBook.CoverPath = imageUri.toString();
                        bookImage.setImageURI(imageUri);
                        saveBook();
                    }
                }
            }catch (IOException e){Toast.makeText(currentContext, "Could not save file.", Toast.LENGTH_LONG).show();}
        }
    }

    private void setMode(boolean editState)
    {
        updateDisplay();
        if (editState)
        {
            dnf_button.setVisibility(View.GONE);
            start_button.setVisibility(View.GONE);
            finish_button.setVisibility(View.GONE);
            pause_button.setVisibility(View.GONE);
            stop_button.setVisibility(View.GONE);
            cancel_session_button.setVisibility(View.GONE);

            if (currentBook.CurrentState == Book.State.FINISHED)
            {
                edit_rating.setVisibility(View.VISIBLE);
            }
            else
            {
                edit_rating.setVisibility(View.GONE);
            }

            edit_layout.setVisibility(View.VISIBLE);
            display_layout.setVisibility(View.GONE);

            bookImage.setClickable(true);
        }
        else
        {
            if (currentBook.CurrentState == Book.State.TO_READ)
            {
                start_button.setVisibility(View.VISIBLE);
                dnf_button.setVisibility(View.GONE);
            }
            else if (currentBook.CurrentState == Book.State.CURRENTLY_READING)
            {
                start_button.setVisibility(View.VISIBLE);
                dnf_button.setVisibility(View.VISIBLE);
            }
            else
            {
                start_button.setVisibility(View.GONE);
                dnf_button.setVisibility(View.GONE);
                finish_button.setVisibility(View.GONE);
            }

            edit_layout.setVisibility(View.GONE);
            edit_rating.setVisibility(View.GONE);
            display_layout.setVisibility(View.VISIBLE);
            bookImage.setClickable(false);
        }
    }

    private void editBook()
    {
        currentBook.Title = edit_Title.getText().toString();
        currentBook.Author = edit_Author.getText().toString();
        try{
            int totalPages = Integer.parseInt(edit_pages_total.getText().toString());
            currentBook.TotalPages = totalPages;
        }catch(NumberFormatException e) {Toast.makeText(getApplicationContext(), "Could not read number from total pages", Toast.LENGTH_LONG);}

        currentBook.Rating = edit_rating.getProgress();
    }

    private void saveBook()
    {
        BookManager bm = new BookManager();
        bm.editBook(getApplicationContext(), currentBook);
    }


    private void updateDisplay()
    {
        tb.setTitle(currentBook.Title);
        bookTitle.setText(currentBook.Title);
        bookAuthor.setText(currentBook.Author);
        bookStatus.setText(bookStates.get(currentBook.CurrentState.getInt()));
        booksStartDate.setText(currentBook.getStartDate());
        booksLastReadDate.setText(currentBook.getLastReadDate());
        bookRating.setRating(currentBook.Rating * bookRating.getStepSize());
        bookPages.setText(currentBook.getPages() + "/" + currentBook.TotalPages);
        book_ppm.setText(String.format("%.2f p/m", currentBook.getAverigePPM()));

        if (running)
        {
            bookTime.setText(ReadTimerService.readTimer.getTimeStr());
        }
        else
        {
            bookTime.setText(currentBook.getTotalReadTime());
        }

        int progress = 0;
        if (currentBook.TotalPages > 0)
        {
            progress = (1000 * currentBook.getPages()) / currentBook.TotalPages;
        }
        ProgressBarAnimation animation = new ProgressBarAnimation(bookPagesProgress, bookPagesProgress.getProgress(), progress);
        animation.setDuration(1000);
        bookPagesProgress.startAnimation(animation);

        if (currentBook.CoverPath.length() > 1)
        {
            File file = new File(URI.create(currentBook.CoverPath).getPath());
            if (file.exists()) {
                bookImage.setImageURI(Uri.parse(currentBook.CoverPath));
                bookImage.setColorFilter(Color.argb(0, 255, 255, 255));
            }
        }

        edit_Title.setText(currentBook.Title);
        edit_Author.setText(currentBook.Author);
        edit_rating.setRating(currentBook.Rating * edit_rating.getStepSize());

        if (currentBook.TotalPages == 0)
        {
            edit_pages_total.setText("");
        }
        else
        {
            edit_pages_total.setText(String.valueOf(currentBook.TotalPages));
        }

        edit_Status.setSelection(currentBook.CurrentState.getInt());

        long timeLeft = 0;
        if (currentBook.getPages() > 0)
        {
            String timeReadingStr = currentBook.getTotalReadTime();
            int indexOfDots = timeReadingStr.indexOf(":");
            int hrsReading = Integer.parseInt(timeReadingStr.substring(0,indexOfDots)); //00:00:00
            int minReading = Integer.parseInt(timeReadingStr.substring(indexOfDots + 1,indexOfDots + 3));
            int secReading = Integer.parseInt(timeReadingStr.substring(indexOfDots + 4,indexOfDots + 6));
            long timeReading = (hrsReading * 3600) + (minReading * 60) + (secReading);

            timeLeft = ((timeReading * (currentBook.TotalPages - currentBook.getPages())) / currentBook.getPages());
            book_time_left.setText((timeLeft / 3600) + "h " + ((timeLeft /60) % 60) + "m " + (timeLeft % 60 )+ "s ");
        }
        else
        {
            book_time_left.setText("-");
        }

        if (currentBook.CurrentState == Book.State.FINISHED ||
                currentBook.CurrentState == Book.State.DNF) {
        } else if (currentBook.CurrentState == Book.State.CURRENTLY_READING) {
            start_button.setText("Continue reading");
        }else {
            start_button.setText("Start reading");
        }

        readMoment lastMom = currentBook.getLastSession();
        if (lastMom == null)
        {
            last_session_pages.setText("-");
            last_session_date.setText("-");
            last_session_time.setText("-");
            pages_per_minute.setText("-");
        }
        else
        {
            last_session_pages.setText(String.valueOf(lastMom.readPages));
            last_session_date.setText(lastMom.readDate);
            last_session_time.setText(lastMom.getTimeString());

            if (lastMom.readPages > 0 && lastMom.readTime > 0)
            {
                pages_per_minute.setText(String.format("%.2f", lastMom.getPPM()));
            }
        }
    }
}
