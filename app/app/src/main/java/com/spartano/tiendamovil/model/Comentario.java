package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Comentario implements Serializable {
    public int UsuarioId;
    public int PublicacionId;
    public int UsuarioRespuestaId;
    public String Contenido;
    public int Estado;
    public Date Creacion;
    public Usuario Usuario;
    public Publicacion Publicacion;

    public Comentario(){}

    public Comentario(int usuarioId, int publicacionId, int usuarioRespuestaId, String contenido, int estado, Date creacion, com.spartano.tiendamovil.model.Usuario usuario, com.spartano.tiendamovil.model.Publicacion publicacion) {
        UsuarioId = usuarioId;
        PublicacionId = publicacionId;
        UsuarioRespuestaId = usuarioRespuestaId;
        Contenido = contenido;
        Estado = estado;
        Creacion = creacion;
        Usuario = usuario;
        Publicacion = publicacion;
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

    public int getUsuarioRespuestaId() {
        return UsuarioRespuestaId;
    }

    public void setUsuarioRespuestaId(int usuarioRespuestaId) {
        UsuarioRespuestaId = usuarioRespuestaId;
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

    public com.spartano.tiendamovil.model.Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(com.spartano.tiendamovil.model.Usuario usuario) {
        Usuario = usuario;
    }

    public com.spartano.tiendamovil.model.Publicacion getPublicacion() {
        return Publicacion;
    }

    public void setPublicacion(com.spartano.tiendamovil.model.Publicacion publicacion) {
        Publicacion = publicacion;
    }
}
