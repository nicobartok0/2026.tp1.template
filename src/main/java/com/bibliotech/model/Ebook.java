package main.java.com.bibliotech.model;

public record Ebook(
        String isbn,
        String titulo,
        String autor,
        int anio,
        String formatoArchivo,
        double tamanioMb,
        Categoria categoria
) implements Recurso {}
