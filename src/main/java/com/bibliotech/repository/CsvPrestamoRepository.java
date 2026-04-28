package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.EstadoPrestamo;
import main.java.com.bibliotech.model.Prestamo;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class CsvPrestamoRepository implements PrestamoRepository {

    private static final String ARCHIVO  = "datos/prestamos.csv";
    private static final String CABECERA = "id,isbn,socioId,fechaInicio,fechaDevolucionEsperada,fechaDevolucionReal,estado";

    private int proximoId = 1;

    public CsvPrestamoRepository() {
        // Inicializar proximoId a partir del máximo existente
        cargarTodos().stream()
                .mapToInt(Prestamo::id)
                .max()
                .ifPresent(max -> proximoId = max + 1);
    }

    public int generarId() {
        return proximoId++;
    }

    @Override
    public void guardar(Prestamo prestamo) {
        Map<Integer, Prestamo> datos = cargarMapa();
        datos.put(prestamo.id(), prestamo);
        persistir(datos);
    }

    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        return Optional.ofNullable(cargarMapa().get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(cargarMapa().values());
    }

    @Override
    public void eliminar(Integer id) {
        // El historial de préstamos es permanente
    }

    @Override
    public List<Prestamo> buscarPorSocio(int socioId) {
        return buscarTodos().stream()
                .filter(p -> p.socioId() == socioId)
                .toList();
    }

    @Override
    public List<Prestamo> buscarPorIsbn(String isbn) {
        return buscarTodos().stream()
                .filter(p -> p.isbn().equals(isbn))
                .toList();
    }

    @Override
    public List<Prestamo> buscarPorEstado(EstadoPrestamo estado) {
        return buscarTodos().stream()
                .filter(p -> p.estado() == estado)
                .toList();
    }

    // -------------------------------------------------------
    // Helpers CSV
    // -------------------------------------------------------

    private Map<Integer, Prestamo> cargarMapa() {
        Map<Integer, Prestamo> mapa = new LinkedHashMap<>();
        for (Prestamo p : cargarTodos()) mapa.put(p.id(), p);
        return mapa;
    }

    private List<Prestamo> cargarTodos() {
        List<Prestamo> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.isBlank()) continue;
                Prestamo p = parsearLinea(linea);
                if (p != null) lista.add(p);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo prestamos.csv: " + e.getMessage());
        }
        return lista;
    }

    private void persistir(Map<Integer, Prestamo> datos) {
        try {
            Files.createDirectories(Path.of("datos"));
            try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
                pw.println(CABECERA);
                for (Prestamo p : datos.values()) {
                    pw.println(serializar(p));
                }
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo prestamos.csv: " + e.getMessage());
        }
    }

    private String serializar(Prestamo p) {
        return String.join(",",
                String.valueOf(p.id()),
                p.isbn(),
                String.valueOf(p.socioId()),
                p.fechaInicio().toString(),
                p.fechaDevolucionEsperada().toString(),
                p.fechaDevolucionReal() != null ? p.fechaDevolucionReal().toString() : "",
                p.estado().name());
    }

    private Prestamo parsearLinea(String linea) {
        String[] campos = linea.split(",", -1);
        if (campos.length < 7) return null;
        try {
            int id                       = Integer.parseInt(campos[0].trim());
            String isbn                  = campos[1].trim();
            int socioId                  = Integer.parseInt(campos[2].trim());
            LocalDate fechaInicio        = LocalDate.parse(campos[3].trim());
            LocalDate fechaDevEsperada   = LocalDate.parse(campos[4].trim());
            LocalDate fechaDevReal       = campos[5].isBlank() ? null : LocalDate.parse(campos[5].trim());
            EstadoPrestamo estado        = EstadoPrestamo.valueOf(campos[6].trim());

            return new Prestamo(id, isbn, socioId, fechaInicio, fechaDevEsperada, fechaDevReal, estado);
        } catch (Exception e) {
            System.err.println("Error parseando línea de préstamo: " + linea);
        }
        return null;
    }
}
