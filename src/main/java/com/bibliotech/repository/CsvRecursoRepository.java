package main.java.com.bibliotech.repository;

import main.java.com.bibliotech.model.Categoria;
import main.java.com.bibliotech.model.Ebook;
import main.java.com.bibliotech.model.Libro;
import main.java.com.bibliotech.model.Recurso;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CsvRecursoRepository implements RecursoRepository {

    private static final String ARCHIVO = "datos/recursos.csv";
    private static final String CABECERA = "tipo,isbn,titulo,autor,anio,paginas,formatoArchivo,tamanioMb,categoria";

    @Override
    public void guardar(Recurso recurso) {
        Map<String, Recurso> datos = cargarTodos();
        datos.put(recurso.isbn(), recurso);
        persistir(datos);
    }

    @Override
    public Optional<Recurso> buscarPorId(String isbn) {
        return Optional.ofNullable(cargarTodos().get(isbn));
    }

    @Override
    public List<Recurso> buscarTodos() {
        return new ArrayList<>(cargarTodos().values());
    }

    @Override
    public void eliminar(String isbn) {
        Map<String, Recurso> datos = cargarTodos();
        datos.remove(isbn);
        persistir(datos);
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        String busqueda = titulo.toLowerCase();
        return buscarTodos().stream()
                .filter(r -> r.titulo().toLowerCase().contains(busqueda))
                .toList();
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {
        String busqueda = autor.toLowerCase();
        return buscarTodos().stream()
                .filter(r -> {
                    if (r instanceof Libro l)  return l.autor().toLowerCase().contains(busqueda);
                    if (r instanceof Ebook e)  return e.autor().toLowerCase().contains(busqueda);
                    return false;
                })
                .toList();
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return buscarTodos().stream()
                .filter(r -> r.categoria() == categoria)
                .toList();
    }

    // -------------------------------------------------------
    // Helpers CSV
    // -------------------------------------------------------

    private Map<String, Recurso> cargarTodos() {
        Map<String, Recurso> mapa = new LinkedHashMap<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return mapa;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; } // saltar cabecera
                if (linea.isBlank()) continue;
                Recurso r = parsearLinea(linea);
                if (r != null) mapa.put(r.isbn(), r);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo recursos.csv: " + e.getMessage());
        }
        return mapa;
    }

    private void persistir(Map<String, Recurso> datos) {
        try {
            Files.createDirectories(Path.of("datos"));
            try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
                pw.println(CABECERA);
                for (Recurso r : datos.values()) {
                    pw.println(serializar(r));
                }
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo recursos.csv: " + e.getMessage());
        }
    }

    private String serializar(Recurso r) {
        if (r instanceof Libro l) {
            return String.join(",",
                    "LIBRO", l.isbn(), escapar(l.titulo()), escapar(l.autor()),
                    String.valueOf(l.anio()), String.valueOf(l.paginas()),
                    "", "", l.categoria().name());
        } else if (r instanceof Ebook e) {
            return String.join(",",
                    "EBOOK", e.isbn(), escapar(e.titulo()), escapar(e.autor()),
                    String.valueOf(e.anio()), "",
                    e.formatoArchivo(), String.valueOf(e.tamanioMb()), e.categoria().name());
        }
        return "";
    }

    private Recurso parsearLinea(String linea) {
        String[] campos = linea.split(",", -1);
        if (campos.length < 9) return null;
        try {
            String tipo      = campos[0].trim();
            String isbn      = campos[1].trim();
            String titulo    = desescapar(campos[2].trim());
            String autor     = desescapar(campos[3].trim());
            int anio         = Integer.parseInt(campos[4].trim());
            Categoria cat    = Categoria.valueOf(campos[8].trim());

            if ("LIBRO".equals(tipo)) {
                int paginas = campos[5].isBlank() ? 0 : Integer.parseInt(campos[5].trim());
                return new Libro(isbn, titulo, autor, anio, paginas, cat);
            } else if ("EBOOK".equals(tipo)) {
                String formato  = campos[6].trim();
                double tamanio  = campos[7].isBlank() ? 0.0 : Double.parseDouble(campos[7].trim());
                return new Ebook(isbn, titulo, autor, anio, formato, tamanio, cat);
            }
        } catch (Exception e) {
            System.err.println("Error parseando línea de recurso: " + linea);
        }
        return null;
    }

    private String escapar(String valor) {
        return valor.replace(",", ";");
    }

    private String desescapar(String valor) {
        return valor.replace(";", ",");
    }
}
