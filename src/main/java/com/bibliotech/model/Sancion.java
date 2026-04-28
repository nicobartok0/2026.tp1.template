package main.java.com.bibliotech.model;

import java.time.LocalDate;

public record Sancion(
        int socioId,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        long diasRetraso
) {
    public boolean estaActiva() {
        return LocalDate.now().isBefore(fechaFin) || LocalDate.now().isEqual(fechaFin);
    }
}
