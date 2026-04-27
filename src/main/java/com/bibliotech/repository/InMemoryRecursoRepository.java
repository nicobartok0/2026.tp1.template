package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Categoria;
import main.java.com.bibliotech.model.Recurso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public class InMemoryRecursoRepository implements RecursoRepository {

    private final Map<String, Recurso> almacenamiento = new HashMap<>();

    @Override
    public void guardar(Recurso recurso) {
        almacenamiento.put(recurso.isbn(), recurso);
    }

    @Override
    public Optional<Recurso> buscarPorId(String isbn) {
        return Optional.ofNullable(almacenamiento.get(isbn));
    }

    @Override
    public List<Recurso> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void eliminar(String isbn) {
        almacenamiento.remove(isbn);
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        String busqueda = titulo.toLowerCase();
        return almacenamiento.values().stream()
                .filter(r -> r.titulo().toLowerCase().contains(busqueda))
                .toList();
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {
        String busqueda = autor.toLowerCase();
        return almacenamiento.values().stream()
                .filter(r -> {
                    if (r instanceof main.java.com.bibliotech.model.Libro l) {
                        return l.autor().toLowerCase().contains(busqueda);
                    } else if (r instanceof main.java.com.bibliotech.model.Ebook e) {
                        return e.autor().toLowerCase().contains(busqueda);
                    }
                    return false;
                })
                .toList();
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return almacenamiento.values().stream()
                .filter(r -> r.categoria() == categoria)
                .toList();
    }
}
