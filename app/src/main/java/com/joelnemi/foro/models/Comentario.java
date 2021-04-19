package com.joelnemi.foro.models;

import java.io.Serializable;
import java.util.Date;

public class Comentario implements Serializable {
    private String userUID;
    private String comentario;
    private Date fecha;
    private Long valoraciones;
    public Comentario() {
        this.userUID = "Kholli";
        this.comentario = "Error";
        this.fecha = new Date();
        this.valoraciones = 0L;
    }
    public Comentario(String username, String comentario, Date fecha, Long valoraciones) {
        this.userUID = username;
        this.comentario = comentario;
        this.valoraciones = valoraciones;
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(Long valoraciones) {
        this.valoraciones = valoraciones;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "userUID='" + userUID + '\'' +
                ", comentario='" + comentario + '\'' +
                ", fecha=" + fecha +
                ", valoraciones=" + valoraciones +
                '}';
    }
}
