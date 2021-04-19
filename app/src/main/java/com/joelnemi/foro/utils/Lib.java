package com.joelnemi.foro.utils;


import com.joelnemi.foro.models.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Lib {

    private static Usuario usuario;
    public static String getTimeElapsed(Date fechaP){
        String fecha = "Ahora";
        Date fechaActual = new Date();

        SimpleDateFormat sdf;

        Calendar calPost = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        calPost.setTime(fechaP);
        Calendar calNow = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        calNow.setTime(fechaActual);

        Long millis = calNow.getTimeInMillis() - calPost.getTimeInMillis();


        if ((millis - 1000) > 0) {
            fecha = "Ahora";
        }

        if ((millis - 60000) > 0) {
            fecha = (((millis - 60000) / 60000)+1) + "min";
        }

        if ((millis - 3600000) > 0) {
            fecha = " hace " + (millis / 3600000 ) + " h";
        }
        if ((millis - 86400000) > 0) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            fecha = sdf.format(fechaP);
        }

        return fecha;
    }

}
