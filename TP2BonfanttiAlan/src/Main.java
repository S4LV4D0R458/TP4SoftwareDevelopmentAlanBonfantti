public class Main {
    public static void main(String[] args) throws Paquete.PaqueteDuplicadoException,
            RegistroCentral.EnvioDuplicadoException, RegistroCentral.EnvioNoEncontradoException,
            RegistroCentral.ClienteDuplicadoException, RegistroCentral.SucursalDuplicadaException,
            RegistroCentral.ClienteNoEncontradoException {
        // Crea las sucursales, el paquete, un envío internacional y una persona
        sucursal sucursal1 = new sucursal(43921, "Santiago");
        sucursal sucursal2 = new sucursal(21392130, "Buenos Aires");
        sucursal sucursal3 = new sucursal(2024958712, "Viedma");
        sucursal casa = new sucursal(1232142141, "Casa - Don Bosco 135, Lamarque");
        Paquete paquete1 = new Paquete("KXS241", "Notebook ASUS TUF 15", 12.3, "queseyo");
        envioInternacional envio1 = new envioInternacional(8001, "Argentina", sucursal1, casa);
        persona persona1 = new persona("Alan", "ValentinELMEJORPROFE@gmail.com", 34321221);

        // Única instancia del registro central, creada en el main
        RegistroCentral registro = new RegistroCentral();
        registro.registrarSucursal(sucursal1);
        registro.registrarSucursal(sucursal2);
        registro.registrarSucursal(sucursal3);
        registro.registrarSucursal(casa);
        registro.registrarCliente(persona1);
        registro.registrarEnvio(envio1);

        // Agrega paquete y envío
        envio1.agregarPaquete(paquete1);
        persona1.agregarEnvio(envio1);

        sucursal[] recorrido = {sucursal1, sucursal2, sucursal3, casa};


        // Muestra los datos del envío
        System.out.println("=== DATOS DEL ENVÍO ===");
        System.out.println("ID Dueño: " + persona1.getID());
        System.out.println("Costo total paquete1: $" + envio1.getCosto());
        System.out.println("Paquete: " + paquete1.getCodigo() + " | Estado inicial: " + paquete1.getEstado());
        System.out.println("Origen: " + envio1.getOrigen().getNombre()
                + " | Destino: " + envio1.getDestino().getNombre());

        // Simula el recorrido del envío: cada sucursal lo encola y despacha en FIFO
        System.out.println("\n=== RECORRIDO DEL ENVÍO ===");
        for (int indice = 0; indice < recorrido.length; indice++) {
            sucursal sucursalActual = recorrido[indice];
            System.out.println(sucursalActual.recibirEnvio(envio1));
            if (indice < recorrido.length - 1) {
                System.out.println(sucursalActual.despachar(1));
            }
        }
        System.out.println(casa.entregarEnvio(envio1));

        // Muestra el comportamiento de la cola cuando ya no quedan envíos
        System.out.println("\n=== COLA DE DESPACHO ===");
        System.out.println("Pendientes en Santiago: " + sucursal1.cantidadPendientes());
        System.out.println(sucursal1.despachar(1));

        // Consulta los datos del historial y los muestra
        System.out.println("=== HISTORIAL DEL ENVÍO ===");
        for (registroSeguimiento movimiento : envio1.getHistorial()) {
            System.out.println(movimiento);
        }
        System.out.print("Sucursales recorridas: ");
        for (int indice = 0; indice < envio1.getSucursalesRecorridas().size(); indice++) {
            if (indice > 0) {
                System.out.print(" -> ");
            }
            System.out.print(envio1.getSucursalesRecorridas().get(indice).getNombre());
        }
        System.out.println();
        System.out.println("Último movimiento: " + envio1.ultimoMovimiento());
        System.out.println("Estado final del paquete: " + paquete1.getEstado());
        System.out.println("Paquete cifrado: " + paquete1.hashCode());

        // Consulta los datos registrados en el sistema
        System.out.println("\n=== REGISTRO CENTRAL ===");
        System.out.println("Envíos registrados: " + registro.listarEnvios().size());
        System.out.println("Clientes registrados: " + registro.listarClientes().size());
        System.out.println("Sucursales registradas: " + registro.listarSucursales().size());
        System.out.println("Envío encontrado: " + registro.buscarEnvio(8001));
        System.out.println("Cliente encontrado por DNI: " + registro.buscarClienteSecuencial(persona1.getID()).getID());
    }
}
