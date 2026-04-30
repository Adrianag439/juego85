package controlador;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import modelo.Jugador;
import servicios.JugadorService;
import vista.App;

/**
 * Controlador de la pantalla inicial de registro de jugador.
 * <p>
 * Permite al jugador ingresar un apodo único para comenzar a jugar.
 * Realiza las siguientes validaciones antes de registrar al jugador:
 * </p>
 * <ul>
 *   <li>El apodo no puede estar vacío.</li>
 *   <li>El apodo no puede superar 10 caracteres.</li>
 *   <li>El apodo no debe estar ya registrado en la base de datos.</li>
 * </ul>
 * <p>
 * Si el apodo es válido, crea el jugador en la BD y navega a la
 * pantalla de selección de nivel.
 * </p>
 *
 * <p><b>FXML asociado:</b> {@code /fx/ingreso.fxml}</p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class IngresoController implements Initializable {

    /** Campo de texto donde el jugador escribe su apodo. */
    @FXML private TextField txtApodo;

    /** Etiqueta para mostrar mensajes de error o validación al usuario. */
    @FXML private Label lblMensaje;

    /** Servicio DAO para operaciones sobre la entidad Jugador. */
    private JugadorService jugadorService;

    /**
     * Inicializa el controlador creando la instancia de {@link JugadorService}.
     *
     * @param url            ubicación del archivo FXML (no utilizada)
     * @param rb             paquete de recursos de internacionalización (no utilizado)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jugadorService = new JugadorService();
    }

    /**
     * Maneja el evento del botón "INGRESAR".
     * <p>
     * Ejecuta las validaciones del apodo en orden:
     * vacío → longitud → duplicado en BD.
     * Si todo es válido, crea el jugador y navega a la vista {@code niveles}.
     * </p>
     *
     * @param event evento de acción del botón (no utilizado directamente)
     */
    @FXML
    private void handleRegistrar(ActionEvent event) {
        String apodo = txtApodo.getText().trim();

        // Validación 1: apodo no vacío
        if (apodo.isEmpty()) {
            lblMensaje.setText("Ingresa un apodo para continuar");
            return;
        }

        // Validación 2: máximo 10 caracteres
        if (apodo.length() > 10) {
            lblMensaje.setText("El apodo debe tener maximo 10 caracteres");
            return;
        }

        try {
            // Validación 3: verificar que el apodo no esté en uso
            Jugador jugador = jugadorService.readone(apodo);

            if (jugador != null) {
                // El apodo ya existe en la base de datos
                lblMensaje.setStyle("-fx-text-fill: red;");
                lblMensaje.setText("El apodo " + apodo + " ya esta en uso. Elige otro.");
                txtApodo.clear();
                txtApodo.requestFocus();
                return;
            }

            // Crea el nuevo jugador y lo persiste en la BD
            jugador = new Jugador();
            jugador.setApodo(apodo);
            jugador.setSesionActiva(true);
            jugador.setFechaRegistro(new Date());
            jugadorService.create(jugador);

            // Navega a la pantalla de selección de nivel
            App.setRoot("niveles");

        } catch (IOException e) {
            lblMensaje.setText("Error al cargar niveles");
        } catch (Exception e) {
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }
}
