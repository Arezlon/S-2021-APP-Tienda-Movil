package com.spartano.tiendamovil.model;

import java.util.Date;

public class Usuario {
    public String Nombre;
    public String Apellido;
    public String Dni;
    public String Email;
    public String Telefono;
    public String Clave;
    public int Permisos;
    public int Estado;
    public Date Creacion;
    public String PermisosNombre;

    public Usuario(){}

    public Usuario(String nombre, String apellido, String dni, String email, String telefono, String clave, int permisos, int estado, Date creacion, String permisosNombre) {
        Nombre = nombre;
        Apellido = apellido;
        Dni = dni;
        Email = email;
        Telefono = telefono;
        Clave = clave;
        Permisos = permisos;
        Estado = estado;
        Creacion = creacion;
        PermisosNombre = permisosNombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public int getPermisos() {
        return Permisos;
    }

    public void setPermisos(int permisos) {
        Permisos = permisos;
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

    public String getPermisosNombre() {
        return PermisosNombre;
    }

    public void setPermisosNombre(String permisosNombre) {
        PermisosNombre = permisosNombre;
    }
}
