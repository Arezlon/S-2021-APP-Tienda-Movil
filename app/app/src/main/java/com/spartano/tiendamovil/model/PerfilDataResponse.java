package com.spartano.tiendamovil.model;

public class PerfilDataResponse {
    Usuario usuario;
    int valoracion;
    int cantidadReseñas;
    int cantidadVentas;
    String reputacion;

    public String getReputacion() {
        return reputacion;
    }

    public void setReputacion(String reputacion) {
        this.reputacion = reputacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public int getCantidadReseñas() {
        return cantidadReseñas;
    }

    public void setCantidadReseñas(int cantidadReseñas) {
        this.cantidadReseñas = cantidadReseñas;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(int cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }
}
