
package com.alura.literalura.principal;

import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal {

    @Autowired
    private LibroService service;

    private Scanner sc = new Scanner(System.in);

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- LITERALURA ---");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos en un año");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> service.buscarYGuardar();
                case 2 -> service.listarLibros();
                case 3 -> service.listarAutores();
                case 4 -> service.autoresVivos();
                case 5 -> service.librosPorIdioma();
                case 0 -> System.out.println("Adiós");
                default -> System.out.println("Opción inválida");
            }
        }
    }
}
