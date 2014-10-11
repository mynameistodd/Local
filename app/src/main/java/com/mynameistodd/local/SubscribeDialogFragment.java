package com.mynameistodd.local;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParsePush;

/**
 * Created by todd on 10/8/14.
 */
public class SubscribeDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final String businessId = getArguments().getString("businessId");
        final String markerId = getArguments().getString("markerId");
        final Boolean subscribed = getArguments().getBoolean("subscribed");

        final Intent intent = new Intent();
        intent.putExtra("businessId", businessId);
        intent.putExtra("markerId", markerId);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(subscribed ? R.string.dialog_unsubscribe : R.string.dialog_subscribe)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (subscribed) {
                            ParsePush.unsubscribeInBackground(businessId);
                        } else {
                            ParsePush.subscribeInBackground(businessId);
                        }
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, intent);
                    }
                });
        return builder.create();
    }
}