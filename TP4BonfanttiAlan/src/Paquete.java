// Clase Paquete
public class Paquete {
    private final String Codigo;
    private final String descripcion;
    private final Double peso;
    private final String remitente;
    private EstadoPaquete estado;


    // Constructor Máximo
    public Paquete(String Codigo, String descripcion, Double peso, String remitente) {
        this(Codigo, descripcion, peso, remitente, true);
    }

    // Constructor mínimo
    public Paquete(String Codigo, String descripcion) {
        this(Codigo, descripcion, null, null, false);
    }

    private Paquete(String Codigo, String descripcion, Double peso, String remitente,
                    boolean datosCompletos) {
        validarTexto(Codigo, "El código del paquete");
        validarTexto(descripcion, "La descripción");
        if (datosCompletos) {
            validarTexto(remitente, "El remitente");
            if (peso == null || !Double.isFinite(peso) || peso <= 0) {
                throw new IllegalArgumentException("El peso debe ser un número positivo");
            }
        }
        this.Codigo = Codigo.trim();
        this.descripcion = descripcion.trim();
        this.peso = datosCompletos ? peso : null;
        this.remitente = datosCompletos ? remitente.trim() : null;
        this.estado = EstadoPaquete.REGISTRADO;
    }

    // Validación de texto
    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
    }


    // Getters
    public String getCodigo() {return Codigo;}
    public Double getPeso() {return peso != null ? peso : 0.0;}
    public EstadoPaquete getEstado() {return this.estado;}

    // Transiciones de estado del paquete.
    // La ubicación (origen, destino y sucursal actual) se valida en el envío.
    public void depositar() {
        if (!puedeDepositarse()) {
            throw new IllegalStateException("El paquete " + Codigo + " no puede depositarse en estado " + estado);
        }
        estado = EstadoPaquete.EN_DEPOSITO;
    }

    public void despachar() {
        if (!puedeDespacharse()) {
            throw new IllegalStateException("El paquete " + Codigo + " no puede despacharse en estado " + estado);
        }
        estado = EstadoPaquete.EN_CAMINO;
    }

    public void entregar() {
        if (!puedeEntregarse()) {
            throw new IllegalStateException("El paquete " + Codigo + " no puede entregarse en estado " + estado);
        }
        estado = EstadoPaquete.ENTREGADO;
    }

    private boolean puedeDepositarse() {
        return estaEnEstado(EstadoPaquete.REGISTRADO) || estaEnEstado(EstadoPaquete.EN_CAMINO);
    }

    private boolean puedeDespacharse() {
        return estaEnEstado(EstadoPaquete.EN_DEPOSITO);
    }

    private boolean puedeEntregarse() {
        return estaEnEstado(EstadoPaquete.EN_DEPOSITO);
    }

    private boolean estaEnEstado(EstadoPaquete estadoEsperado) {
        return estado == estadoEsperado;
    }

    // Sobrescribe equals para comparar paquetes por código
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Paquete)) return false;
        Paquete otro = (Paquete) objeto;
        return Codigo.equals(otro.Codigo);
    }

    // Sobrescribir hashCode para mantener la coherencia con equals
    @Override
    public int hashCode() {
        return Codigo.hashCode();
    }

    public static class PaqueteDuplicadoException extends Exception {
        public PaqueteDuplicadoException(String mensaje) {
            super(mensaje);
        }
    }
}
