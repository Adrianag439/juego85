package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Jugador;
import modelo.Niveles;
import modelo.Sesion;
import vista.App;

/**
 * Controlador de la pantalla de inicio (menú de selección de nivel).
 * <p>
 * Muestra al jugador un saludo personalizado y le presenta los botones
 * para acceder a la fase de descubrimiento del nivel Fácil o del nivel Difícil.
 * Instancia los niveles como subclases anónimas de {@link Niveles} y consulta
 * la sesión activa para personalizar la bienvenida.
 * </p>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 */
public class InicController implements Initializable {

    /** Botón que navega a la pantalla de descubrimiento del nivel Fácil. */
    @FXML 
    private Button btnfacil;

    /** Botón que navega a la pantalla de descubrimiento del nivel Difícil. */
    @FXML 
    private Button btndificil;

    /** Etiqueta que muestra el mensaje de bienvenida con el apodo del jugador activo. */
    @FXML 
    private Label lblBienvenida;

    /**
     * Configuración del nivel Fácil: id=1, 5 vidas, 10 pts base, sin temporizador.
     * Definido como subclase anónima de {@link Niveles}.
     */
    // Niveles definidos como subclases anónimas
    private Niveles nivelFacil;

    /**
     * Configuración del nivel Difícil: id=2, 3 vidas, 20 pts base, con temporizador.
     * Definido como subclase anónima de {@link Niveles}.
     */
    private Niveles nivelDificil;

    /**
     * Inicializa el controlador al cargar la vista FXML.
     * <p>
     * Instancia ambos niveles y, si existe una sesión activa con jugador válido,
     * muestra un mensaje de bienvenida personalizado en {@link #lblBienvenida}.
     * </p>
     *
     * @param url URL de localización del archivo FXML (no utilizado directamente)
     * @param rb  paquete de recursos de internacionalización (no utilizado directamente)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Nivel Fácil: id=1, nombre, 5 vidas, puntuación base 10, sin temporizador
        nivelFacil = new Niveles(1, "Fácil", 5, 10, false) {};

        // Nivel Difícil: id=2, nombre, 3 vidas, puntuación base 20, con temporizador
        nivelDificil = new Niveles(2, "Difícil", 3, 20, true) {};

        // Mostrar bienvenida con datos de la sesión activa
        Sesion sesion = IngresoController.sesionActual;
        Jugador jugador = IngresoController.jugadorSesion;

        if (lblBienvenida != null && sesion != null && sesion.isActiva() && jugador != null) {
            lblBienvenida.setText("¡Bienvenido, " + jugador.getApodo() + "!");
        }
    }

    /**
     * Maneja el evento del botón del nivel Fácil.
     * <p>
     * Navega a la pantalla de descubrimiento de la regla para el nivel Fácil
     * ({@code adivinar.fxml}).
     * </p>
     *
     * @throws IOException si ocurre un error al cargar el archivo FXML de destino
     */
    @FXML
    private void hadlebutton() throws IOException {
        // Abre la pantalla para DESCUBRIR la regla del nivel FÁCIL
        App.setRoot("adivinar");
    }

    /**
     * Maneja el evento del botón del nivel Difícil.
     * <p>
     * Navega a la pantalla de descubrimiento de la regla para el nivel Difícil
     * ({@code adivinardif.fxml}).
     * </p>
     *
     * @throws IOException si ocurre un error al cargar el archivo FXML de destino
     */
    @FXML
    private void hadledificl() throws IOException {
        // Abre la pantalla para DESCUBRIR la regla del nivel DIFÍCIL
        App.setRoot("adivinardif");
    }
}