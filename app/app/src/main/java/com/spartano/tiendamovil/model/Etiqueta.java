package com.spartano.tiendamovil.model;

import java.io.Serializable;

public class Etiqueta implements Serializable {
    public int id;
    public String nombre;
    public int estado;
    public String creacion;

    public Etiqueta(){}

    public Etiqueta(int id, String nombre, int estado, String creacion) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.creacion = creacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = this.id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
