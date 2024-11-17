public class Usuario extends Thread {
    private final String idUsuario;
    private final Tarjeta tarjeta;

    public Usuario(String idUsuario, Tarjeta tarjeta) {
        this.idUsuario = idUsuario;
        this.tarjeta = tarjeta;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            double monto = Math.random() * 100;
            int operacion = (int) (Math.random() * 3);

            switch (operacion) {
                case 0 -> tarjeta.cargarSaldo(monto);
                case 1 -> tarjeta.realizarPago(monto);
                case 2 -> tarjeta.consultarSaldo();
            }

            try {
                Thread.sleep((int) (Math.random() * 1000)); // Simula el tiempo entre operaciones
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
