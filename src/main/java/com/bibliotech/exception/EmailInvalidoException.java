package main.java.com.bibliotech.exception;

public class EmailInvalidoException extends BibliotecaException {

    private final String email;

    public EmailInvalidoException(String email) {
        super("El formato del email es inválido: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
