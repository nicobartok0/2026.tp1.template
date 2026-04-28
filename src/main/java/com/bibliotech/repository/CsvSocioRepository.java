package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Docente;
import main.java.com.bibliotech.model.Estudiante;
import main.java.com.bibliotech.model.Socio;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvSocioRepository implements SocioRepository {

    private static final String ARCHIVO  = "datos/socios.csv";
    private static final String CABECERA = "tipo,id,nombre,apellido,dni,email";

    @Override
    public void guardar(Socio socio) {
        Map<Integer, Socio> datos = cargarTodos();
        datos.put(socio.getId(), socio);
        persistir(datos);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return Optional.ofNullable(cargarTodos().get(id));
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(cargarTodos().values());
    }

    @Override
    public void eliminar(Integer id) {
        Map<Integer, Socio> datos = cargarTodos();
        datos.remove(id);
        persistir(datos);
    }

    @Override
    public boolean existeDni(String dni) {
        return cargarTodos().values().stream()
                .anyMatch(s -> s.getDni().equals(dni));
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return cargarTodos().values().stream()
                .filter(s -> s.getDni().equals(dni))
                .findFirst();
    }

    // -------------------------------------------------------
    // Helpers CSV
    // -------------------------------------------------------

    private Map<Integer, Socio> cargarTodos() {
        Map<Integer, Socio> mapa = new LinkedHashMap<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return mapa;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.isBlank()) continue;
                Socio s = parsearLinea(linea);
                if (s != null) mapa.put(s.getId(), s);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo socios.csv: " + e.getMessage());
        }
        return mapa;
    }

    private void persistir(Map<Integer, Socio> datos) {
        try {
            Files.createDirectories(Path.of("datos"));
            try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
                pw.println(CABECERA);
                for (Socio s : datos.values()) {
                    pw.println(serializar(s));
                }
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo socios.csv: " + e.getMessage());
        }
    }

    private String serializar(Socio s) {
        String tipo = (s instanceof Docente) ? "DOCENTE" : "ESTUDIANTE";
        return String.join(",",
                tipo,
                String.valueOf(s.getId()),
                s.getNombre(),
                s.getApellido(),
                s.getDni(),
                s.getEmail());
    }

    private Socio parsearLinea(String linea) {
        String[] campos = linea.split(",", -1);
        if (campos.length < 6) return null;
        try {
            String tipo     = campos[0].trim();
            int id          = Integer.parseInt(campos[1].trim());
            String nombre   = campos[2].trim();
            String apellido = campos[3].trim();
            String dni      = campos[4].trim();
            String email    = campos[5].trim();

            if ("DOCENTE".equals(tipo))      return new Docente(id, nombre, apellido, dni, email);
            if ("ESTUDIANTE".equals(tipo))   return new Estudiante(id, nombre, apellido, dni, email);
        } catch (Exception e) {
            System.err.println("Error parseando línea de socio: " + linea);
        }
        return null;
    }
}
