
package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import modelo.Jugador;
import modelo.Niveles;
import modelo.Sesion;

/**
 * Controlador de la ventana de reglas generales del juego.
 * <p>
 * Muestra en un área de texto no editable las instrucciones del juego para
 * ambos niveles (Fácil y Difícil). Los parámetros de cada nivel (vidas,
 * puntuación base, temporizador) se obtienen dinámicamente desde instancias
 * de {@link Niveles}, garantizando que el texto refleje siempre la configuración
 * actual. Si hay una sesión activa, el texto incluye un saludo personalizado
 * al jugador.
 * </p>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 */
public class ReglasController {

    /** Área de texto que muestra las reglas del juego (solo lectura). */
    @FXML
    private TextArea txtReglas;

    /**
     * Nivel Fácil con sus datos reales: id=1, 5 vidas, 10 pts base, sin temporizador.
     * Definido como subclase anónima de {@link Niveles}.
     */
    // Subclases anónimas de Niveles para obtener sus datos reales
    private final Niveles nivelFacil   = new Niveles(1, "Fácil",   5, 10, false) {};

    /**
     * Nivel Difícil con sus datos reales: id=2, 3 vidas, 20 pts base, con temporizador.
     * Definido como subclase anónima de {@link Niveles}.
     */
    private final Niveles nivelDificil = new Niveles(2, "Difícil", 3, 20, true)  {};

    /**
     * Inicializa el controlador al cargar la vista FXML.
     * <p>
     * Lee el jugador y la sesión activa desde {@link IngresoController},
     * construye el texto de reglas con datos reales de los niveles y un saludo
     * personalizado (si aplica), y lo establece en {@link #txtReglas} como
     * contenido no editable.
     * </p>
     */
    @FXML
    public void initialize() {

        // Leer jugador y sesión activa
        Jugador jugador = IngresoController.jugadorSesion;
        Sesion  sesion  = IngresoController.sesionActual;

        // Saludo personalizado si hay sesión activa
        String saludo = "";
        if (sesion != null && sesion.isActiva() && jugador != null) {
            saludo = "👤 Jugador: " + jugador.getApodo() + "\n\n";
        }

        // Construir texto de reglas con datos reales de los niveles
        String reglas =
            saludo +
            "🎯 OBJETIVO DEL JUEGO\n\n" +
            "Debes descubrir la regla secreta que transforma un número\n" +
            "de ENTRADA en un número de SALIDA.\n\n" +

            "🧩 PRIMERA FASE — DESCUBRIMIENTO\n" +
            "• Ingresa un número y presiona ENTER.\n" +
            "• El sistema mostrará el resultado según la regla secreta.\n" +
            "• Prueba varios números para identificar el patrón.\n\n" +

            "🚀 CUANDO ESTÉS LISTO\n" +
            "• Presiona el botón \"ESTOY LISTO\".\n" +
            "• Se abrirá una nueva ventana para elegir el nivel.\n\n" +

            // Nivel fácil: datos tomados de la clase Niveles
            "🟢 " + nivelFacil.getNombre().toUpperCase() + "\n" +
            "• Intentos disponibles : " + nivelFacil.getVidas() + "\n" +
            "• Puntos por acierto   : " + nivelFacil.getPuntuacionBase() + "\n" +
            "• Temporizador         : " + (nivelFacil.isTieneTemporizador() ? "Sí" : "No") + "\n" +
            "• Ingresa el número y completa la columna RESULTADO.\n" +
            "• Presiona VERIFICAR para validar tu respuesta.\n" +
            "• Si fallas los " + nivelFacil.getVidas() + " intentos, se activan las pistas.\n\n" +

            // Nivel difícil: datos tomados de la clase Niveles
            "🔴 " + nivelDificil.getNombre().toUpperCase() + "\n" +
            "• Intentos disponibles : " + nivelDificil.getVidas() + "\n" +
            "• Puntos por acierto   : " + nivelDificil.getPuntuacionBase() + "\n" +
            "• Temporizador         : " + (nivelDificil.isTieneTemporizador() ? "Sí — 3 minutos" : "No") + "\n" +
            "• No hay pistas disponibles.\n" +
            "• Debes resolver antes de quedarte sin intentos o tiempo.\n\n" +

            "🏆 OBJETIVO FINAL\n" +
            "Descubrir correctamente la regla antes de quedarte\n" +
            "sin intentos o sin tiempo.";

        txtReglas.setText(reglas);
        txtReglas.setEditable(false);
    }

    /**
     * Maneja el evento del botón de cierre de la ventana.
     * <p>
     * Obtiene el {@link Stage} a partir del nodo origen del evento y lo cierra.
     * </p>
     *
     * @param event evento de acción disparado por el botón de cierre
     */
    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}