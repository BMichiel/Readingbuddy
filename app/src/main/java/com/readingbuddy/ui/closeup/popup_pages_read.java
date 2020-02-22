package com.readingbuddy.ui.closeup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;

import com.readingbuddy.R;

public class popup_pages_read extends AppCompatDialogFragment {
    private Toolbar popupToolbar;
    private EditText currentPage;
    private Button acceptButton;
    private Button cancelButton;
    private pages_read_dialog_listener listener;
    private int minPage;
    private int maxPage;

    public popup_pages_read(int min_page, int max_page)
    {
        minPage = min_page;
        maxPage = max_page;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog builder = new AlertDialog.Builder(getActivity(), getTheme()).create();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_pages_read, null);

        builder.setView(view);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);

        popupToolbar = view.findViewById(R.id.popupToolbar);
        currentPage = view.findViewById(R.id.pages_read);
        cancelButton = view.findViewById(R.id.cancel_button);
        acceptButton = view.findViewById(R.id.done_button);

        popupToolbar.setTitle("What page are you currently on?");

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = currentPage.getText().toString();
                try {
                    if (Integer.parseInt(text) > minPage && Integer.parseInt(text) <= maxPage) {
                        listener.applyPagesRead(currentPage.getText().toString());
                        builder.dismiss();
                    }
                    else Toast.makeText(popup_pages_read.this.getContext(), "Fill in a page number between " + minPage + " and " + maxPage, Toast.LENGTH_LONG).show();
                }catch (NumberFormatException e){
                    Toast.makeText(popup_pages_read.this.getContext(), "Fill in a valid page", Toast.LENGTH_LONG).show();
                }
            }
        });
        return builder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (pages_read_dialog_listener) context;
        }catch(ClassCastException e){};
    }

    public interface pages_read_dialog_listener
    {
        void applyPagesRead(String currentPage);
    }
}
