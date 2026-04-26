package main.java.com.bibliotech.model;

public abstract class Socio {

    private final int id;
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final String email;
    private final TipoSocio tipo;

    protected Socio(int id, String nombre, String apellido, String dni, String email, TipoSocio tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.tipo = tipo;
    }

    public abstract int getLimiteLibros();

    public int getId()           { return id; }
    public String getNombre()    { return nombre; }
    public String getApellido()  { return apellido; }
    public String getDni()       { return dni; }
    public String getEmail()     { return email; }
    public TipoSocio getTipo()   { return tipo; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s (DNI: %s) - límite: %d libro(s)",
                tipo, nombre, apellido, dni, getLimiteLibros());
    }
}
