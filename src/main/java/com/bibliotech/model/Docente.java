package main.java.com.bibliotech.model;

public class Docente extends Socio {

    private static final int LIMITE_PRESTAMOS = 5;

    public Docente(int id, String nombre, String apellido, String dni, String email) {
        super(id, nombre, apellido, dni, email, TipoSocio.DOCENTE);
    }

    @Override
    public int getLimiteLibros() {
        return LIMITE_PRESTAMOS;
    }
}
