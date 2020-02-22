package com.readingbuddy.ui.closeup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;

import com.readingbuddy.R;

public class popup_finished extends AppCompatDialogFragment {
    private Toolbar popupToolbar;
    private RatingBar ratingBar;
    private Button acceptButton;
    private Button cancelButton;
    private finished_dialog_listener listener;

    private boolean completed;
    public popup_finished(boolean completed)
    {
        this.completed = completed;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog builder = new AlertDialog.Builder(getActivity(), getTheme()).create();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_rating, null);

        builder.setView(view);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);

        popupToolbar = view.findViewById(R.id.popupToolbar);
        ratingBar = view.findViewById(R.id.rating);
        cancelButton = view.findViewById(R.id.cancel_button);
        acceptButton = view.findViewById(R.id.done_button);

        if (completed)
        {
            popupToolbar.setTitle("Congratulations!");
        }
        else
        {
            popupToolbar.setTitle("Better luck next time");
        }

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = ratingBar.getProgress();
                listener.applyFinished(rating, completed);
                builder.dismiss();
            }
        });
        return builder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (finished_dialog_listener) context;
        }catch(ClassCastException e){};
    }

    public interface finished_dialog_listener
    {
        void applyFinished(int rating, boolean completed);
    }
}
