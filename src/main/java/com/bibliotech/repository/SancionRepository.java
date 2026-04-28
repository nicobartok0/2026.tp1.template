package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Sancion;

import java.util.List;
import java.util.Optional;

public interface SancionRepository extends Repository<Sancion, Integer> {

    Optional<Sancion> buscarSancionActivaPorSocio(int socioId);

    List<Sancion> buscarPorSocio(int socioId);
}
