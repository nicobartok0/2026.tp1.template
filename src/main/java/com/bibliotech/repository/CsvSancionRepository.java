package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Sancion;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class CsvSancionRepository implements SancionRepository {

    private static final String ARCHIVO  = "datos/sanciones.csv";
    private static final String CABECERA = "socioId,fechaInicio,fechaFin,diasRetraso";

    @Override
    public void guardar(Sancion sancion) {
        List<Sancion> datos = cargarLista();
        datos.add(sancion);
        persistir(datos);
    }

    @Override
    public Optional<Sancion> buscarPorId(Integer id) {
        List<Sancion> lista = cargarLista();
        if (id < 1 || id > lista.size()) return Optional.empty();
        return Optional.of(lista.get(id - 1));
    }

    @Override
    public List<Sancion> buscarTodos() {
        return cargarLista();
    }

    @Override
    public void eliminar(Integer id) {
        // Las sanciones son históricas, no se eliminan
    }

    @Override
    public Optional<Sancion> buscarSancionActivaPorSocio(int socioId) {
        return cargarLista().stream()
                .filter(s -> s.socioId() == socioId && s.estaActiva())
                .findFirst();
    }

    @Override
    public List<Sancion> buscarPorSocio(int socioId) {
        return cargarLista().stream()
                .filter(s -> s.socioId() == socioId)
                .toList();
    }

    // -------------------------------------------------------
    // Helpers CSV
    // -------------------------------------------------------

    private List<Sancion> cargarLista() {
        List<Sancion> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.isBlank()) continue;
                Sancion s = parsearLinea(linea);
                if (s != null) lista.add(s);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo sanciones.csv: " + e.getMessage());
        }
        return lista;
    }

    private void persistir(List<Sancion> datos) {
        try {
            Files.createDirectories(Path.of("datos"));
            try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
                pw.println(CABECERA);
                for (Sancion s : datos) {
                    pw.println(serializar(s));
                }
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo sanciones.csv: " + e.getMessage());
        }
    }

    private String serializar(Sancion s) {
        return String.join(",",
                String.valueOf(s.socioId()),
                s.fechaInicio().toString(),
                s.fechaFin().toString(),
                String.valueOf(s.diasRetraso()));
    }

    private Sancion parsearLinea(String linea) {
        String[] campos = linea.split(",", -1);
        if (campos.length < 4) return null;
        try {
            int socioId          = Integer.parseInt(campos[0].trim());
            LocalDate fechaInicio = LocalDate.parse(campos[1].trim());
            LocalDate fechaFin   = LocalDate.parse(campos[2].trim());
            long diasRetraso     = Long.parseLong(campos[3].trim());
            return new Sancion(socioId, fechaInicio, fechaFin, diasRetraso);
        } catch (Exception e) {
            System.err.println("Error parseando línea de sanción: " + linea);
        }
        return null;
    }
}
