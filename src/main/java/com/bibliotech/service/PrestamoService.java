package main.java.com.bibliotech.service;

import main.java.com.bibliotech.exception.BibliotecaException;
import main.java.com.bibliotech.model.Prestamo;

import java.util.List;

public interface PrestamoService {

    void realizarPrestamo(String isbn, int socioId) throws BibliotecaException;

    void registrarDevolucion(int prestamoId) throws BibliotecaException;

    List<Prestamo> listarPorSocio(int socioId);

    List<Prestamo> listarActivos();

    List<Prestamo> listarTodos();
}
