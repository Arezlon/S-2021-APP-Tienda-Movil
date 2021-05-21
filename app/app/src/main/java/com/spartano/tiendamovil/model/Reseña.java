package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Reseña implements Serializable {
    public int UsuarioId;
    public int PublicacionId;
    public int Puntaje;
    public String Encabezado;
    public String Contenido;
    public int Estado;
    public Date Creacion;
    public Usuario usuario;
    public Publicacion publicacion;

    public Reseña(){}

    public Reseña(int usuarioId, int publicacionId, int puntaje, String encabezado, String contenido, int estado, Date creacion, Usuario usuario, Publicacion publicacion) {
        UsuarioId = usuarioId;
        PublicacionId = publicacionId;
        Puntaje = puntaje;
        Encabezado = encabezado;
        Contenido = contenido;
        Estado = estado;
        Creacion = creacion;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        UsuarioId = usuarioId;
    }

    public int getPublicacionId() {
        return PublicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        PublicacionId = publicacionId;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }

    public String getEncabezado() {
        return Encabezado;
    }

    public void setEncabezado(String encabezado) {
        Encabezado = encabezado;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public Date getCreacion() {
        return Creacion;
    }

    public void setCreacion(Date creacion) {
        Creacion = creacion;
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
