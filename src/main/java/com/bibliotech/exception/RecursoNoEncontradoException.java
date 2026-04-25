package main.java.com.bibliotech.exception;

public class RecursoNoEncontradoException extends BibliotecaException {

    private final String isbn;

    public RecursoNoEncontradoException(String isbn) {
        super("No se encontró ningún recurso con ISBN: " + isbn);
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }
}
