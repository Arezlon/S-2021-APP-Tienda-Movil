package com.spartano.tiendamovil.model;

public class LoginResponse {
    private int statusCode;
    private String token;
    private Usuario usuario;

    public LoginResponse() {
    }

    public LoginResponse(int statusCode, String token, Usuario usuario) {
        this.statusCode = statusCode;
        this.token = token;
        this.usuario = usuario;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
