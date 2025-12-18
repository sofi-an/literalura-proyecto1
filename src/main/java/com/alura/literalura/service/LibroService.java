
package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepo;
    @Autowired
    private AutorRepository autorRepo;

    private Scanner sc = new Scanner(System.in);
    private ObjectMapper mapper = new ObjectMapper();

    public void buscarYGuardar() {
        try {
            System.out.print("Título: ");
            String titulo = sc.nextLine();
            String url = "https://gutendex.com/books/?search=" + titulo.replace(" ", "%20");
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(res.body()).get("results");
            if (root.isEmpty()) {
                System.out.println("Libro no encontrado");
                return;
            }
            JsonNode b = root.get(0);
            String t = b.get("title").asText();
            if (libroRepo.existsByTitulo(t)) {
                System.out.println("El libro ya existe");
                return;
            }
            JsonNode a = b.get("authors").get(0);
            String nombre = a.get("name").asText();
            Integer nac = a.get("birth_year").isNull()? null : a.get("birth_year").asInt();
            Integer fal = a.get("death_year").isNull()? null : a.get("death_year").asInt();

            Autor autor = autorRepo.findByNombre(nombre);
            if (autor == null) autor = autorRepo.save(new Autor(nombre, nac, fal));

            String idioma = b.get("languages").get(0).asText();
            int desc = b.get("download_count").asInt();

            libroRepo.save(new Libro(t, idioma, desc, autor));
            System.out.println("Guardado: " + t);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarLibros() {
        libroRepo.findAll().forEach(l -> System.out.println(l.getTitulo()));
    }

    public void listarAutores() {
        autorRepo.findAll().forEach(a -> System.out.println(a.getNombre()));
    }

    public void autoresVivos() {
        System.out.print("Año: ");
        int y = Integer.parseInt(sc.nextLine());
        autorRepo.findAll().stream()
                .filter(a -> a.getNacimiento()!=null && a.getNacimiento()<=y && (a.getFallecimiento()==null || a.getFallecimiento()>y))
                .forEach(a -> System.out.println(a.getNombre()));
    }

    public void librosPorIdioma() {
        System.out.print("Idioma (ES/EN/FR/PT): ");
        String i = sc.nextLine();
        libroRepo.findAll().stream().filter(l -> l.getIdioma().equalsIgnoreCase(i))
                .forEach(l -> System.out.println(l.getTitulo()));
    }
}
