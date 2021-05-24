package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Compra implements Serializable {
    public int id;
    public int usuarioId;
    public int publicacionId;
    public int cantidad;
    public float precio;
    public int estado;
    public String creacion;
    public Usuario usuario;
    public Publicacion publicacion;

    public Compra(){}

    public Compra(int id, int usuarioId, int publicacionId, int cantidad, float precio, int estado, String creacion, com.spartano.tiendamovil.model.Usuario usuario, com.spartano.tiendamovil.model.Publicacion publicacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.publicacionId = publicacionId;
        this.cantidad = cantidad;
        this.precio = precio;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
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

    public com.spartano.tiendamovil.model.Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(com.spartano.tiendamovil.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.spartano.tiendamovil.model.Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(com.spartano.tiendamovil.model.Publicacion publicacion) {
        this.publicacion = publicacion;
    }
}
