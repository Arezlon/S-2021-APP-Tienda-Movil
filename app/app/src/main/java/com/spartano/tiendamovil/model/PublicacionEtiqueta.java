package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class PublicacionEtiqueta implements Serializable {
    public int id;
    public int publicacionId;
    public int etiquetaId;
    public int estado;
    public String creacion;
    public Publicacion publicacion;
    public Etiqueta etiqueta;

    public PublicacionEtiqueta(){}

    public PublicacionEtiqueta(int id, int publicacionId, int etiquetaId, int estado, String creacion, com.spartano.tiendamovil.model.Publicacion publicacion, com.spartano.tiendamovil.model.Etiqueta etiqueta) {
        this.id = id;
        this.publicacionId = publicacionId;
        this.etiquetaId = etiquetaId;
        this.estado = estado;
        this.creacion = creacion;
        this.publicacion = publicacion;
        this.etiqueta = etiqueta;
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

    public int getEtiquetaId() {
        return etiquetaId;
    }

    public void setEtiquetaId(int etiquetaId) {
        this.etiquetaId = etiquetaId;
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

    public com.spartano.tiendamovil.model.Etiqueta getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(com.spartano.tiendamovil.model.Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }
}
