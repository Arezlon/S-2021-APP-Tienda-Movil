package com.spartano.tiendamovil.model;

import java.io.Serializable;

public class Notificacion implements Serializable {

    public int id;
    public int tipo;
    public String mensaje;
    public int usuarioId;
    public Usuario usuario;
    public int compraId;
    public Compra compra;
    public int publicacionId;
    public Publicacion publicacion;
    public int estado;
    public String creacion;

    public Notificacion(){}

    public Notificacion(int id, int tipo, String mensaje, int usuarioId, Usuario usuario, int compraId, Compra compra, int publicacionId, Publicacion publicacion, int estado, String creacion) {
        this.id = id;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.usuarioId = usuarioId;
        this.usuario = usuario;
        this.compraId = compraId;
        this.compra = compra;
        this.publicacionId = publicacionId;
        this.publicacion = publicacion;
        this.estado = estado;
        this.creacion = creacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getCompraId() {
        return compraId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
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
}
