import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

// Clase sucursal
public class sucursal {
    private int codigo;
    private String nombre;
    // Cola FIFO: los envíos esperan su turno para ser despachados
    private final Queue<envio> pendientesDeDespacho;

    // Constructor
    public sucursal(int codigo, String nombre) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código de sucursal debe ser positivo");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de sucursal es obligatorio");
        }
        this.codigo = codigo;
        this.nombre = nombre.trim();
        this.pendientesDeDespacho = new ArrayDeque<>();
    }

    // Recibir un envío: registra la recepción y lo agrega al final de la cola
    public String recibirEnvio(envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser null");
        }
        if (pendientesDeDespacho.contains(envio)) {
            throw new IllegalStateException("El envío " + envio.getCodigo() + " ya está en la sucursal " + nombre);
        }
        envio.recibirEnSucursal(this);
        pendientesDeDespacho.add(envio);
        return "Envío " + envio.getCodigo() + " recibido en " + nombre
                + " (pendientes de despacho: " + pendientesDeDespacho.size() + ")";
    }

    // Despachar como máximo `cantidad` envíos, respetando el orden de llegada (FIFO)
    public String despachar(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de envíos a despachar debe ser positiva");
        }
        if (pendientesDeDespacho.isEmpty()) {
            return "No hay envíos pendientes de despacho en " + nombre;
        }

        StringBuilder resultado = new StringBuilder();
        int despachados = 0;
        while (despachados < cantidad && !pendientesDeDespacho.isEmpty()) {
            // Se mira el primero sin sacarlo: si la validación falla, sigue en la cola
            envio envio = pendientesDeDespacho.peek();
            envio.despacharDesde(this);
            pendientesDeDespacho.poll();

            if (despachados > 0) {
                resultado.append("\n");
            }
            resultado.append("Envío ").append(envio.getCodigo())
                    .append(" despachado desde ").append(nombre);
            despachados++;
        }
        return resultado.toString();
    }

    // Entregar un envío que se encuentra en esta sucursal
    public String entregarEnvio(envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser null");
        }
        if (!pendientesDeDespacho.contains(envio)) {
            throw new IllegalStateException("El envío " + envio.getCodigo() + " no está en la sucursal " + nombre);
        }
        envio.entregarEn(this);
        pendientesDeDespacho.remove(envio);
        return "Envío " + envio.getCodigo() + " entregado en " + nombre;
    }

    // Envíos esperando despacho, en orden de llegada
    public List<envio> listarPendientes() {
        return Collections.unmodifiableList(new ArrayList<>(pendientesDeDespacho));
    }

    public int cantidadPendientes() {
        return pendientesDeDespacho.size();
    }

    // Getters
    public int getCodigo() {return this.codigo;}
    public String getNombre() {return this.nombre;}

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof sucursal)) return false;
        sucursal otra = (sucursal) objeto;
        return codigo == otra.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
