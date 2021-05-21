package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable {
    public int CompraId;
    public int UsuarioId;
    public String Contenido;
    public int Estado;
    public Date Creacion;
    public Compra Compra;
    public Usuario Usuario;

    public Mensaje(){}

    public Mensaje(int compraId, int usuarioId, String contenido, int estado, Date creacion, com.spartano.tiendamovil.model.Compra compra, com.spartano.tiendamovil.model.Usuario usuario) {
        CompraId = compraId;
        UsuarioId = usuarioId;
        Contenido = contenido;
        Estado = estado;
        Creacion = creacion;
        Compra = compra;
        Usuario = usuario;
    }

    public int getCompraId() {
        return CompraId;
    }

    public void setCompraId(int compraId) {
        CompraId = compraId;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        UsuarioId = usuarioId;
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

    public com.spartano.tiendamovil.model.Compra getCompra() {
        return Compra;
    }

    public void setCompra(com.spartano.tiendamovil.model.Compra compra) {
        Compra = compra;
    }

    public com.spartano.tiendamovil.model.Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(com.spartano.tiendamovil.model.Usuario usuario) {
        Usuario = usuario;
    }
}
