package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Socio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySocioRepository implements SocioRepository {

    private final Map<Integer, Socio> almacenamiento = new HashMap<>();
    private final Map<String, Integer> indiceDni = new HashMap<>();

    @Override
    public void guardar(Socio socio) {
        almacenamiento.put(socio.getId(), socio);
        indiceDni.put(socio.getDni(), socio.getId());
    }

    @Override
    public boolean existeDni(String dni) {
        return indiceDni.containsKey(dni);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return Optional.ofNullable(indiceDni.get(dni))
                .flatMap(id -> Optional.ofNullable(almacenamiento.get(id)));
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void eliminar(Integer id) {
        Socio socio = almacenamiento.remove(id);
        if (socio != null) {
            indiceDni.remove(socio.getDni());
        }
    }
}
