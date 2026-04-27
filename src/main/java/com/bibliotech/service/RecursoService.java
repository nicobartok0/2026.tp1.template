package main.java.com.bibliotech.service;

import main.java.com.bibliotech.model.Categoria;
import main.java.com.bibliotech.model.Ebook;
import main.java.com.bibliotech.model.Libro;
import main.java.com.bibliotech.model.Recurso;
import main.java.com.bibliotech.exception.RecursoNoEncontradoException;

import java.util.List;
import java.util.Optional;

public interface RecursoService {

    void registrarLibro(Libro libro);

    void registrarEbook(Ebook ebook);

    Optional<Recurso> buscarPorIsbn(String isbn);

    List<Recurso> buscarPorTitulo(String titulo);

    List<Recurso> buscarPorAutor(String autor);

    List<Recurso> buscarPorCategoria(Categoria categoria);

    List<Recurso> listarTodos();

    void eliminar(String isbn) throws RecursoNoEncontradoException;
}
