package com.mycompany.gestionbiblioteca;
public class Bibliotecario extends Persona {
    public Bibliotecario(String nombre, String email) {
        super(nombre, email);
    }
    @Override
    public String getRol() {
        return "Bibliotecario";
    }
}