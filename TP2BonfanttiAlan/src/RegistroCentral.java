import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Guarda clientes, sucursales y envíos para permitir encontrarlos por su dato
// No ejecuta acciones del negocio (despachar, entregar, cancelar, etc.): esas responsabilidades siguen en envio y sucursal
public class RegistroCentral {
    // Los clientes se buscan de forma secuencial (Parte 4)
    private final List<persona> clientes;
    // Sucursales y envíos tienen un código único: HashMap
    private final Map<Integer, sucursal> sucursales;
    private final Map<Integer, envio> envios;

    // Se crea una sola instancia en el main
    public RegistroCentral() {
        this.clientes = new ArrayList<>();
        this.sucursales = new HashMap<>();
        this.envios = new HashMap<>();
    }

    // ############## Clientes ###########

    // Registra un cliente controlando que no se repita su DNI
    public void registrarCliente(persona cliente) throws ClienteDuplicadoException {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser null");
        }
        if (buscarCliente(cliente.getID()) != null) {
            throw new ClienteDuplicadoException(
                    "Ya existe un cliente registrado con el DNI " + cliente.getID());
        }
        clientes.add(cliente);
    }

    // Devuelve todos los clientes registrados
    public List<persona> listarClientes() {
        return Collections.unmodifiableList(new ArrayList<>(clientes));
    }

    // Busca un cliente por DNI recorriendo la lista de forma secuencial
    public persona buscarClienteSecuencial(int dni) throws ClienteNoEncontradoException {
        persona cliente = buscarCliente(dni);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("No existe un cliente con el DNI " + dni);
        }
        return cliente;
    }

    // Búsqueda interna: devuelve null si no existe (no avisa al usuario)
    private persona buscarCliente(int dni) {
        for (persona cliente : clientes) {
            if (cliente.getID() == dni) {
                return cliente;
            }
        }
        return null;
    }

    // ############### Sucursales (HashMap) ################

    // Registra una sucursal controlando que no se repita su código
    public void registrarSucursal(sucursal sucursal) throws SucursalDuplicadaException {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser null");
        }
        if (sucursales.containsKey(sucursal.getCodigo())) {
            throw new SucursalDuplicadaException(
                    "Ya existe una sucursal registrada con el código " + sucursal.getCodigo());
        }
        sucursales.put(sucursal.getCodigo(), sucursal);
    }

    // Devuelve todas las sucursales registradas
    public List<sucursal> listarSucursales() {
        return Collections.unmodifiableList(new ArrayList<>(sucursales.values()));
    }

    // Busca una sucursal por su código de forma directa
    public sucursal buscarSucursal(int codigo) throws SucursalNoEncontradaException {
        sucursal sucursal = sucursales.get(codigo);
        if (sucursal == null) {
            throw new SucursalNoEncontradaException("No existe una sucursal con el código " + codigo);
        }
        return sucursal;
    }

    // ################## Envíos (HashMap) ################

    // Registra un envío que no repite su código de seguimiento
    public void registrarEnvio(envio envio) throws EnvioDuplicadoException {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser null");
        }
        if (envios.containsKey(envio.getCodigo())) {
            throw new EnvioDuplicadoException(
                    "Ya existe un envío registrado con el código " + envio.getCodigo());
        }
        envios.put(envio.getCodigo(), envio);
    }

    // Busca un envío por su código de seguimiento directamente
    public envio buscarEnvio(int codigo) throws EnvioNoEncontradoException {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código del envío debe ser positivo");
        }
        envio envio = envios.get(codigo);
        if (envio == null) {
            throw new EnvioNoEncontradoException("No existe un envío con el código " + codigo);
        }
        return envio;
    }

    // Devuelve todos los envíos registrados
    public List<envio> listarEnvios() {
        return Collections.unmodifiableList(new ArrayList<>(envios.values()));
    }

    // ################## Excepciones del registro ####################

    public static class ClienteDuplicadoException extends Exception {
        public ClienteDuplicadoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class ClienteNoEncontradoException extends Exception {
        public ClienteNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class SucursalDuplicadaException extends Exception {
        public SucursalDuplicadaException(String mensaje) {
            super(mensaje);
        }
    }

    public static class SucursalNoEncontradaException extends Exception {
        public SucursalNoEncontradaException(String mensaje) {
            super(mensaje);
        }
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
