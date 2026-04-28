package main.java.com.bibliotech.exception;

import java.time.LocalDate;

public class SocioSancionadoException extends BibliotecaException {

    private final int socioId;
    private final LocalDate fechaFin;

    public SocioSancionadoException(int socioId, LocalDate fechaFin) {
        super("El socio con ID " + socioId + " está sancionado hasta el " + fechaFin + " y no puede realizar préstamos.");
        this.socioId = socioId;
        this.fechaFin = fechaFin;
    }

    public int getSocioId()       { return socioId; }
    public LocalDate getFechaFin() { return fechaFin; }
}
