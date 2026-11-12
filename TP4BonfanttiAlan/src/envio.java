import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Clase abstracta
public abstract class envio {
    private int codigo;
    private sucursal origen;
    private sucursal destino;
    private sucursal sucursalActual;
    private List<Paquete> paquetes;
    private double costo;
    private List<registroSeguimiento> historial;
    private EstadoEnvio estado;

    public envio(int codigo, sucursal origen, sucursal destino) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código del envío debe ser positivo");
        }
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("El origen y el destino del envío son obligatorios");
        }
        this.codigo = codigo;
        this.origen = origen;
        this.destino = destino;
        this.sucursalActual = null;
        this.paquetes = new ArrayList<>();
        this.costo = 0.0;
        this.historial = new ArrayList<>();
        this.estado = EstadoEnvio.REGISTRADO;
    }

    // Gestión de paquetes - MAXIMO: 3
    public void agregarPaquete(Paquete paquete) throws Paquete.PaqueteDuplicadoException {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser null");
        }
        if (paquetes.contains(paquete)) {
            throw new Paquete.PaqueteDuplicadoException("El paquete ya está agregado al envío");
        }
        if (!hayLugar()) {
            throw new IllegalStateException("El envío no puede llevar más de 3 paquetes");
        }
        if (!todaviaNoSalio()) {
            throw new IllegalStateException("No se pueden agregar paquetes a un envío que ya fue despachado");
        }
        paquetes.add(paquete);
        this.costo = calcularCosto(); // Recalcula
    }

    void recibirEnSucursal(sucursal sucursal) {
        validarPaquetes();
        validarRecepcionEn(sucursal);
        for (Paquete paq : paquetes) {
            paq.depositar();
        }
        this.sucursalActual = sucursal;
        this.estado = EstadoEnvio.EN_DEPOSITO;
        registrarMovimiento(TipoMovimiento.RECIBIDO_EN_SUCURSAL, sucursal);
    }

    void despacharDesde(sucursal sucursal) {
        validarPaquetes();
        validarDespachoDesde(sucursal);
        for (Paquete paq : paquetes) {
            paq.despachar();
        }
        this.sucursalActual = null;
        this.estado = EstadoEnvio.EN_CAMINO;
        registrarMovimiento(TipoMovimiento.DESPACHADO_DESDE_SUCURSAL, sucursal);
    }

    void entregarEn(sucursal sucursal) {
        validarPaquetes();
        validarEntregaEn(sucursal);
        for (Paquete paq : paquetes) {
            paq.entregar();
        }
        this.estado = EstadoEnvio.ENTREGADO;
        registrarMovimiento(TipoMovimiento.ENTREGADO, sucursal);
    }

    // Validaciones de ubicación del envío
    public void validarRecepcionEn(sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser null");
        }
        if (!puedeRecibirseEn(sucursal)) {
            throw new IllegalStateException("El envío " + codigo + " no puede recibirse en "
                    + sucursal.getNombre() + " desde " + estado);
        }
    }

    public void validarDespachoDesde(sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser null");
        }
        if (!puedeDespacharseDesde(sucursal)) {
            throw new IllegalStateException("El envío " + codigo + " no puede despacharse desde "
                    + sucursal.getNombre() + " en estado " + estado);
        }
    }

    public void validarEntregaEn(sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser null");
        }
        if (!puedeEntregarseEn(sucursal)) {
            throw new IllegalStateException("El envío " + codigo + " no puede entregarse en "
                    + sucursal.getNombre() + " en estado " + estado);
        }
    }

    private boolean puedeRecibirseEn(sucursal sucursal) {
        return (estaEnEstado(EstadoEnvio.REGISTRADO) && esSucursalOrigen(sucursal))
                || estaEnEstado(EstadoEnvio.EN_CAMINO);
    }

    private boolean puedeDespacharseDesde(sucursal sucursal) {
        return estaEnEstado(EstadoEnvio.EN_DEPOSITO) && sucursal.equals(sucursalActual);
    }

    private boolean puedeEntregarseEn(sucursal sucursal) {
        return estaEnEstado(EstadoEnvio.EN_DEPOSITO)
                && sucursal.equals(sucursalActual)
                && sucursal.equals(destino);
    }

    private boolean estaEnEstado(EstadoEnvio estadoEsperado) {
        return estado == estadoEsperado;
    }

    private boolean esSucursalOrigen(sucursal sucursal) {
        return origen.equals(sucursal);
    }

    // Validación de paquetes
    private void validarPaquetes() {
        if (paquetes.isEmpty()) {
            throw new IllegalStateException("El envío debe contener al menos un paquete");
        }
    }

    private boolean hayLugar() {
        return paquetes.size() < 3;
    }

    private boolean todaviaNoSalio() {
        return estaEnEstado(EstadoEnvio.REGISTRADO) || estaEnEstado(EstadoEnvio.EN_DEPOSITO);
    }

    // Subclases que definen su clase de costo
    protected abstract double calcularCosto();

    // Peso total para calcular costos
    protected double totalPeso() {
        double totalPeso = 0;
        for (Paquete paq : paquetes) {
            totalPeso += paq.getPeso();
        }
        return totalPeso;
    }

    // Registrar movimiento del envío
    private void registrarMovimiento(TipoMovimiento tipo, sucursal sucursal) {
        historial.add(new registroSeguimiento(LocalDateTime.now(), tipo, sucursal));
    }

    public List<registroSeguimiento> getHistorial() {
        return Collections.unmodifiableList(new ArrayList<>(historial));
    }

    public List<sucursal> getSucursalesRecorridas() {
        List<sucursal> sucursales = new ArrayList<>();
        for (registroSeguimiento movimiento : historial) {
            if (!sucursales.contains(movimiento.getSucursal())) {
                sucursales.add(movimiento.getSucursal());
            }
        }
        return Collections.unmodifiableList(sucursales);
    }

    public registroSeguimiento ultimoMovimiento() {
        if (historial.isEmpty()) {
            throw new IllegalStateException("El envío " + codigo + " no tiene movimientos registrados");
        }
        return historial.get(historial.size() - 1);
    }


    // Getters
    public int getCodigo() {return codigo;}
    public double getCosto() {return costo;}
    public sucursal getOrigen() {return origen;}
    public sucursal getDestino() {return destino;}
    public sucursal getSucursalActual() {return sucursalActual;}
    public EstadoEnvio getEstado() {return estado;}
}
