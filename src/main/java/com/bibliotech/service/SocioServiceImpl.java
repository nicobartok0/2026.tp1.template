package main.java.com.bibliotech.service;

import main.java.com.bibliotech.exception.DniDuplicadoException;
import main.java.com.bibliotech.exception.EmailInvalidoException;
import main.java.com.bibliotech.exception.SocioNoEncontradoException;
import main.java.com.bibliotech.model.Docente;
import main.java.com.bibliotech.model.Estudiante;
import main.java.com.bibliotech.model.Socio;
import main.java.com.bibliotech.repository.SocioRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class SocioServiceImpl implements SocioService {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public void registrarEstudiante(int id, String nombre, String apellido, String dni, String email)
            throws DniDuplicadoException, EmailInvalidoException {
        validarEmail(email);
        validarDniUnico(dni);
        socioRepository.guardar(new Estudiante(id, nombre, apellido, dni, email));
    }

    @Override
    public void registrarDocente(int id, String nombre, String apellido, String dni, String email)
            throws DniDuplicadoException, EmailInvalidoException {
        validarEmail(email);
        validarDniUnico(dni);
        socioRepository.guardar(new Docente(id, nombre, apellido, dni, email));
    }

    @Override
    public Socio buscarPorId(int id) throws SocioNoEncontradoException {
        return socioRepository.buscarPorId(id)
                .orElseThrow(() -> new SocioNoEncontradoException(id));
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return socioRepository.buscarPorDni(dni);
    }

    @Override
    public List<Socio> listarTodos() {
        return socioRepository.buscarTodos();
    }

    // --- Validaciones privadas ---

    private void validarEmail(String email) throws EmailInvalidoException {
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            throw new EmailInvalidoException(email);
        }
    }

    private void validarDniUnico(String dni) throws DniDuplicadoException {
        if (socioRepository.existeDni(dni)) {
            throw new DniDuplicadoException(dni);
        }
    }
}
