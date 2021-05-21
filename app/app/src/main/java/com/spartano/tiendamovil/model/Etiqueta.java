package com.spartano.tiendamovil.model;

import java.io.Serializable;

public class Etiqueta implements Serializable {
    public String Nombre;
    public int Estado;
    public String Creacion;

    public Etiqueta(){}

    public Etiqueta(String nombre, int estado, String creacion) {
        Nombre = nombre;
        Estado = estado;
        Creacion = creacion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public String getCreacion() {
        return Creacion;
    }

    public void setCreacion(String creacion) {
        Creacion = creacion;
    }
}
