package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable {
    public int id;
    public int compraId;
    public int usuarioId;
    public String contenido;
    public int estado;
    public String creacion;
    public Compra compra;
    public Usuario usuario;

    public Mensaje(){}

    public Mensaje(int id, int compraId, int usuarioId, String contenido, int estado, String creacion, com.spartano.tiendamovil.model.Compra compra, com.spartano.tiendamovil.model.Usuario usuario) {
        this.id = id;
        this.compraId = compraId;
        this.usuarioId = usuarioId;
        this.contenido = contenido;
        this.estado = estado;
        this.creacion = creacion;
        this.compra = compra;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompraId() {
        return compraId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
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

    public com.spartano.tiendamovil.model.Compra getCompra() {
        return compra;
    }

    public void setCompra(com.spartano.tiendamovil.model.Compra compra) {
        this.compra = compra;
    }

    public com.spartano.tiendamovil.model.Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(com.spartano.tiendamovil.model.Usuario usuario) {
        this.usuario = usuario;
    }
}
