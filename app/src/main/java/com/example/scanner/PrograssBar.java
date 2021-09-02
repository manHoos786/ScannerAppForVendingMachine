package com.example.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

class PrograssBar {
    private Activity activity;
    private AlertDialog dialog;

    PrograssBar(Activity myActivity){
        activity = myActivity;
    }

    void startPrograssBar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissPrograssBar(){
        dialog.dismiss();
    }
}
