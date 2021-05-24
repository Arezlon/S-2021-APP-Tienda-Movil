package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Direccion implements Serializable {
    public int id;
    public int usuarioId;
    public String nombre;
    public String pais;
    public String provincia;
    public String localidad;
    public String calle;
    public int numero;
    public int piso;
    public String referencia;
    public long latitud;
    public long longitud;
    public int estado;
    public String creacion;
    public Usuario usuario;

    public Direccion(){}

    public Direccion(int id, int usuarioId, String nombre, String pais, String provincia, String localidad, String calle, int numero, int piso, String referencia, long latitud, long longitud, int estado, String creacion, com.spartano.tiendamovil.model.Usuario usuario) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.numero = numero;
        this.piso = piso;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.creacion = creacion;
        this.usuario = usuario;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public long getLatitud() {
        return latitud;
    }

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public long getLongitud() {
        return longitud;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
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
}
