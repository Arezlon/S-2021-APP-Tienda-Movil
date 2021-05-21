package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class PublicacionImagen implements Serializable {
    public int PublicacionId;
    public String Direccion;
    public int Estado;
    public Date Creacion;
    public Publicacion Publicacion;

    public PublicacionImagen(){}

    public PublicacionImagen(int publicacionId, String direccion, int estado, Date creacion, com.spartano.tiendamovil.model.Publicacion publicacion) {
        PublicacionId = publicacionId;
        Direccion = direccion;
        Estado = estado;
        Creacion = creacion;
        Publicacion = publicacion;
    }

    public int getPublicacionId() {
        return PublicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        PublicacionId = publicacionId;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
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

    public com.spartano.tiendamovil.model.Publicacion getPublicacion() {
        return Publicacion;
    }

    public void setPublicacion(com.spartano.tiendamovil.model.Publicacion publicacion) {
        Publicacion = publicacion;
    }
}
