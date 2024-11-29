import java.util.concurrent.locks.ReentrantLock;

public class Tarjeta {
    private double saldo;
    private final ReentrantLock lock = new ReentrantLock();

    public Tarjeta(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public void cargarSaldo(double monto) {
        lock.lock();
        try {
            saldo += monto;
            System.out.println("Saldo cargado: " + monto + ". Saldo actual: " + saldo);
        } finally {
            lock.unlock();
        }
    }

    public boolean realizarPago(double monto) {
        lock.lock();
        try {
            if (saldo >= monto) {
                saldo -= monto;
                System.out.println("Pago realizado: " + monto + ". Saldo actual: " + saldo);
                return true;
            } else {
                System.out.println("Saldo insuficiente.");
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public double consultarSaldo() {
        lock.lock();
        try {
            return saldo;
        } finally {
            lock.unlock();
        }
    }
}
