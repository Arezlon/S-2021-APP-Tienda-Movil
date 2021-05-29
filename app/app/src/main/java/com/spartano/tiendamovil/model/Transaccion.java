package com.spartano.tiendamovil.model;

import java.io.Serializable;

public class Transaccion implements Serializable {
    public int id;
    public int compraId;
    public int usuarioId;
    public float importe;
    public float balance;
    public int tipo;
    public String tiposNombre;
    public int estado;
    public String creacion;
    public Compra compra;
    public Usuario usuario;

    public Transaccion() {}

    public Transaccion(int id, int compraId, int usuarioId, float importe, float balance, int tipo, String tiposNombre, int estado, String creacion, Compra compra, Usuario usuario) {
        this.id = id;
        this.compraId = compraId;
        this.usuarioId = usuarioId;
        this.importe = importe;
        this.balance = balance;
        this.tipo = tipo;
        this.tiposNombre = tiposNombre;
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

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getTiposNombre() {
        return tiposNombre;
    }

    public void setTiposNombre(String tiposNombre) {
        this.tiposNombre = tiposNombre;
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
