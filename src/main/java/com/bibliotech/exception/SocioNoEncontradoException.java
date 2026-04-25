package main.java.com.bibliotech.exception;

public class SocioNoEncontradoException extends BibliotecaException {

    private final int socioId;

    public SocioNoEncontradoException(int socioId) {
        super("No se encontró ningún socio con ID: " + socioId);
        this.socioId = socioId;
    }

    public int getSocioId() {
        return socioId;
    }
}
