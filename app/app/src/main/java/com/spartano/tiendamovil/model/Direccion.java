package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Direccion implements Serializable {
    public int UsuarioId;
    public String Nombre ;
    public String Pais ;
    public String Provincia ;
    public String Localidad;
    public String Calle;
    public int Numero;
    public int Piso;
    public String Referencia;
    public long Latitud;
    public long Longitud;
    public int Estado;
    public Date Creacion;
    public Usuario Usuario;

    public Direccion(){}

    public Direccion(int usuarioId, String nombre, String pais, String provincia, String localidad, String calle, int numero, int piso, String referencia, long latitud, long longitud, int estado, Date creacion, com.spartano.tiendamovil.model.Usuario usuario) {
        UsuarioId = usuarioId;
        Nombre = nombre;
        Pais = pais;
        Provincia = provincia;
        Localidad = localidad;
        Calle = calle;
        Numero = numero;
        Piso = piso;
        Referencia = referencia;
        Latitud = latitud;
        Longitud = longitud;
        Estado = estado;
        Creacion = creacion;
        Usuario = usuario;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        UsuarioId = usuarioId;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    public int getPiso() {
        return Piso;
    }

    public void setPiso(int piso) {
        Piso = piso;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public long getLatitud() {
        return Latitud;
    }

    public void setLatitud(long latitud) {
        Latitud = latitud;
    }

    public long getLongitud() {
        return Longitud;
    }

    public void setLongitud(long longitud) {
        Longitud = longitud;
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

    public com.spartano.tiendamovil.model.Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(com.spartano.tiendamovil.model.Usuario usuario) {
        Usuario = usuario;
    }
}
