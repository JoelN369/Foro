package com.joelnemi.foro.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Comunidad implements Serializable {

    private String nombre;
    private Date kholliAge;
    private ArrayList<String> idsPosts;


    public Comunidad() {

        this.nombre = "Kholli";
        this.kholliAge = new Date();
        this.idsPosts = new ArrayList<>();

    }


    public Comunidad(String nombre) {

        this.nombre = nombre;
        this.kholliAge = new Date();
        this.idsPosts = new ArrayList<>();

    }


    public Comunidad(String nombre, Date kholliAge, ArrayList<String> idsPosts) {
        this.nombre = nombre;
        this.kholliAge = kholliAge;
        this.idsPosts = idsPosts;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getKholliAge() {
        return kholliAge;
    }

    public void setKholliAge(Date kholliAge) {
        this.kholliAge = kholliAge;
    }

    public ArrayList<String> getIdsPosts() {
        return idsPosts;
    }

    public void setIdsPosts(ArrayList<String> idsPosts) {
        this.idsPosts = idsPosts;
    }

    @Override
    public String toString() {
        return "Comunidad{" +
                "nombre='" + nombre + '\'' +
                ", kholliAge=" + kholliAge +
                ", idsPosts=" + idsPosts +
                '}';
    }
}
