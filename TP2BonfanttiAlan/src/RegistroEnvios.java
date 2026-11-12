import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Objeto que registra todos los envíos del sistema.
// Se mantiene como un registro simple, separado de RegistroCentral
public class RegistroEnvios {
    private final List<envio> envios;

    public RegistroEnvios() {
        this.envios = new ArrayList<>();
    }

    // Registra un envío, evitando códigos repetidos
    public void registrar(envio envio) throws EnvioDuplicadoException {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser null");
        }
        if (existeCodigo(envio.getCodigo())) {
            throw new EnvioDuplicadoException("Ya existe un envío registrado con el código " + envio.getCodigo());
        }
        envios.add(envio);
    }

    // Busca un envío por su código
    public envio buscarPorCodigo(int codigo) throws EnvioNoEncontradoException {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código del envío debe ser positivo");
        }
        for (envio envio : envios) {
            if (envio.getCodigo() == codigo) {
                return envio;
            }
        }
        throw new EnvioNoEncontradoException("No existe un envío con el código " + codigo);
    }

    public List<envio> listar() {
        return Collections.unmodifiableList(new ArrayList<>(envios));
    }

    public int cantidad() {
        return envios.size();
    }

    private boolean existeCodigo(int codigo) {
        for (envio envio : envios) {
            if (envio.getCodigo() == codigo) {
                return true;
            }
        }
        return false;
    }

    public static class EnvioDuplicadoException extends Exception {
        public EnvioDuplicadoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class EnvioNoEncontradoException extends Exception {
        public EnvioNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }
}
