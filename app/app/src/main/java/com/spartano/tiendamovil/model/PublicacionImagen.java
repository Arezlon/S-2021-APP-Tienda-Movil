package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class PublicacionImagen implements Serializable {
    public int id;
    public int publicacionId;
    public String direccion;
    public int estado;
    public String creacion;
    public Publicacion publicacion;

    public PublicacionImagen(){}

    public PublicacionImagen(int id, int publicacionId, String direccion, int estado, String creacion, com.spartano.tiendamovil.model.Publicacion publicacion) {
        this.id = id;
        this.publicacionId = publicacionId;
        this.direccion = direccion;
        this.estado = estado;
        this.creacion = creacion;
        this.publicacion = publicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public com.spartano.tiendamovil.model.Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(com.spartano.tiendamovil.model.Publicacion publicacion) {
        this.publicacion = publicacion;
    }
}
