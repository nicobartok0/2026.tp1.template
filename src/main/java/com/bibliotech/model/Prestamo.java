package main.java.com.bibliotech.model;

import java.time.LocalDate;

public record Prestamo(
        int id,
        String isbn,
        int socioId,
        LocalDate fechaInicio,
        LocalDate fechaDevolucionEsperada,
        LocalDate fechaDevolucionReal,
        EstadoPrestamo estado
) {
    public boolean estaVencido() {
        return estado == EstadoPrestamo.ACTIVO
                && LocalDate.now().isAfter(fechaDevolucionEsperada);
    }

    public long calcularDiasRetraso() {
        if (fechaDevolucionReal == null) return 0;
        long dias = fechaDevolucionEsperada.until(fechaDevolucionReal).getDays();
        return Math.max(0, dias);
    }
}
