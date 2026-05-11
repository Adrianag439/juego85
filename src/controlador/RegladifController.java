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
 * Controlador de la ventana de reglas del nivel Difícil.
 * <p>
 * Muestra en un área de texto no editable las instrucciones completas del
 * modo difícil, incluyendo objetivo, datos del nivel, fases de juego,
 * condiciones de victoria/derrota, consejos y un ejemplo práctico.
 * Si hay una sesión activa, encabeza el texto con un saludo personalizado
 * al jugador.
 * </p>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 */
public class RegladifController {

    /** Área de texto que muestra las reglas del nivel Difícil (solo lectura). */
    @FXML
    private TextArea txtReglas;

    /** Nivel difícil con sus datos reales: id=2, 3 vidas, 20 pts base, con temporizador. */
    // Nivel difícil con sus datos reales
    private final Niveles nivelDificil = new Niveles(2, "Difícil", 3, 20, true) {};

    /**
     * Inicializa el controlador al cargar la vista FXML.
     * <p>
     * Lee el jugador y la sesión activa desde {@link IngresoController},
     * construye el texto de reglas con un saludo personalizado (si aplica)
     * y lo establece en {@link #txtReglas} como contenido no editable.
     * </p>
     */
    @FXML
    public void initialize() {
 txtReglas.setStyle("-fx-text-fill: white; -fx-control-inner-background: #1e1e2f;");
        // Leer jugador y sesión activa
        Jugador jugador = IngresoController.jugadorSesion;
        Sesion sesion = IngresoController.sesionActual;

        // Saludo personalizado si hay sesión activa
        String saludo = "";
        if (sesion != null && sesion.isActiva() && jugador != null) {
            saludo = "👤 Jugador: " + jugador.getApodo() + "\n\n";
        }

        // Construir texto solo del nivel difícil
        String reglas = construirTextoReglas(saludo);

        txtReglas.setText(reglas);
        txtReglas.setEditable(false);
    }

    /**
     * Construye el texto completo de las reglas del nivel Difícil.
     * <p>
     * Incorpora el saludo recibido como parámetro y rellena los valores
     * dinámicos (intentos, puntos, rango de números) desde {@link #nivelDificil}.
     * </p>
     *
     * @param saludo cadena con el saludo personalizado al jugador, o vacía si
     *               no hay sesión activa
     * @return texto formateado con las reglas completas del modo difícil
     */
    private String construirTextoReglas(String saludo) {
        return saludo +
                        "🔴 MODO DIFÍCIL\n" +
            
            "🎯 OBJETIVO DEL JUEGO\n" +
            
            "Debes DESCUBRIR la REGLA SECRETA que transforma un número\n" +
            "de ENTRADA en un número de SALIDA.\n\n" +

          
            "⚠️ DATOS DEL NIVEL\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• Intentos disponibles : " + nivelDificil.getVidas() + " INTENTOS\n" +
            "• Puntos por acierto   : " + nivelDificil.getPuntuacionBase() + " PUNTOS\n" +
            "• Temporizador         : ⏱️ SÍ — 3 MINUTOS\n" +
            "• Pistas disponibles   : ❌ NO HAY PISTAS\n" +
            "• Rango de números      : 10 al 55\n\n" +

            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "📋 CÓMO JUGAR\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "1️⃣ PRIMERA FASE — DESCUBRIMIENTO:\n" +
            "   • Ingresa un número del 10 al 55.\n" +
            "   • Presiona ENTER y el sistema mostrará el RESULTADO.\n" +
            "   • La tabla registrará tus intentos.\n" +
            "   • Prueba varios números para identificar el patrón.\n\n" +
            
            "2️⃣ SEGUNDA FASE — VERIFICACIÓN:\n" +
            "   • Cuando creas saber la regla, presiona \"ESTOY LISTO\".\n" +
            "   • Se abrirá una nueva ventana para jugar el nivel.\n" +
            "   • Ingresa el número y escribe el RESULTADO que creas correcto.\n" +
            "   • Presiona VERIFICAR para comprobar tu respuesta.\n\n" +

            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "✅ CONDICIÓN DE VICTORIA\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• Acertar el resultado correspondiente según la regla secreta.\n" +
            "• Si aciertas → ganas " + nivelDificil.getPuntuacionBase() + " puntos.\n\n" +

            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "❌ CONDICIONES DE DERROTA\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• Si usas todos tus " + nivelDificil.getVidas() + " INTENTOS → GAME OVER\n" +
            "• Si se acaba el TIEMPO (3 minutos) → GAME OVER\n" +
            "• Si fallas un intento → pierdes 1 oportunidad\n" +
            "• Cada fallo te mostrará el RESULTADO CORRECTO\n\n" +

            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "💡 CONSEJOS PARA GANAR\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• Prueba números de 1 dígito y 2 dígitos.\n" +
            "• Observa cómo cambia el resultado con cada número.\n" +
            "• Compara diferentes números para encontrar el patrón.\n" +
            "• Recuerda analizar los dígitos individualmente.\n\n" +

            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🎲 EJEMPLO PRÁCTICO\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Si la regla secreta fuera \"SUMAR LOS DÍGITOS\":\n\n" +
            "• Número 23 → 2 + 3 = 5\n" +
            "• Número 45 → 4 + 5 = 9\n" +
            "• Número 10 → 1 + 0 = 1\n\n" +
            "🔍 ¡TÚ DEBES DESCUBRIR CUÁL ES LA REGLA SECRETA!\n\n" +

            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🎯 ¡DEMUESTRA TU INGENIO Y SUPERA EL MODO DIFÍCIL!\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
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