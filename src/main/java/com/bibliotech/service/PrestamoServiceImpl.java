package main.java.com.bibliotech.service;

import main.java.com.bibliotech.exception.BibliotecaException;
import main.java.com.bibliotech.exception.LibroNoDisponibleException;
import main.java.com.bibliotech.exception.LimitePrestamosException;
import main.java.com.bibliotech.exception.RecursoNoEncontradoException;
import main.java.com.bibliotech.exception.SocioNoEncontradoException;
import main.java.com.bibliotech.exception.SocioSancionadoException;
import main.java.com.bibliotech.model.EstadoPrestamo;
import main.java.com.bibliotech.model.Prestamo;
import main.java.com.bibliotech.model.Recurso;
import main.java.com.bibliotech.model.Sancion;
import main.java.com.bibliotech.model.Socio;
import main.java.com.bibliotech.repository.PrestamoRepository;
import main.java.com.bibliotech.repository.RecursoRepository;
import main.java.com.bibliotech.repository.SancionRepository;
import main.java.com.bibliotech.repository.SocioRepository;

import java.time.LocalDate;
import java.util.List;

public class PrestamoServiceImpl implements PrestamoService {

    private static final int DIAS_PRESTAMO  = 7;
    private static final int DIAS_SANCION_POR_DIA_RETRASO = 2;

    private final RecursoRepository recursoRepository;
    private final SocioRepository socioRepository;
    private final PrestamoRepository prestamoRepository;
    private final SancionRepository sancionRepository;

    public PrestamoServiceImpl(RecursoRepository recursoRepository,
                               SocioRepository socioRepository,
                               PrestamoRepository prestamoRepository,
                               SancionRepository sancionRepository) {
        this.recursoRepository  = recursoRepository;
        this.socioRepository    = socioRepository;
        this.prestamoRepository = prestamoRepository;
        this.sancionRepository  = sancionRepository;
    }

    @Override
    public void realizarPrestamo(String isbn, int socioId) throws BibliotecaException {
        // Verificar que el recurso existe
        Recurso recurso = recursoRepository.buscarPorId(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException(isbn));

        // Verificar que el socio existe
        Socio socio = socioRepository.buscarPorId(socioId)
                .orElseThrow(() -> new SocioNoEncontradoException(socioId));

        // Verificar que el socio no está sancionado
        sancionRepository.buscarSancionActivaPorSocio(socioId).ifPresent(s -> {
            try {
                throw new SocioSancionadoException(socioId, s.fechaFin());
            } catch (SocioSancionadoException e) {
                throw new RuntimeException(e);
            }
        });

        // Verificar que el recurso no está ya prestado
        boolean estaDisponible = prestamoRepository.buscarPorIsbn(isbn).stream()
                .noneMatch(p -> p.estado() == EstadoPrestamo.ACTIVO);
        if (!estaDisponible) {
            throw new LibroNoDisponibleException(isbn);
        }

        // Verificar que el socio no superó su límite
        long prestamosActivos = prestamoRepository.buscarPorSocio(socioId).stream()
                .filter(p -> p.estado() == EstadoPrestamo.ACTIVO)
                .count();
        if (prestamosActivos >= socio.getLimiteLibros()) {
            throw new LimitePrestamosException(socioId, socio.getLimiteLibros());
        }

        // Registrar el préstamo
        int id = prestamoRepository.generarId();
        Prestamo prestamo = new Prestamo(
                id,
                isbn,
                socioId,
                LocalDate.now(),
                LocalDate.now().plusDays(DIAS_PRESTAMO),
                null,
                EstadoPrestamo.ACTIVO
        );
        prestamoRepository.guardar(prestamo);
    }

    @Override
    public void registrarDevolucion(int prestamoId) throws BibliotecaException {
        Prestamo prestamo = prestamoRepository.buscarPorId(prestamoId)
                .orElseThrow(() -> new BibliotecaException("No se encontró el préstamo con ID: " + prestamoId));

        if (prestamo.estado() != EstadoPrestamo.ACTIVO) {
            throw new BibliotecaException("El préstamo con ID " + prestamoId + " ya fue devuelto.");
        }

        LocalDate hoy = LocalDate.now();
        EstadoPrestamo estadoFinal = hoy.isAfter(prestamo.fechaDevolucionEsperada())
                ? EstadoPrestamo.VENCIDO
                : EstadoPrestamo.DEVUELTO;

        Prestamo devuelto = new Prestamo(
                prestamo.id(),
                prestamo.isbn(),
                prestamo.socioId(),
                prestamo.fechaInicio(),
                prestamo.fechaDevolucionEsperada(),
                hoy,
                estadoFinal
        );
        prestamoRepository.guardar(devuelto);

        // Aplicar sanción si hubo retraso
        if (estadoFinal == EstadoPrestamo.VENCIDO) {
            long diasRetraso = devuelto.calcularDiasRetraso();
            long diasBloqueo = diasRetraso * DIAS_SANCION_POR_DIA_RETRASO;
            Sancion sancion  = new Sancion(
                    prestamo.socioId(),
                    hoy,
                    hoy.plusDays(diasBloqueo),
                    diasRetraso
            );
            sancionRepository.guardar(sancion);
            System.out.println("⚠ Devolución con " + diasRetraso + " día(s) de retraso.");
            System.out.println("⚠ Sanción aplicada: bloqueado por " + diasBloqueo + " día(s) hasta " + sancion.fechaFin() + ".");
        }
    }

    @Override
    public List<Prestamo> listarPorSocio(int socioId) {
        return prestamoRepository.buscarPorSocio(socioId);
    }

    @Override
    public List<Prestamo> listarActivos() {
        return prestamoRepository.buscarPorEstado(EstadoPrestamo.ACTIVO);
    }

    @Override
    public List<Prestamo> listarTodos() {
        return prestamoRepository.buscarTodos();
    }
}
