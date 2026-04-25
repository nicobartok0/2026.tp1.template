package main.java.com.bibliotech.exception;

public class BibliotecaException extends Exception{

    public BibliotecaException(String mensaje) {
        super(mensaje);
    }

    public BibliotecaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
