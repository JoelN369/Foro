package com.joelnemi.foro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Post implements Comparable<Post>, Serializable {


    private String texto;
    private String urlFoto;
    private Long valoracion;
    private ArrayList<Comentario> comentarios;
    private String userUID;
    private String categoria;
    private Date fechaPost;

    public Post() {
        this.texto = "Esto es un error";
        this.urlFoto = "";
        this.valoracion = 404L;
        this.comentarios = new ArrayList<>();
        this.userUID = "o3fqbo404notfound";
        this.categoria = "Error";
        this.fechaPost = new Date();

    }

    public Post(String texto, String urlFoto, Long valoracion, ArrayList<Comentario> comentarios, String userUID,
                String categoria, Date fechaPost) {
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

    @Override
    public String toString() {
        return "Post{" +
                "texto='" + texto + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", valoracion=" + valoracion +
                ", comentarios=" + comentarios +
                ", userName='" + userUID + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }

    @Override
    public int compareTo(Post o) {
        return this.fechaPost.compareTo(o.getFechaPost());
    }
    public static Comparator<Post> masValorados
            = new Comparator<Post>(){


        @Override
        public int compare(Post o1, Post o2) {
            return o1.getValoracion().compareTo(o2.getValoracion());

        }

    };


}

