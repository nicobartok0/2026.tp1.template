package main.java.com.bibliotech;

import main.java.com.bibliotech.exception.BibliotecaException;
import main.java.com.bibliotech.model.Categoria;
import main.java.com.bibliotech.model.Ebook;
import main.java.com.bibliotech.model.Libro;
import main.java.com.bibliotech.model.Prestamo;
import main.java.com.bibliotech.model.Socio;
import main.java.com.bibliotech.repository.InMemoryPrestamoRepository;
import main.java.com.bibliotech.repository.InMemorySancionRepository;
import main.java.com.bibliotech.repository.InMemoryRecursoRepository;
import main.java.com.bibliotech.repository.InMemorySocioRepository;
import main.java.com.bibliotech.service.PrestamoService;
import main.java.com.bibliotech.service.PrestamoServiceImpl;
import main.java.com.bibliotech.service.RecursoService;
import main.java.com.bibliotech.service.RecursoServiceImpl;
import main.java.com.bibliotech.service.SocioService;
import main.java.com.bibliotech.service.SocioServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    // Repositorios
    private static final InMemoryRecursoRepository recursoRepo = new InMemoryRecursoRepository();
    private static final InMemorySocioRepository socioRepo     = new InMemorySocioRepository();
    private static final InMemoryPrestamoRepository prestamoRepo = new InMemoryPrestamoRepository();
    private static final InMemorySancionRepository sancionRepo   = new InMemorySancionRepository();

    // Servicios (inyección por constructor)
    private static final RecursoService recursoService   = new RecursoServiceImpl(recursoRepo);
    private static final SocioService socioService       = new SocioServiceImpl(socioRepo);
    private static final PrestamoService prestamoService = new PrestamoServiceImpl(recursoRepo, socioRepo, prestamoRepo, sancionRepo);

    public static void main(String[] args) {
        System.out.println("=== Bienvenido a BiblioTech ===");
        boolean ejecutando = true;

        while (ejecutando) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1 -> menuRecursos();
                    case 2 -> menuSocios();
                    case 3 -> menuPrestamos();
                    case 0 -> ejecutando = false;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (BibliotecaException e) {
                System.out.println("✗ Error: " + e.getMessage());
            }
        }

        System.out.println("¡Hasta luego!");
        scanner.close();
    }

    // -------------------------------------------------------
    // Menús
    // -------------------------------------------------------

    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Gestión de Recursos");
        System.out.println("2. Gestión de Socios");
        System.out.println("3. Gestión de Préstamos");
        System.out.println("0. Salir");
    }

    private static void menuRecursos() throws BibliotecaException {
        System.out.println("\n--- Recursos ---");
        System.out.println("1. Registrar Libro");
        System.out.println("2. Registrar Ebook");
        System.out.println("3. Buscar por título");
        System.out.println("4. Buscar por autor");
        System.out.println("5. Buscar por categoría");
        System.out.println("6. Listar todos");
        System.out.println("7. Eliminar recurso");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> registrarLibro();
            case 2 -> registrarEbook();
            case 3 -> {
                String titulo = leerTexto("Título a buscar: ");
                imprimirRecursos(recursoService.buscarPorTitulo(titulo));
            }
            case 4 -> {
                String autor = leerTexto("Autor a buscar: ");
                imprimirRecursos(recursoService.buscarPorAutor(autor));
            }
            case 5 -> {
                Categoria cat = leerCategoria();
                imprimirRecursos(recursoService.buscarPorCategoria(cat));
            }
            case 6 -> imprimirRecursos(recursoService.listarTodos());
            case 7 -> {
                String isbn = leerTexto("ISBN a eliminar: ");
                recursoService.eliminar(isbn);
                System.out.println("✓ Recurso eliminado.");
            }
            default -> System.out.println("Opción inválida.");
        }
    }

    private static void menuSocios() throws BibliotecaException {
        System.out.println("\n--- Socios ---");
        System.out.println("1. Registrar Estudiante");
        System.out.println("2. Registrar Docente");
        System.out.println("3. Buscar por ID");
        System.out.println("4. Listar todos");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> registrarSocio("ESTUDIANTE");
            case 2 -> registrarSocio("DOCENTE");
            case 3 -> {
                int id = leerEntero("ID del socio: ");
                Socio socio = socioService.buscarPorId(id);
                System.out.println("  " + socio);
            }
            case 4 -> socioService.listarTodos().forEach(s -> System.out.println("  " + s));
            default -> System.out.println("Opción inválida.");
        }
    }

    private static void menuPrestamos() throws BibliotecaException {
        System.out.println("\n--- Préstamos ---");
        System.out.println("1. Realizar préstamo");
        System.out.println("2. Registrar devolución");
        System.out.println("3. Listar préstamos activos");
        System.out.println("4. Listar por socio");
        System.out.println("5. Listar historial completo");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> {
                String isbn   = leerTexto("ISBN del recurso: ");
                int socioId   = leerEntero("ID del socio: ");
                prestamoService.realizarPrestamo(isbn, socioId);
                System.out.println("✓ Préstamo registrado.");
            }
            case 2 -> {
                int prestamoId = leerEntero("ID del préstamo: ");
                prestamoService.registrarDevolucion(prestamoId);
                System.out.println("✓ Devolución registrada.");
            }
            case 3 -> imprimirPrestamos(prestamoService.listarActivos());
            case 4 -> {
                int socioId = leerEntero("ID del socio: ");
                imprimirPrestamos(prestamoService.listarPorSocio(socioId));
            }
            case 5 -> imprimirPrestamos(prestamoService.listarTodos());
            default -> System.out.println("Opción inválida.");
        }
    }

    // -------------------------------------------------------
    // Acciones
    // -------------------------------------------------------

    private static void registrarLibro() {
        System.out.println("-- Nuevo Libro --");
        String isbn   = leerTexto("ISBN: ");
        String titulo = leerTexto("Título: ");
        String autor  = leerTexto("Autor: ");
        int anio      = leerEntero("Año: ");
        int paginas   = leerEntero("Páginas: ");
        Categoria cat = leerCategoria();

        recursoService.registrarLibro(new Libro(isbn, titulo, autor, anio, paginas, cat));
        System.out.println("✓ Libro registrado.");
    }

    private static void registrarEbook() {
        System.out.println("-- Nuevo Ebook --");
        String isbn    = leerTexto("ISBN: ");
        String titulo  = leerTexto("Título: ");
        String autor   = leerTexto("Autor: ");
        int anio       = leerEntero("Año: ");
        String formato = leerTexto("Formato (PDF, EPUB, MOBI): ");
        double tamanio = leerDouble("Tamaño (MB): ");
        Categoria cat  = leerCategoria();

        recursoService.registrarEbook(new Ebook(isbn, titulo, autor, anio, formato, tamanio, cat));
        System.out.println("✓ Ebook registrado.");
    }

    private static void registrarSocio(String tipo) throws BibliotecaException {
        System.out.println("-- Nuevo " + tipo + " --");
        int id         = leerEntero("ID: ");
        String nombre  = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String dni     = leerTexto("DNI: ");
        String email   = leerTexto("Email: ");

        if (tipo.equals("ESTUDIANTE")) {
            socioService.registrarEstudiante(id, nombre, apellido, dni, email);
        } else {
            socioService.registrarDocente(id, nombre, apellido, dni, email);
        }
        System.out.println("✓ Socio registrado.");
    }

    // -------------------------------------------------------
    // Helpers de impresión
    // -------------------------------------------------------

    private static void imprimirRecursos(List<?> recursos) {
        if (recursos.isEmpty()) {
            System.out.println("  No se encontraron recursos.");
            return;
        }
        recursos.forEach(r -> System.out.println("  " + r));
    }

    private static void imprimirPrestamos(List<Prestamo> prestamos) {
        if (prestamos.isEmpty()) {
            System.out.println("  No hay préstamos para mostrar.");
            return;
        }
        prestamos.forEach(p -> System.out.printf(
                "  [#%d] ISBN: %s | Socio: %d | Desde: %s | Vence: %s | Estado: %s%n",
                p.id(), p.isbn(), p.socioId(),
                p.fechaInicio(), p.fechaDevolucionEsperada(), p.estado()
        ));
    }

    // -------------------------------------------------------
    // Helpers de lectura
    // -------------------------------------------------------

    private static String leerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un número válido.");
            }
        }
    }

    private static double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un número válido.");
            }
        }
    }

    private static Categoria leerCategoria() {
        System.out.println("  Categorías: " + java.util.Arrays.toString(Categoria.values()));
        while (true) {
            String input = leerTexto("  Categoría: ").toUpperCase();
            try {
                return Categoria.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("  Categoría inválida, intente nuevamente.");
            }
        }
    }
}
