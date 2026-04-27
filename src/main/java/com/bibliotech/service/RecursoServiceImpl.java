package main.java.com.bibliotech.service;

import main.java.com.bibliotech.exception.RecursoNoEncontradoException;
import main.java.com.bibliotech.model.Categoria;
import main.java.com.bibliotech.model.Ebook;
import main.java.com.bibliotech.model.Libro;
import main.java.com.bibliotech.model.Recurso;
import main.java.com.bibliotech.repository.RecursoRepository;

import java.util.List;
import java.util.Optional;

public class RecursoServiceImpl implements RecursoService {

    private final RecursoRepository recursoRepository;

    public RecursoServiceImpl(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @Override
    public void registrarLibro(Libro libro) {
        recursoRepository.guardar(libro);
    }

    @Override
    public void registrarEbook(Ebook ebook) {
        recursoRepository.guardar(ebook);
    }

    @Override
    public Optional<Recurso> buscarPorIsbn(String isbn) {
        return recursoRepository.buscarPorId(isbn);
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        return recursoRepository.buscarPorTitulo(titulo);
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {
        return recursoRepository.buscarPorAutor(autor);
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return recursoRepository.buscarPorCategoria(categoria);
    }

    @Override
    public List<Recurso> listarTodos() {
        return recursoRepository.buscarTodos();
    }

    @Override
    public void eliminar(String isbn) throws RecursoNoEncontradoException {
        recursoRepository.buscarPorId(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException(isbn));
        recursoRepository.eliminar(isbn);
    }
}
