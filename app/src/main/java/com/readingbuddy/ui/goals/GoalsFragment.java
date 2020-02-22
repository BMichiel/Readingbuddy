package com.readingbuddy.ui.goals;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.readingbuddy.R;
import com.readingbuddy.goalsManager;

import java.sql.Time;
import java.text.ParseException;

public class GoalsFragment  extends Fragment {
    private GoalsViewModel goalViewModel;

    private Context currentContext;

    private EditText Goal_Year_Pages;
    private EditText Goal_Year_Books;
    private EditText Goal_Month_Pages;
    private EditText Goal_Month_Books;
    private EditText Goal_Daily_Pages;

    private CheckBox Notification_Monday;
    private CheckBox Notification_Tuesday;
    private CheckBox Notification_Wednesday;
    private CheckBox Notification_Thursday;
    private CheckBox Notification_Friday;
    private CheckBox Notification_Saturday;
    private CheckBox Notification_Sunday;

    private TextView Notification_Time;

    private Button Goals_Save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goals, container,false);

        Goal_Year_Pages = v.findViewById(R.id.Goal_Year_Pages);
        Goal_Year_Books = v.findViewById(R.id.Goal_Year_Books);
        Goal_Month_Pages = v.findViewById(R.id.Goal_Month_Pages);
        Goal_Month_Books = v.findViewById(R.id.Goal_Month_Books);
        Goal_Daily_Pages = v.findViewById(R.id.Goal_Daily_Pages);

        Notification_Monday = v.findViewById(R.id.Notification_Monday);
        Notification_Tuesday = v.findViewById(R.id.Notification_Tuesday);
        Notification_Wednesday = v.findViewById(R.id.Notification_Wednesday);
        Notification_Thursday = v.findViewById(R.id.Notification_Thursday);
        Notification_Friday = v.findViewById(R.id.Notification_Friday);
        Notification_Saturday = v.findViewById(R.id.Notification_Saturday);
        Notification_Sunday = v.findViewById(R.id.Notification_Sunday);

        Notification_Time = v.findViewById(R.id.Notification_Time);

        Goals_Save = v.findViewById(R.id.Goals_Save);

        currentContext = getActivity();

        goalsManager GM = new goalsManager(currentContext);

        Goal_Year_Books.setText("" + GM.YearBooks);
        Goal_Year_Pages.setText("" + GM.YearPages);
        Goal_Month_Books.setText("" + GM.MonthBooks);
        Goal_Month_Pages.setText("" + GM.MonthPages);
        Goal_Daily_Pages.setText("" + GM.DailyPages);

        Notification_Monday.setChecked(GM.notifyMonday);
        Notification_Tuesday.setChecked(GM.notifyTuesday);
        Notification_Wednesday.setChecked(GM.notifyWednesday);
        Notification_Thursday.setChecked(GM.notifyThursday);
        Notification_Friday.setChecked(GM.notifyFriday);
        Notification_Saturday.setChecked(GM.notifySaturday);
        Notification_Sunday.setChecked(GM.notifySunday);

        Notification_Time.setText(GM.timeToStr(GM.notificationTime));

        Goals_Save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Goal_Year_Pages.getText().length() > 0) {GM.YearPages = Integer.parseInt(Goal_Year_Pages.getText().toString());}
                if (Goal_Year_Books.getText().length() > 0) {GM.YearBooks = Integer.parseInt(Goal_Year_Books.getText().toString());}
                if (Goal_Month_Pages.getText().length() > 0) {GM.MonthPages = Integer.parseInt(Goal_Month_Pages.getText().toString());}
                if (Goal_Month_Books.getText().length() > 0) {GM.MonthBooks = Integer.parseInt(Goal_Month_Books.getText().toString());}
                if (Goal_Daily_Pages.getText().length() > 0) {GM.DailyPages = Integer.parseInt(Goal_Daily_Pages.getText().toString());}

                GM.notifyMonday = Notification_Monday.isChecked();
                GM.notifyTuesday = Notification_Tuesday.isChecked();
                GM.notifyWednesday = Notification_Wednesday.isChecked();
                GM.notifyThursday = Notification_Thursday.isChecked();
                GM.notifyFriday = Notification_Friday.isChecked();
                GM.notifySaturday = Notification_Saturday.isChecked();
                GM.notifySunday = Notification_Sunday.isChecked();

                GM.notificationTime = GM.timeFromStr(Notification_Time.getText().toString());

                try {
                    InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e){/*keyboard was not open so it didn't need to be closed. closing it crashes the app*/}

                //TODO create and remove notifications per day

                GM.saveGoals();
                Toast.makeText(currentContext, "Saved goals successfully", Toast.LENGTH_LONG).show();
            }
        });

        Notification_Time.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Goals_Save.callOnClick();
                return true;
            }
        });
        return v;
    }
}
