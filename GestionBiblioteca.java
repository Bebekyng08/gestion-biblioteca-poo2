package com.mycompany.gestionbiblioteca;

import java.util.Scanner;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
public class GestionBiblioteca {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PrintStream out;
        try {
            out = new PrintStream(System.out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            out = System.out;
        }
        Bibliotecario bibliotecario1 = new Bibliotecario("Ana Lopez", "analopez@biblioteca.com");
        Biblioteca biblioteca = new Biblioteca("Biblioteca Municipal", bibliotecario1);     
        biblioteca.cargarDatos();

        boolean salir = false;
        while (!salir) {
            out.println("\n--- Menú de Gestión de Biblioteca ---");
            out.println("1. Agregar Libro");
            out.println("2. Registrar Usuario");
            out.println("3. Prestar Libro");
            out.println("4. Devolver Libro");
            out.println("5. Mostrar Información de la Biblioteca");
            out.println("6. Salir y Guardar Datos");
            out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        out.print("Título del libro: ");
                        String titulo = scanner.nextLine();
                        out.print("Autor: ");
                        String autor = scanner.nextLine();
                        out.print("Año de publicación: ");
                        int anio = Integer.parseInt(scanner.nextLine());
                        Libro nuevoLibro = new Libro(titulo, autor, anio);
                        biblioteca.agregarLibro(nuevoLibro);
                        out.println("Libro agregado exitosamente.");
                        break;
                    case 2:
                        out.print("Nombre del usuario: ");
                        String nombre = scanner.nextLine();
                        out.print("Email: ");
                        String email = scanner.nextLine();
                        Usuario nuevoUsuario = new Usuario(nombre, email);
                        biblioteca.registrarUsuario(nuevoUsuario);
                        out.println("Usuario registrado exitosamente.");
                        break;
                    case 3:
                        out.print("Título del libro a prestar: ");
                        String tituloPrestamo = scanner.nextLine();
                        out.print("Nombre del usuario: ");
                        String nombreUsuario = scanner.nextLine();
                        Libro libroPrestamo = biblioteca.buscarLibroPorTitulo(tituloPrestamo);
                        Usuario usuarioPrestamo = biblioteca.buscarUsuarioPorNombre(nombreUsuario);
                        if (libroPrestamo != null && usuarioPrestamo != null) {
                            biblioteca.prestarLibro(libroPrestamo, usuarioPrestamo);
                        } else {
                            out.println("Libro o usuario no encontrado.");
                        }
                        break;
                    case 4:
                        out.print("Título del libro a devolver: ");
                        String tituloDevolucion = scanner.nextLine();
                        out.print("Nombre del usuario: ");
                        String nombreUsuarioDev = scanner.nextLine();
                        Usuario usuarioDevolucion = biblioteca.buscarUsuarioPorNombre(nombreUsuarioDev);
                        if (usuarioDevolucion != null) {
                            Libro libroDevolucion = biblioteca.buscarLibroPrestadoPorUsuario(tituloDevolucion, usuarioDevolucion);
                            if (libroDevolucion != null) {
                                biblioteca.devolverLibro(libroDevolucion, usuarioDevolucion);
                            } else {
                                out.println("El libro no se encontró en los préstamos de este usuario.");
                            }
                        } else {
                            out.println("Usuario no encontrado.");
                        }
                        break;
                    case 5:
                        out.println(biblioteca.toString());
                        break;
                    case 6:
                        salir = true;
                        biblioteca.guardarDatos();
                        out.println("Datos guardados. Saliendo...");
                        break;
                    default:
                        out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                out.println("Error: Ingrese un número válido para la opción o año.");
            } catch (Exception e) {
                out.println("Error inesperado: " + e.getMessage());
            }
        }
        scanner.close();
    }
}