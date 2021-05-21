package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Publicacion implements Serializable {
    public int UsuarioId;
    public String Titulo;
    public String Descripcion;
    public float Precio;
    public int Categoria;
    public int Tipo;
    public int Stock;
    public int Estado;
    public Date Creacion;
    public Usuario Usuario;
    public String CategoriaNombre;
    public String TipoNombr;

    public Publicacion(){}

    public Publicacion(int usuarioId, String titulo, String descripcion, float precio, int categoria, int tipo, int stock, int estado, Date creacion, com.spartano.tiendamovil.model.Usuario usuario, String categoriaNombre, String tipoNombr) {
        UsuarioId = usuarioId;
        Titulo = titulo;
        Descripcion = descripcion;
        Precio = precio;
        Categoria = categoria;
        Tipo = tipo;
        Stock = stock;
        Estado = estado;
        Creacion = creacion;
        Usuario = usuario;
        CategoriaNombre = categoriaNombre;
        TipoNombr = tipoNombr;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        UsuarioId = usuarioId;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public float getPrecio() {
        return Precio;
    }

    public void setPrecio(float precio) {
        Precio = precio;
    }

    public int getCategoria() {
        return Categoria;
    }

    public void setCategoria(int categoria) {
        Categoria = categoria;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
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

    public String getCategoriaNombre() {
        return CategoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        CategoriaNombre = categoriaNombre;
    }

    public String getTipoNombr() {
        return TipoNombr;
    }

    public void setTipoNombr(String tipoNombr) {
        TipoNombr = tipoNombr;
    }
}
