package main.java.com.bibliotech.service;

import main.java.com.bibliotech.exception.DniDuplicadoException;
import main.java.com.bibliotech.exception.EmailInvalidoException;
import main.java.com.bibliotech.exception.SocioNoEncontradoException;
import main.java.com.bibliotech.model.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService {

    void registrarEstudiante(int id, String nombre, String apellido, String dni, String email)
            throws DniDuplicadoException, EmailInvalidoException;

    void registrarDocente(int id, String nombre, String apellido, String dni, String email)
            throws DniDuplicadoException, EmailInvalidoException;

    Socio buscarPorId(int id) throws SocioNoEncontradoException;

    Optional<Socio> buscarPorDni(String dni);

    List<Socio> listarTodos();
}
