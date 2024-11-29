public class Usuario extends Thread {
    private final String nombre;
    private final Tarjeta tarjeta;
    private final String operacion;
    private final double monto;

    public Usuario(String nombre, Tarjeta tarjeta, String operacion, double monto) {
        this.nombre = nombre;
        this.tarjeta = tarjeta;
        this.operacion = operacion;
        this.monto = monto;
    }

    @Override
    public void run() {
        realizarOperacion();
    }

    private void realizarOperacion() {
        switch (operacion.toLowerCase()) {
            case "cargar":
                tarjeta.cargarSaldo(monto);
                break;
            case "pagar":
                tarjeta.realizarPago(monto);
                break;
            case "consultar":
                System.out.println(nombre + " consulta el saldo: " + tarjeta.consultarSaldo());
                break;
            default:
                System.out.println("Operación no válida.");
        }
    }
}
