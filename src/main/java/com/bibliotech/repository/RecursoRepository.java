package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Categoria;
import main.java.com.bibliotech.model.Recurso;

import java.util.List;

public interface RecursoRepository extends Repository<Recurso, String> {

    List<Recurso> buscarPorTitulo(String titulo);

    List<Recurso> buscarPorAutor(String autor);

    List<Recurso> buscarPorCategoria(Categoria categoria);
}
