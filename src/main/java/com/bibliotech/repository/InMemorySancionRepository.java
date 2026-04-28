package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Sancion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySancionRepository implements SancionRepository {

    private final Map<Integer, List<Sancion>> almacenamiento = new HashMap<>();
    private int proximoId = 1;

    public int generarId() {
        return proximoId++;
    }

    @Override
    public void guardar(Sancion sancion) {
        almacenamiento
                .computeIfAbsent(sancion.socioId(), k -> new ArrayList<>())
                .add(sancion);
    }

    @Override
    public Optional<Sancion> buscarPorId(Integer id) {
        return almacenamiento.values().stream()
                .flatMap(List::stream)
                .filter(s -> almacenamiento
                        .getOrDefault(s.socioId(), List.of()).indexOf(s) + 1 == id)
                .findFirst();
    }

    @Override
    public List<Sancion> buscarTodos() {
        return almacenamiento.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public void eliminar(Integer id) {
        // Las sanciones no se eliminan, son históricas
    }

    @Override
    public Optional<Sancion> buscarSancionActivaPorSocio(int socioId) {
        return almacenamiento.getOrDefault(socioId, List.of()).stream()
                .filter(Sancion::estaActiva)
                .findFirst();
    }

    @Override
    public List<Sancion> buscarPorSocio(int socioId) {
        return new ArrayList<>(almacenamiento.getOrDefault(socioId, List.of()));
    }
}
