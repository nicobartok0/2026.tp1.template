package main.java.com.bibliotech.exception;

public class LimitePrestamosException extends BibliotecaException {

    private final int socioId;
    private final int limite;

    public LimitePrestamosException(int socioId, int limite) {
        super("El socio con ID " + socioId + " alcanzó el límite de " + limite + " préstamo(s) activo(s).");
        this.socioId = socioId;
        this.limite = limite;
    }

    public int getSocioId() {
        return socioId;
    }

    public int getLimite() {
        return limite;
    }
}
