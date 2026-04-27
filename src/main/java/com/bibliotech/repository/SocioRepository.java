package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Socio;

import java.util.Optional;

public interface SocioRepository extends Repository<Socio, Integer> {

    boolean existeDni(String dni);

    Optional<Socio> buscarPorDni(String dni);
}
