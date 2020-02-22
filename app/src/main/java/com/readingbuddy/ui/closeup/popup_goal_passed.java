package com.readingbuddy.ui.closeup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;

import com.plattysoft.leonids.ParticleSystem;
import com.readingbuddy.R;
import com.readingbuddy.goalsManager;
import com.readingbuddy.ui.goals.GoalsViewModel;

public class popup_goal_passed extends AppCompatDialogFragment {
    private Toolbar popupToolbar;
    private Button acceptButton;
    private TextView AchievedGoalText;

    private goalsManager.GOALS goalPassed;
    private int goalNr;

    public popup_goal_passed(goalsManager.GOALS goalPassed,  int goalNr)
    {
        this.goalPassed = goalPassed;
        this.goalNr = goalNr;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog builder = new AlertDialog.Builder(getActivity(), getTheme()).create();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_goal_completed, null);

        builder.setView(view);

        popupToolbar = view.findViewById(R.id.popupToolbar);
        acceptButton = view.findViewById(R.id.done_button);
        AchievedGoalText = view.findViewById(R.id.AchievedGoalText);

        popupToolbar.setTitle("Congratulations!");

        switch (goalPassed)
        {
            case YR_BOOKS:
                AchievedGoalText.setText(String.format("You achieved your goal for %d books this year!", goalNr));
                break;
            case YR_PAGES:
                AchievedGoalText.setText(String.format("You achieved your goal for %d pages this year!", goalNr));
                break;
            case MT_BOOKS:
                AchievedGoalText.setText(String.format("You achieved your goal for %d books this month!", goalNr));
                break;
            case MT_PAGES:
                AchievedGoalText.setText(String.format("You achieved your goal for %d pages this month!", goalNr));
                break;
            case DY_PAGES:
                AchievedGoalText.setText(String.format("You achieved your goal for %d pages this day!", goalNr));
                break;
        }

        acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        return builder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
        }catch(ClassCastException e){};
    }

}
