package com.joelnemi.foro.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Usuario implements Serializable {

    private String userUID;
    private String nombre;
    private String foto;
    private Long fama;
    private Date kholliAge;
    private ArrayList<String> idsPostsGuardados;
    private ArrayList<String> idsPostsSubidos;
    private ArrayList<String> idsUsuariosSeguidos;

    public Usuario() {
        this.userUID = "";
        this.nombre = "Joel369";
        this.foto = "";
        this.fama = 321L;
        this.kholliAge = new Date();
        this.idsPostsGuardados = new ArrayList<>();
        this.idsPostsSubidos = new ArrayList<>();
        this.idsUsuariosSeguidos = new ArrayList<>();
    }

    public Usuario(String userUID, String nombre, String foto){
        this.userUID = userUID;
        this.nombre = nombre;
        this.foto = foto;
        this.fama = 0L;
        this.kholliAge = new Date();
        this.idsPostsGuardados = new ArrayList<>();
        this.idsPostsSubidos = new ArrayList<>();
        this.idsUsuariosSeguidos = new ArrayList<>();

    }

    public Usuario(String userUID, String nombre, String foto, Long fama, Date kholliAge, ArrayList<String> idsPostsGuardados,
                   ArrayList<String> idsPostsSubidos,ArrayList<String> idsUsuariosSeguidos) {
        this.userUID = userUID;
        this.nombre = nombre;
        this.foto = foto;
        this.fama = fama;
        this.kholliAge = kholliAge;
        this.idsPostsGuardados = idsPostsGuardados;
        this.idsPostsSubidos = idsPostsSubidos;
        this.idsUsuariosSeguidos = idsUsuariosSeguidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Long getFama() {
        return fama;
    }

    public void setFama(Long fama) {
        this.fama = fama;
    }

    public Date getKholliAge() {
        return kholliAge;
    }

    public void setKholliAge(Date kholliAge) {
        this.kholliAge = kholliAge;
    }

    public ArrayList<String> getIdsPosts() {
        return idsPostsGuardados;
    }

    public void setIdsPosts(ArrayList<String> idsPosts) {
        this.idsPostsGuardados = idsPosts;
    }

    public ArrayList<String> getIdsPostsGuardados() {
        return idsPostsGuardados;
    }

    public void setIdsPostsGuardados(ArrayList<String> idsPostsGuardados) {
        this.idsPostsGuardados = idsPostsGuardados;
    }

    public ArrayList<String> getIdsPostsSubidos() {
        return idsPostsSubidos;
    }

    public void setIdsPostsSubidos(ArrayList<String> idsPostsSubidos) {
        this.idsPostsSubidos = idsPostsSubidos;
    }

    public ArrayList<String> getIdsUsuariosSeguidos() {
        return idsUsuariosSeguidos;
    }

    public void setIdsUsuariosSeguidos(ArrayList<String> idsUsuariosSeguidos) {
        this.idsUsuariosSeguidos = idsUsuariosSeguidos;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "userUID='" + userUID + '\'' +
                ", nombre='" + nombre + '\'' +
                ", foto='" + foto + '\'' +
                ", fama=" + fama +
                ", kholliAge=" + kholliAge +
                ", idsPostsGuardados=" + idsPostsGuardados +
                ", idsPostsSubidos=" + idsPostsSubidos +
                ", idsUsuariosSeguidos=" + idsUsuariosSeguidos +
                '}';
    }
}
