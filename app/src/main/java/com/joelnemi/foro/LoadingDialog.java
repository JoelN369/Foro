package com.joelnemi.foro;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog {

    private static Activity activity;
    private AlertDialog dialog;
    private static LoadingDialog loadingDialog;
    private static View v;
    private static AlertDialog.Builder builder;

    private LoadingDialog(Activity activity2){
        activity = activity2;
    }

    public static LoadingDialog getInstance(Activity activity2){
        if (activity2 == null){
            createDialog();
            return loadingDialog;
        }

        if (activity != null){
            if (activity.getClass() != activity2.getClass()){
                activity = activity2;
                createDialog();
                return loadingDialog;
            }
            else{
                createDialog();
                return loadingDialog;
            }
        }else{

            loadingDialog = new LoadingDialog(activity2);
            createDialog();
            return loadingDialog;
        }

    }

    private static void createDialog(){
        builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        v = inflater.inflate(R.layout.loading_layout, null);
        builder.setView(v);
        builder.setCancelable(true);
    }

    public void startDialog(){


        dialog = builder.create();
        dialog.show();
    }

    public void dissmisDialog(){
        dialog.dismiss();
    }

    public void changeTextDialog(String text){
        if (v != null) {
            TextView tvLoading;
            tvLoading = v.findViewById(R.id.tvTextLoading);
            tvLoading.setText(text);
        }

    }
}
