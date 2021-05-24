package com.spartano.tiendamovil.model;

import java.io.Serializable;
import java.util.Date;

public class Publicacion implements Serializable {
    public int id;
    public int usuarioId;
    public String titulo;
    public String descripcion;
    public float precio;
    public int categoria;
    public int tipo;
    public int stock;
    public int estado;
    public String creacion;
    public Usuario usuario;

    public String categoriaNombre;
    public String tipoNombre;

    public Publicacion(){}

    public Publicacion(int id, int usuarioId, String titulo, String descripcion, float precio, int categoria, int tipo, int stock, int estado, String creacion, Usuario usuario, String categoriaNombre, String tipoNombre) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.tipo = tipo;
        this.stock = stock;
        this.estado = estado;
        this.creacion = creacion;
        this.usuario = usuario;

        this.categoriaNombre = categoriaNombre;
        this.tipoNombre = tipoNombre;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }

    public com.spartano.tiendamovil.model.Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(com.spartano.tiendamovil.model.Usuario usuario) {
        this.usuario = usuario;
    }
}
