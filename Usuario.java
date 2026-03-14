package com.mycompany.gestionbiblioteca;
public class Usuario extends Persona {
    public Usuario(String nombre, String email) {
        super(nombre, email);
    }

    @Override
    public String getRol() {
        return "Usuario";
    }
}