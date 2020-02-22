package com.readingbuddy.ui.reading_sessions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.readingbuddy.R;
import com.readingbuddy.readMoment;

public class popup_edit_session extends AppCompatDialogFragment {
    private Toolbar popupTitle;
    private EditText pagesRead;
    private EditText timeRead;
    private Button acceptButton;
    private Button cancelButton;
    private Button deleteButton;
    private editSessionListener listener;

    private readMoment currentMoment;
    private int position;

    public popup_edit_session(readMoment currentMoment, int position)
    {
        this.currentMoment = currentMoment;
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog builder = new AlertDialog.Builder(getActivity(), getTheme()).create();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_edit_session, null);

        builder.setView(view);

        popupTitle = (Toolbar) view.findViewById(R.id.popupToolbar);
        pagesRead = view.findViewById(R.id.pages_read);
        timeRead = view.findViewById(R.id.time_read);

        cancelButton = view.findViewById(R.id.cancel_button);
        acceptButton = view.findViewById(R.id.done_button);
        deleteButton = view.findViewById(R.id.delete_button);

        popupTitle.setTitle(currentMoment.readDate);
        pagesRead.setHint(String.valueOf(currentMoment.readPages));
        timeRead.setHint(currentMoment.getTimeString());
        pagesRead.setText(String.valueOf(currentMoment.readPages));
        timeRead.setText(currentMoment.getTimeString());

        timeRead.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    acceptButton.callOnClick();
                }
                return false;
            }
        });

        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onDeleteButtonPressed(position);
                builder.dismiss();
            }
        });

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pagesReadStr = pagesRead.getText().toString();
                String timeReadStr = timeRead.getText().toString();
                long hrs;
                int min;
                int sec;
                try
                {
                    hrs = Long.parseLong(timeReadStr.substring(0, timeReadStr.indexOf("h")));
                    timeReadStr = timeReadStr.substring(timeReadStr.indexOf("h") + 2);
                    min = Integer.parseInt(timeReadStr.substring(0, timeReadStr.indexOf("m")));
                    timeReadStr = timeReadStr.substring(timeReadStr.indexOf("m") + 2);
                    sec = Integer.parseInt(timeReadStr.substring(0, timeReadStr.indexOf("s")));

                    long time = (hrs * 3600) + (min * 60) + sec;

                    currentMoment.readPages = Integer.parseInt(pagesReadStr);
                    currentMoment.readTime = time;
                    listener.onEditWindowClosed(currentMoment, position);
                    builder.dismiss();
                }catch (NumberFormatException e){Toast.makeText(popup_edit_session.this.getContext(), "Arguments invalid", Toast.LENGTH_LONG).show();};

            }
        });
        return builder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (editSessionListener) context;
        }catch(ClassCastException e){};
    }

    public interface editSessionListener
    {
        void onEditWindowClosed(readMoment newMoment, int position);
        void onDeleteButtonPressed(int position);
    }
}
