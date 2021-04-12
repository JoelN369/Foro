package com.joelnemi.foro.utils;

import androidx.annotation.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joelnemi.foro.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Lib {

    private static Usuario usuario;
    public static String getTimeElapsed(Date fechaP){
        String fecha = "";
        Date fechaActual = new Date();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));

        Calendar calPost = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        calPost.setTime(fechaP);
        Calendar calNow = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        calNow.setTime(fechaActual);

        Long millis = calNow.getTimeInMillis() - calPost.getTimeInMillis();

        if ((millis - 60000) > 0) {
            fecha = ((millis - 60000) / 60000) + "min";
        }

        if ((millis - 3600000) > 0) {
            fecha = " hace " + (millis / 3600000 ) + " h";
        }
        if ((millis - 86400000) > 0) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            fecha = sdf.format(fechaP);
        }

        if (fecha.equals(""))
            fecha = sdf.format(fechaP);

        return fecha;
    }

}
