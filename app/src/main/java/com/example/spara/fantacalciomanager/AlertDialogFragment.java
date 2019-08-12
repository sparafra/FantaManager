package com.example.spara.fantacalciomanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;


    public AlertDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static AlertDialogFragment newInstance(String title) {
        AlertDialogFragment frag = new AlertDialogFragment();
        frag.setCancelable(false);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Nella cartella 'Download' del tuo dispositivo troverai la cartella 'FantaFinando' con i file creati!");
        alertDialogBuilder.setPositiveButton("Ottimo!",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog("Ok");
            }
        });


        return alertDialogBuilder.create();
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    // ...

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // ...
        // 2. Setup a callback when the "Done" button is pressed on keyboard
        mEditText.setOnEditorActionListener(this);
    }

    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog("No");
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

}
