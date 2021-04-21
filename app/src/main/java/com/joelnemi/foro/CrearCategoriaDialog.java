package com.joelnemi.foro;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;


public class CrearCategoriaDialog {

    private static Activity activity;
    private AlertDialog dialog;

    private CrearCategoriaDialog(Activity activity2){
        activity = activity2;
    }

    public void startDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dissmisDialog(){
        dialog.dismiss();
    }


}
