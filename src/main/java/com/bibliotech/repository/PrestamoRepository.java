package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.EstadoPrestamo;
import main.java.com.bibliotech.model.Prestamo;

import java.util.List;

public interface PrestamoRepository extends Repository<Prestamo, Integer> {

    List<Prestamo> buscarPorSocio(int socioId);

    List<Prestamo> buscarPorIsbn(String isbn);

    List<Prestamo> buscarPorEstado(EstadoPrestamo estado);
}
