import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.ReentrantLock;

public class SistemaGestionTarjetas {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaGestionTarjetas::mostrarVentanaInicioSesion);
    }

    private static void mostrarVentanaInicioSesion() {
        JFrame frame = new JFrame("Inicio de Sesión");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE_ON_CLOSE
        frame.setSize(300, 150);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Ingrese el nombre del usuario:");
        JTextField nombreField = new JTextField();

        JButton iniciarSesionButton = new JButton("Iniciar Sesión");

        iniciarSesionButton.addActionListener(e -> {
            String nombreUsuario = nombreField.getText().trim();
            if (!nombreUsuario.isEmpty()) {
                new InterfazTarjeta(nombreUsuario, 1000.0);  // Saldo inicial 1000.0
                nombreField.setText("");  // Limpiar el campo para el siguiente usuario
            } else {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(label, BorderLayout.NORTH);
        frame.add(nombreField, BorderLayout.CENTER);
        frame.add(iniciarSesionButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}

class InterfazTarjeta {
    private Tarjeta tarjeta;
    private String nombreUsuario;
    private JFrame frame;
    private JTextField montoField;
    private JTextArea outputArea;

    public InterfazTarjeta(String nombreUsuario, double saldoInicial) {
        this.nombreUsuario = nombreUsuario;
        this.tarjeta = new Tarjeta(saldoInicial, this);
        initComponents();
    }

    private void initComponents() {
        frame = new JFrame("Gestión de Tarjeta - Usuario: " + nombreUsuario);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE_ON_CLOSE para cada ventana de usuario
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        JLabel montoLabel = new JLabel("Monto:");
        montoField = new JTextField();

        topPanel.add(montoLabel);
        topPanel.add(montoField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton cargarButton = new JButton("Cargar Saldo");
        JButton pagarButton = new JButton("Realizar Pago");
        JButton consultarButton = new JButton("Consultar Saldo");

        buttonPanel.add(cargarButton);
        buttonPanel.add(pagarButton);
        buttonPanel.add(consultarButton);

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        cargarButton.addActionListener(e -> realizarOperacion("cargar"));
        pagarButton.addActionListener(e -> realizarOperacion("pagar"));
        consultarButton.addActionListener(e -> realizarOperacion("consultar"));

        frame.setVisible(true);
    }

    private void realizarOperacion(String operacion) {
        try {
            double monto = operacion.equals("consultar") ? 0 : Double.parseDouble(montoField.getText());
            Usuario usuario = new Usuario(nombreUsuario, tarjeta, operacion, monto);
            usuario.start();
            usuario.join();
        } catch (NumberFormatException ex) {
            mostrarMensaje("Por favor, introduce un monto válido.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void mostrarMensaje(String mensaje) {
        outputArea.append(mensaje + "\n");
        System.out.println("Usuario [" + nombreUsuario + "]: " + mensaje); // Mensaje en consola con el nombre del usuario
    }

    private static class Tarjeta {
        private double saldo;
        private final ReentrantLock lock = new ReentrantLock();
        private final InterfazTarjeta interfaz;

        public Tarjeta(double saldoInicial, InterfazTarjeta interfaz) {
            this.saldo = saldoInicial;
            this.interfaz = interfaz;
        }

        public void cargarSaldo(double monto) {
            lock.lock();
            try {
                saldo += monto;
                interfaz.mostrarMensaje("Saldo cargado: " + monto + ". Saldo actual: " + saldo);
            } finally {
                lock.unlock();
            }
        }

        public boolean realizarPago(double monto) {
            lock.lock();
            try {
                if (saldo >= monto) {
                    saldo -= monto;
                    interfaz.mostrarMensaje("Pago realizado: " + monto + ". Saldo actual: " + saldo);
                    return true;
                } else {
                    interfaz.mostrarMensaje("Saldo insuficiente. No se pudo realizar el pago.");
                    return false;
                }
            } finally {
                lock.unlock();
            }
        }

        public void consultarSaldo() {
            lock.lock();
            try {
                interfaz.mostrarMensaje("Saldo actual: " + saldo);
            } finally {
                lock.unlock();
            }
        }
    }

    private static class Usuario extends Thread {
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
            switch (operacion.toLowerCase()) {
                case "cargar":
                    tarjeta.cargarSaldo(monto);
                    break;
                case "pagar":
                    tarjeta.realizarPago(monto);
                    break;
                case "consultar":
                    tarjeta.consultarSaldo();
                    break;
                default:
                    tarjeta.interfaz.mostrarMensaje("Operación no válida.");
            }
        }
    }
}
