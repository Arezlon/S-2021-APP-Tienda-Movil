package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Reseña implements Serializable {
    public int id;
    public int usuarioId;
    public int publicacionId;
    public float puntaje;
    public String encabezado;
    public String contenido;
    public int estado;
    public String creacion;
    public Usuario usuario;
    public Publicacion publicacion;

    public Reseña(){}

    public Reseña(int id, int usuarioId, int publicacionId, float puntaje, String encabezado, String contenido, int estado, String creacion, Usuario usuario, Publicacion publicacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.publicacionId = publicacionId;
        this.puntaje = puntaje;
        this.encabezado = encabezado;
        this.contenido = contenido;
        this.estado = estado;
        this.creacion = creacion;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(float puntaje) {
        this.puntaje = puntaje;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCreacion() {
        return creacion;
    }

    public void setCreacion(String creacion) {
        this.creacion = creacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }
}
