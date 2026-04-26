package main.java.com.bibliotech.model;

public class Estudiante extends Socio {

    private static final int LIMITE_PRESTAMOS = 3;

    public Estudiante(int id, String nombre, String apellido, String dni, String email) {
        super(id, nombre, apellido, dni, email, TipoSocio.ESTUDIANTE);
    }

    @Override
    public int getLimiteLibros() {
        return LIMITE_PRESTAMOS;
    }
}
