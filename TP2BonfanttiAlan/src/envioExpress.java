public class envioExpress extends envio {

    public envioExpress(int codigo, sucursal origen, sucursal destino) {
        super(codigo, origen, destino);
    }

    protected double calcularCosto() {
        // Suma $200 por kg 
        return 1000 + (totalPeso() * 200);
    }

    @Override
    public String toString() {
        return "EnvíoExpress[" + getCodigo() + "] — Costo: $" + getCosto();
    }
}
