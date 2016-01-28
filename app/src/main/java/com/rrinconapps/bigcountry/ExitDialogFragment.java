package com.rrinconapps.bigcountry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Models a dialog asking user's confirmation for exit.
 *
 * @author  Ricardo Rincon
 * @since 2016-01-27
 */
public class ExitDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.exit_confirmation_question)
                .setTitle(R.string.app_name)
                .setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Leave the app
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.negative_button_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
