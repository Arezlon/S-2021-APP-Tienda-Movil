package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Compra implements Serializable {
    public int UsuarioId;
    public int PublicacionId;
    public int Cantidad;
    public float Precio;
    public int Estado;
    public Date Creacion;
    public Usuario Usuario;
    public Publicacion Publicacion;

    public Compra(){}

    public Compra(int usuarioId, int publicacionId, int cantidad, float precio, int estado, Date creacion, com.spartano.tiendamovil.model.Usuario usuario, com.spartano.tiendamovil.model.Publicacion publicacion) {
        UsuarioId = usuarioId;
        PublicacionId = publicacionId;
        Cantidad = cantidad;
        Precio = precio;
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

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public float getPrecio() {
        return Precio;
    }

    public void setPrecio(float precio) {
        Precio = precio;
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
