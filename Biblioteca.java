package com.mycompany.gestionbiblioteca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File; 
public class Biblioteca {
    private String nombre;
    private Bibliotecario bibliotecario;
    private ArrayList<Libro> libros;
    private Map<Usuario, ArrayList<Libro>> prestamos;

    public Biblioteca(String nombre, Bibliotecario bibliotecario) {
        this.nombre = nombre;
        this.bibliotecario = bibliotecario;
        this.libros = new ArrayList<>();
        this.prestamos = new HashMap<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    public void registrarUsuario(Usuario usuario) {
        prestamos.put(usuario, new ArrayList<>());
    }

    public void prestarLibro(Libro libro, Usuario usuario) {
        if (libros.contains(libro) && prestamos.containsKey(usuario) && libro.esPrestable()) {
            libros.remove(libro);
            prestamos.get(usuario).add(libro);
            libro.setDisponible(false);
            System.out.println("El libro '" + libro.getTitulo() + "' ha sido prestado a " + usuario.getNombre());
        } else {
            System.out.println("No se pudo prestar el libro '" + libro.getTitulo() + "' a " + usuario.getNombre() + ". Verifique disponibilidad o registro.");
        }
    }

    public void devolverLibro(Libro libro, Usuario usuario) {
        if (prestamos.containsKey(usuario) && prestamos.get(usuario).contains(libro)) {
            prestamos.get(usuario).remove(libro);
            libros.add(libro);
            libro.setDisponible(true);
            System.out.println("El libro '" + libro.getTitulo() + "' ha sido devuelto por " + usuario.getNombre());
        } else {
            System.out.println("No se pudo devolver el libro '" + libro.getTitulo() + "' de " + usuario.getNombre());
        }
    }
    public Libro buscarLibroPorTitulo(String titulo) {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return libro;
            }
        }
        return null;
    }
    public Libro buscarLibroPrestadoPorUsuario(String titulo, Usuario usuario) {
        if (prestamos.containsKey(usuario)) {
            for (Libro libro : prestamos.get(usuario)) {
                if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                    return libro;
                }
            }
        }
        return null;
    }
    public Usuario buscarUsuarioPorNombre(String nombre) {
        for (Usuario usuario : prestamos.keySet()) {
            if (usuario.getNombre().equalsIgnoreCase(nombre)) {
                return usuario;
            }
        }
        return null;
    }
    public void cargarDatos() {
        File file = new File("biblioteca_datos.txt");
        if (!file.exists()) {
            System.out.println("Archivo de datos no encontrado. Se creará uno nuevo al guardar.");
            return; 
        }
        try (BufferedReader br = new BufferedReader(new FileReader("biblioteca_datos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Libro:")) {
                    String[] parts = linea.split(":")[1].split(",");
                    Libro libro = new Libro(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()));
                    agregarLibro(libro);
                } else if (linea.startsWith("Usuario:")) {
                    String[] parts = linea.split(":")[1].split(",");
                    Usuario usuario = new Usuario(parts[0].trim(), parts[1].trim());
                    registrarUsuario(usuario);
                } else if (linea.startsWith("Prestamo:")) {
                    String[] parts = linea.split(":")[1].split(",");
                    String nombreUsuario = parts[0].trim();
                    String tituloLibro = parts[1].trim();
                    Usuario usuario = buscarUsuarioPorNombre(nombreUsuario);
                    Libro libro = buscarLibroPorTitulo(tituloLibro);
                    if (usuario != null && libro != null) {
                        prestarLibro(libro, usuario);
                    } else {
                        System.out.println("Préstamo ignorado: Usuario o libro no encontrado (" + nombreUsuario + ", " + tituloLibro + ")");
                    }
                }
            }
            System.out.println("Datos cargados exitosamente, incluyendo préstamos.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }
    public void guardarDatos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("biblioteca_datos.txt"))) {
            for (Libro libro : libros) {
                writer.println("Libro:" + libro.getTitulo() + "," + libro.getAutor() + "," + libro.getAnioPublicacion());
            }
            for (Usuario usuario : prestamos.keySet()) {
                writer.println("Usuario:" + usuario.getNombre() + "," + usuario.getEmail());
            }
            for (Map.Entry<Usuario, ArrayList<Libro>> entry : prestamos.entrySet()) {
                Usuario usuario = entry.getKey();
                for (Libro libro : entry.getValue()) {
                    writer.println("Prestamo:" + usuario.getNombre() + "," + libro.getTitulo());
                }
            }
            System.out.println("Datos guardados en biblioteca_datos.txt, incluyendo préstamos.");
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Biblioteca: ").append(nombre).append("\n");
        sb.append("Bibliotecario: ").append(bibliotecario.getNombre()).append(" (").append(bibliotecario.getEmail()).append("), Rol: ").append(bibliotecario.getRol()).append("\n");
        sb.append("Libros disponibles: ").append(libros.size()).append("\n");
        sb.append("Usuarios registrados: ").append(prestamos.size()).append("\n");
        return sb.toString();
    }
}