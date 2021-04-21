package com.joelnemi.foro.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Post implements Comparable<Post>, Serializable {

    private String postUID;
    private String texto;
    private String urlFoto;
    private Long valoracion;
    private ArrayList<Comentario> comentarios;
    private String userUID;
    private String categoria;
    private Date fechaPost;

    public Post() {
        this.postUID = "";
        this.texto = "Esto es un error";
        this.urlFoto = "";
        this.valoracion = 404L;
        this.comentarios = new ArrayList<>();
        this.userUID = "o3fqbo404notfound";
        this.categoria = "Error";
        this.fechaPost = new Date();

    }

    public Post(String postUID, String texto, String urlFoto, Long valoracion, ArrayList<Comentario> comentarios, String userUID,
                String categoria, Date fechaPost) {
        this.postUID = postUID;
        this.texto = texto;
        this.urlFoto = urlFoto;
        this.valoracion = valoracion;
        this.comentarios = comentarios;
        this.userUID = userUID;
        this.categoria = categoria;
        this.fechaPost = fechaPost;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Long getValoracion() {
        return valoracion;
    }

    public void setValoracion(Long valoracion) {
        this.valoracion = valoracion;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public String getUser() {
        return userUID;
    }

    public void setUser(String userUID) {
        this.userUID = userUID;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getFechaPost() {
        return fechaPost;
    }

    public void setFechaPost(Date fechaPost) {
        this.fechaPost = fechaPost;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public static Comparator<Post> getMasValorados() {
        return masValorados;
    }

    public static void setMasValorados(Comparator<Post> masValorados) {
        Post.masValorados = masValorados;
    }

    public String getPostUID() {
        return postUID;
    }

    public void setPostUID(String postUID) {
        this.postUID = postUID;
    }


    @Override
    public String toString() {
        return "Post{" +
                "postUID='" + postUID + '\'' +
                ", texto='" + texto + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", valoracion=" + valoracion +
                ", comentarios=" + comentarios +
                ", userUID='" + userUID + '\'' +
                ", categoria='" + categoria + '\'' +
                ", fechaPost=" + fechaPost +
                '}';
    }

    @Override
    public int compareTo(Post o) {
        return o.getFechaPost().compareTo(this.fechaPost);
    }
    public static Comparator<Post> masValorados
            = (o1, o2) -> o1.getValoracion().compareTo(o2.getValoracion());


}

