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
    private ArrayList<String> idsPostsVotadosPositivo;
    private ArrayList<String> idsPostsVotadosNegativo;
    private ArrayList<String> idsUsuariosSeguidos;
    private ArrayList<Comentario> comentariosPublicados;

    public Usuario() {
        this.userUID = "";
        this.nombre = "Joel369";
        this.foto = "";
        this.fama = 321L;
        this.kholliAge = new Date();
        this.idsPostsGuardados = new ArrayList<>();
        this.idsPostsVotadosPositivo = new ArrayList<>();
        this.idsUsuariosSeguidos = new ArrayList<>();
    }

    public Usuario(String userUID, String nombre, String foto){
        this.userUID = userUID;
        this.nombre = nombre;
        this.foto = foto;
        this.fama = 0L;
        this.kholliAge = new Date();
        this.idsPostsGuardados = new ArrayList<>();
        this.idsPostsVotadosPositivo = new ArrayList<>();
        this.idsUsuariosSeguidos = new ArrayList<>();
        this.idsPostsVotadosNegativo = new ArrayList<>();
        this.comentariosPublicados = new ArrayList<>();

    }

    public Usuario(String userUID, String nombre, String foto, Long fama,
                   Date kholliAge, ArrayList<String> idsPostsGuardados, ArrayList<String> idsPostsVotadosPositivo,
                   ArrayList<String> idsPostsVotadosNegativo, ArrayList<String> idsUsuariosSeguidos,
                   ArrayList<Comentario> comentariosPublicados) {
        this.userUID = userUID;
        this.nombre = nombre;
        this.foto = foto;
        this.fama = fama;
        this.kholliAge = kholliAge;
        this.idsPostsGuardados = idsPostsGuardados;
        this.idsPostsVotadosPositivo = idsPostsVotadosPositivo;
        this.idsPostsVotadosNegativo = idsPostsVotadosNegativo;
        this.idsUsuariosSeguidos = idsUsuariosSeguidos;
        this.comentariosPublicados = comentariosPublicados;
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


    public ArrayList<String> getIdsPostsGuardados() {
        return idsPostsGuardados;
    }

    public void setIdsPostsGuardados(ArrayList<String> idsPostsGuardados) {
        this.idsPostsGuardados = idsPostsGuardados;
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

    public void setIdsPostsVotadosNegativo(ArrayList<String> idsPostsVotadosNegativo) {
        this.idsPostsVotadosNegativo = idsPostsVotadosNegativo;
    }

    public ArrayList<String> getIdsPostsVotadosNegativo() {
        return idsPostsVotadosNegativo;
    }

    public void setIdsPostsVotadosPositivo(ArrayList<String> idsPostsVotadosPositivo) {
        this.idsPostsVotadosPositivo = idsPostsVotadosPositivo;
    }

    public ArrayList<String> getIdsPostsVotadosPositivo() {
        return idsPostsVotadosPositivo;
    }

    public ArrayList<Comentario> getComentariosPublicados() {
        return comentariosPublicados;
    }

    public void setComentariosPublicados(ArrayList<Comentario> comentariosPublicados) {
        this.comentariosPublicados = comentariosPublicados;
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
                ", idsPostsVotados=" + idsPostsVotadosPositivo +
                ", idsUsuariosSeguidos=" + idsUsuariosSeguidos +
                '}';
    }
}
