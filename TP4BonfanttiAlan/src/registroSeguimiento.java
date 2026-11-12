import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Clase registroSeguimiento
public class registroSeguimiento {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime fechaHora;
    private TipoMovimiento tipo;
    private sucursal sucursal;

    // Constructor
    registroSeguimiento(LocalDateTime fechaHora, TipoMovimiento tipo, sucursal sucursal) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora del movimiento son obligatorias");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de movimiento es obligatorio");
        }
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal del movimiento es obligatoria");
        }
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.sucursal = sucursal;
    }

    // Getters
    public TipoMovimiento getTipo() {return tipo;}
    public sucursal getSucursal() {return sucursal;}
    public String getFechaHoraFormateada() {return fechaHora.format(FORMATO_FECHA);}

    // Representa en cadena el registro de seguimiento
    @Override
    public String toString() {
        return getFechaHoraFormateada() + " | " + getTipo() + " | " + sucursal.getNombre();
    }

}
