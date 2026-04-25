package main.java.com.bibliotech.exception;

public class DniDuplicadoException extends BibliotecaException {

    private final String dni;

    public DniDuplicadoException(String dni) {
        super("Ya existe un socio registrado con el DNI: " + dni);
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }
}
