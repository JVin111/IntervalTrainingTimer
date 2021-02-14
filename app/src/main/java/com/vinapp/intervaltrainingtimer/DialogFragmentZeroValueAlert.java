package com.vinapp.intervaltrainingtimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogFragmentZeroValueAlert extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder zeroValueDialog = new AlertDialog.Builder(getActivity());
        zeroValueDialog.setTitle(R.string.zeroValueAlertTitle)
                .setMessage(R.string.zeroValueAlertMessage)
                .setPositiveButton(R.string.zeroValueAlertOkButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        return zeroValueDialog.create();
    }
}