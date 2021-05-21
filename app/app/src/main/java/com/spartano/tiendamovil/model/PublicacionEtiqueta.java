package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class PublicacionEtiqueta implements Serializable {
    public int PublicacionId;
    public int EtiquetaId;
    public int Estado;
    public Date Creacion;
    public Publicacion Publicacion;
    public Etiqueta Etiqueta;

    public PublicacionEtiqueta(){}

    public PublicacionEtiqueta(int publicacionId, int etiquetaId, int estado, Date creacion, com.spartano.tiendamovil.model.Publicacion publicacion, com.spartano.tiendamovil.model.Etiqueta etiqueta) {
        PublicacionId = publicacionId;
        EtiquetaId = etiquetaId;
        Estado = estado;
        Creacion = creacion;
        Publicacion = publicacion;
        Etiqueta = etiqueta;
    }

    public int getPublicacionId() {
        return PublicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        PublicacionId = publicacionId;
    }

    public int getEtiquetaId() {
        return EtiquetaId;
    }

    public void setEtiquetaId(int etiquetaId) {
        EtiquetaId = etiquetaId;
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

    public com.spartano.tiendamovil.model.Etiqueta getEtiqueta() {
        return Etiqueta;
    }

    public void setEtiqueta(com.spartano.tiendamovil.model.Etiqueta etiqueta) {
        Etiqueta = etiqueta;
    }
}
