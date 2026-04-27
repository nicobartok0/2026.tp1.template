package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.EstadoPrestamo;
import main.java.com.bibliotech.model.Prestamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryPrestamoRepository implements PrestamoRepository {

    private final Map<Integer, Prestamo> almacenamiento = new HashMap<>();
    private int proximoId = 1;

    @Override
    public void guardar(Prestamo prestamo) {
        almacenamiento.put(prestamo.id(), prestamo);
    }

    public int generarId() {
        return proximoId++;
    }

    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void eliminar(Integer id) {
        almacenamiento.remove(id);
    }

    @Override
    public List<Prestamo> buscarPorSocio(int socioId) {
        return almacenamiento.values().stream()
                .filter(p -> p.socioId() == socioId)
                .toList();
    }

    @Override
    public List<Prestamo> buscarPorIsbn(String isbn) {
        return almacenamiento.values().stream()
                .filter(p -> p.isbn().equals(isbn))
                .toList();
    }

    @Override
    public List<Prestamo> buscarPorEstado(EstadoPrestamo estado) {
        return almacenamiento.values().stream()
                .filter(p -> p.estado() == estado)
                .toList();
    }
}
