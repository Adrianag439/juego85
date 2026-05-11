package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Jugador;
import modelo.Niveles;
import modelo.Partida;
import modelo.Sesion;

/**
 * Controlador JavaFX de la pantalla de <b>Puntaje</b>.
 *
 * <p>Muestra un resumen de la partida recién finalizada: apodo del jugador,
 * puntuación obtenida, nivel jugado, puntos por acierto y estado de la sesión.
 * Los datos se reciben a través de los campos estáticos {@link #puntajeActual}
 * y {@link #nivelActual}, que deben ser asignados por el controlador que
 * abre esta ventana antes de hacer la transición.</p>
 *
 ** <p><b>Ejemplo de uso desde otro controlador:</b></p>
 * <pre>{@code
 * PuntajeController.puntajeActual = partida.getPuntuacion();
 * PuntajeController.nivelActual   = "Difícil";
 * App.openWindow("puntaje", "Puntaje");
 * }</pre>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 * @see DificilController
 * @see PruebaController
 */
public class PuntajeController implements Initializable {

    // -------------------------------------------------------------------------
    // Componentes FXML
    // -------------------------------------------------------------------------

    /** Etiqueta que muestra el apodo del jugador activo en sesión. */
    @FXML private Label lbJugador;

    /** Etiqueta que muestra la puntuación total obtenida seguida de " pts". */
    @FXML private Label lbPuntaje;

    /** Etiqueta que muestra el nombre del nivel jugado (p.ej. "Fácil" o "Difícil"). */
    @FXML private Label lbNivel;

    /** Etiqueta que muestra los puntos otorgados por cada respuesta correcta. */
    @FXML private Label lbVidas;

    /** Etiqueta que indica si la sesión del jugador está activa o cerrada. */
    @FXML private Label lbSesion;

    // -------------------------------------------------------------------------
    // Estado compartido (estático)
    // -------------------------------------------------------------------------

    /**
     * Puntuación a mostrar en esta pantalla.
     *
     * <p>Debe ser asignado por el controlador de origen antes de abrir esta
     * ventana. Se reinicia a {@code 0} al pulsar el botón de reinicio.</p>
     */
    public static int puntajeActual = 0;

    /**
     * Nombre del nivel jugado ({@code "Fácil"} o {@code "Difícil"}).
     *
     * <p>Determina qué FXML se carga al reiniciar y qué configuración de
     * {@link Niveles} se utiliza para mostrar los detalles.</p>
     */
    public static String nivelActual = "";

    // -------------------------------------------------------------------------
    // Estado interno
    // -------------------------------------------------------------------------

    /** Instancia de partida que encapsula la puntuación recibida. */
    private Partida partida;

    /** Configuración del nivel correspondiente a {@link #nivelActual}. */
    private Niveles nivel;

    /** Jugador obtenido desde la sesión activa en {@link IngresoController}. */
    private Jugador jugador;

    /** Sesión activa obtenida desde {@link IngresoController}. */
    private Sesion sesion;

    // -------------------------------------------------------------------------
    // Inicialización
    // -------------------------------------------------------------------------

    /**
     * Método de inicialización invocado automáticamente por el {@code FXMLLoader}
     * tras cargar el archivo FXML asociado.
     *
     * <p>Construye la partida con {@link #puntajeActual}, configura el {@link Niveles}
     * apropiado según {@link #nivelActual} y actualiza todas las etiquetas de la vista.</p>
     *
     * @param url ubicación del archivo FXML (puede ser {@code null}).
     * @param rb  paquete de recursos de internacionalización (puede ser {@code null}).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partida = new Partida();
        partida.setPuntuacion(puntajeActual);

        if ("Difícil".equals(nivelActual)) {
            nivel = new Niveles(2, "Difícil", 3, 20, true) {};
        } else {
            nivel = new Niveles(1, "Fácil", 5, 10, false) {};
        }

        jugador = IngresoController.jugadorSesion;
        if (jugador != null) {
            lbJugador.setText(jugador.getApodo());
        }

        sesion = IngresoController.sesionActual;
        if (lbSesion != null && sesion != null) {
            lbSesion.setText(sesion.isActiva() ? "Sesión activa" : "Sesión cerrada");
        }

        lbPuntaje.setText(partida.getPuntuacion() + " pts");
        lbNivel.setText(nivel.getNombre());

        if (lbVidas != null) {
            lbVidas.setText("Puntos por acierto: " + nivel.getPuntuacionBase());
        }
    }

    // -------------------------------------------------------------------------
    // Manejadores de eventos FXML
    // -------------------------------------------------------------------------

    /**
     * Manejador del botón <em>Reiniciar</em>.
     *
     * <p>Pone a cero {@link #puntajeActual}, determina el FXML del nivel
     * correspondiente a {@link #nivelActual} y carga la escena de juego en la
     * ventana actual.</p>
     *
     * @param event evento de acción disparado por el botón.
     */
    @FXML
    private void handleReiniciar(ActionEvent event) {
        try {
            PuntajeController.puntajeActual = 0;

            String fxml;
            if ("Difícil".equals(nivelActual)) {
                fxml = "/fx/dificil.fxml";
            } else {
                fxml = "/fx/facil.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) lbPuntaje.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Manejador del botón <em>Salir</em>.
     *
     * <p>Cierra la aplicación JavaFX por completo mediante {@link Platform#exit()}
     * y finaliza la JVM con {@link System#exit(int)} código {@code 0}.</p>
     *
     * @param event evento de acción disparado por el botón.
     */
    @FXML
    private void handleSalir(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}