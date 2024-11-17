import java.util.ArrayList;
import java.util.List;

public class SistemaTarjetas {
    public static void main(String[] args) {
        Tarjeta tarjeta = new Tarjeta("TARJETA123");
        List<Usuario> usuarios = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            usuarios.add(new Usuario("Usuario" + i, tarjeta));
        }

        usuarios.forEach(Thread::start);

        usuarios.forEach(usuario -> {
            try {
                usuario.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Simulación completada.");
    }
}
