public class envioEstandar extends envio {

    public envioEstandar(int codigo, sucursal origen, sucursal destino) {
        super(codigo, origen, destino);
    }

    @Override
    protected double calcularCosto() {
        // Suma $100 por kg
        return 500 + (totalPeso() * 100);
    }

    @Override
    public String toString() {
        return "EnvíoEstándar[" + getCodigo() + "] — Costo: $" + getCosto();
    }
}
