import java.util.ArrayList;
import java.util.List;

public class SistemaTarjetas {
    private final List<Usuario> usuarios = new ArrayList<>();

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void iniciarSimulacion() {
        for (Usuario usuario : usuarios) {
            usuario.start();
        }
        for (Usuario usuario : usuarios) {
            try {
                usuario.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
