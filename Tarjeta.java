import java.util.concurrent.locks.ReentrantLock;

public class Tarjeta {
    private double saldo;
    private final String idTarjeta;
    private final ReentrantLock lock = new ReentrantLock();

    public Tarjeta(String idTarjeta) {
        this.idTarjeta = idTarjeta;
        this.saldo = 0;
    }

    public void cargarSaldo(double monto) {
        lock.lock();
        try {
            if (monto > 0) {
                saldo += monto;
                System.out.println("Saldo cargado: " + monto + " | Saldo actual: " + saldo);
            } else {
                System.out.println("Monto no válido para cargar saldo.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void realizarPago(double monto) {
        lock.lock();
        try {
            if (monto > 0 && saldo >= monto) {
                saldo -= monto;
                System.out.println("Pago realizado: " + monto + " | Saldo actual: " + saldo);
            } else {
                System.out.println("Pago rechazado. Fondos insuficientes o monto inválido.");
            }
        } finally {
            lock.unlock();
        }
    }

    public double consultarSaldo() {
        lock.lock();
        try {
            System.out.println("Saldo actual: " + saldo);
            return saldo;
        } finally {
            lock.unlock();
        }
    }
}
