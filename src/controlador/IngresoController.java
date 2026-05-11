package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.Jugador;
import modelo.Sesion;
import servicios.JugadorService;
import vista.App;

/**
 * Controlador de la pantalla de ingreso (registro/inicio de sesión) del juego.
 * <p>
 * Gestiona el flujo de autenticación basado en apodo: si el apodo ya existe y
 * pertenece al dispositivo actual, inicia sesión automáticamente; si pertenece
 * a otro jugador, bloquea el acceso; si es nuevo, crea el perfil y lo persiste.
 * </p>
 * <p>
 * El apodo del último jugador registrado en el dispositivo se guarda con
 * {@link Preferences} para pre-rellenar el campo en futuros accesos.
 * La sesión activa y el jugador se exponen como campos {@code static} para
 * que otros controladores los consulten sin inyección de dependencias.
 * </p>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 */
public class IngresoController implements Initializable {

    /** Campo de texto donde el jugador escribe su apodo. */
    @FXML private TextField txtApodo;

    /** Etiqueta que muestra mensajes de validación o bienvenida al jugador. */
    @FXML private Label lblMensaje;

    /** Servicio de acceso a datos para operaciones CRUD sobre {@link Jugador}. */
    private JugadorService jugadorService;

    /**
     * Sesión activa compartida con todos los controladores de la aplicación.
     * Se inicializa al registrar o autenticar a un jugador.
     */
    // Guarda la sesión y jugador para los demás controladores
    public static Sesion sesionActual;

    /**
     * Jugador activo compartido con todos los controladores de la aplicación.
     * Se inicializa al registrar o autenticar a un jugador.
     */
    public static Jugador jugadorSesion;

    /** Clave con la que se guarda el apodo en el dispositivo mediante {@link Preferences}. */
    // Clave con la que se guarda el apodo en el dispositivo
    private static final String KEY_APODO = "apodo_guardado";

    /** Preferencias del usuario a nivel de paquete para persistir el apodo localmente. */
    private Preferences prefs;

    /**
     * Inicializa el controlador al cargar la vista FXML.
     * <p>
     * Crea el servicio de jugadores, obtiene las preferencias locales del
     * dispositivo y, si existe un apodo guardado, lo pre-rellena en el campo
     * de texto con un saludo de bienvenida.
     * </p>
     *
     * @param url URL de localización del archivo FXML (no utilizado directamente)
     * @param rb  paquete de recursos de internacionalización (no utilizado directamente)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jugadorService = new JugadorService();

        // Preferences guarda datos localmente en el dispositivo
        prefs = Preferences.userNodeForPackage(IngresoController.class);

        // Si hay un apodo guardado, pre-rellenar el campo automáticamente
        String apodoGuardado = prefs.get(KEY_APODO, null);
        if (apodoGuardado != null && !apodoGuardado.isEmpty()) {
            txtApodo.setText(apodoGuardado);
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("¡Hola de nuevo, " + apodoGuardado + "!");
        }
    }

    /**
     * Maneja el evento del botón REGISTRAR/INGRESAR.
     * <p>
     * Valida el apodo ingresado y aplica la siguiente lógica:
     * <ul>
     *   <li>Si el apodo es del propio dispositivo → inicia sesión y navega a niveles.</li>
     *   <li>Si el apodo pertenece a otro jugador → muestra error y bloquea el acceso.</li>
     *   <li>Si el apodo es nuevo → crea el jugador, guarda el apodo en preferencias
     *       y navega a niveles.</li>
     * </ul>
     * </p>
     *
     * @param event evento de acción disparado por el botón
     */
    @FXML
    private void handleRegistrar(ActionEvent event) {
        String apodo = txtApodo.getText().trim();

        if (apodo.isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Ingresa un apodo para continuar");
            return;
        }
        if (apodo.length() > 10) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("El apodo debe tener máximo 10 caracteres");
            return;
        }

        try {
            Jugador jugador = jugadorService.readone(apodo);
            String apodoGuardado = prefs.get(KEY_APODO, null);

            if (jugador != null) {
                if (apodo.equals(apodoGuardado)) {
                    // Usuario propio → dejar pasar
                    jugador.setSesionActiva(true);
                    sesionActual  = new Sesion(apodo, true, new Date(), UUID.randomUUID().toString());
                    jugadorSesion = jugador;
                    
                    // ✅ CAMBIADO: Ahora va a SELECCIONAR NIVELES
                    App.setRoot("niveles");  // ← CAMBIA AQUÍ
                    
                } else {
                    // Apodo de otra persona → bloquear
                    lblMensaje.setStyle("-fx-text-fill: red;");
                    lblMensaje.setText("El apodo '" + apodo + "' ya está en uso. Elige otro.");
                    txtApodo.clear();
                    txtApodo.requestFocus();
                }
            } else {
                // Nuevo jugador → crear y guardar
                jugador = new Jugador();
                jugador.setApodo(apodo);
                jugador.setSesionActiva(true);
                jugador.setFechaRegistro(new Date());
                jugadorService.create(jugador);
                prefs.put(KEY_APODO, apodo);

                sesionActual  = new Sesion(apodo, true, new Date(), UUID.randomUUID().toString());
                jugadorSesion = jugador;
                
                // ✅ CAMBIADO: Ahora va a SELECCIONAR NIVELES
                App.setRoot("niveles");  // ← CAMBIA AQUÍ
            }

        } catch (IOException e) {
            lblMensaje.setText("Error al cargar la pantalla");
        } catch (Exception e) {
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }
}