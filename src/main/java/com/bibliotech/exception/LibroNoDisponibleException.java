package main.java.com.bibliotech.exception;

public class LibroNoDisponibleException extends BibliotecaException {

    private final String isbn;

    public LibroNoDisponibleException(String isbn) {
        super("El libro con ISBN '" + isbn + "' no está disponible para préstamo.");
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }
}
