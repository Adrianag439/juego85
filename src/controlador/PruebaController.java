package controlador;

import con.mijuego.Resultado;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import java.io.IOException;
import vista.App;
import javafx.scene.control.Alert.AlertType;
import modelo.Jugador;
import modelo.Niveles;
import modelo.Partida;
import modelo.Pista;
import modelo.Sesion;
import servicios.JugadorService;

/**
 * Controlador JavaFX para el nivel <b>Fácil</b> del juego de factoriales.
 *
 * <p>El jugador debe calcular el factorial de números enteros del 1 al
 * {@value #NUMERO_MAX}. Se presentan {@value #CANTIDAD_NUMEROS} números
 * simultáneamente y dispone de {@value #TOTAL_INTENTOS} intentos y hasta
 * {@value #TOTAL_PISTAS} pistas.</p>
 *
 * <p><b>SFlujo de juego:</b></p>
 * <ol>
 *   <li>El jugador escribe los factoriales en la columna RESULTADO.</li>
 *   <li>Pulsa <em>Verificar</em>; las filas correctas quedan bloqueadas (✅).</li>
 *   <li>Si agota los {@value #TOTAL_INTENTOS} intentos con filas pendientes,
 *       pasa a la fase de pistas.</li>
 *   <li>En la fase de pistas puede usar hasta {@value #TOTAL_PISTAS} pistas
 *       antes de que la partida finalice.</li>
 * </ol>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 * @see DificilController
 * @see PuntajeController
 */
public class PruebaController implements Initializable {

    // -------------------------------------------------------------------------
    // Componentes FXML
    // -------------------------------------------------------------------------

    /** Etiqueta que muestra el número de intentos o pistas restantes según la fase. */
    @FXML private Label lb_intentos;

    /**
     * Campo de texto auxiliar (actualmente oculto en la UI).
     * Reservado para futuros ingresos de datos adicionales.
     */
    @FXML private TextField intenoone;

    /**
     * Tabla principal del juego; cada fila contiene el número generado,
     * el campo editable para la respuesta y el estado (✅ / ❌).
     */
    @FXML private TableView<Resultado> tablenumbers;

    /** Botón que dispara la verificación de respuestas ingresadas. */
    @FXML private Button veri;

    /** Botón que reinicia completamente la partida actual. */
    @FXML private Button reini;

    /** Botón que solicita y muestra una pista; se habilita solo en fase de pistas. */
    @FXML private Button pista;

    // -------------------------------------------------------------------------
    // Estado interno
    // -------------------------------------------------------------------------

    /** Lista observable de resultados enlazada a {@link #tablenumbers}. */
    private ObservableList<Resultado> listaResultados;

    /** Servicio de acceso a datos para la entidad {@link Jugador}. */
    private JugadorService daoJugador;

    /** Jugador activo en la sesión actual. */
    private Jugador jugadorActual;

    /** Objeto que modela el estado de la partida en curso. */
    private Partida partida;

    /** Configuración del nivel Fácil (id=1, sin temporizador). */
    private Niveles nivel;

    /** Objeto que genera y almacena el texto de la pista actual. */
    private Pista pistaActual;

    // -------------------------------------------------------------------------
    // Constantes del nivel
    // -------------------------------------------------------------------------

    /** Número de filas/números mostrados en la tabla por ronda. */
    private static final int CANTIDAD_NUMEROS = 6;

    /** Valor mínimo (inclusive) del rango de números generados. */
    private static final int NUMERO_MIN = 1;

    /** Valor máximo (inclusive) del rango de números generados. */
    private static final int NUMERO_MAX = 10;

    /** Total de intentos de verificación disponibles en la fase normal. */
    private static final int TOTAL_INTENTOS = 5;

    /** Total de pistas disponibles tras agotar los intentos. */
    private static final int TOTAL_PISTAS = 3;

    // -------------------------------------------------------------------------
    // Variables de estado mutable
    // -------------------------------------------------------------------------

    /** Contador de intentos que decrece con cada verificación fallida. */
    private int intentosRestantes;

    /** Contador de pistas disponibles que decrece con cada pista usada. */
    private int pistasRestantes;

    /**
     * Indica si la partida se encuentra en la <em>fase de pistas</em>
     * ({@code true}) o en la fase normal de intentos ({@code false}).
     */
    private boolean faseHints;

    // -------------------------------------------------------------------------
    // Inicialización
    // -------------------------------------------------------------------------

    /**
     * Método de inicialización invocado automáticamente por el {@code FXMLLoader}
     * tras cargar el archivo FXML asociado.
     *
     * <p>Configura el servicio de datos, la partida, el nivel, oculta el campo
     * auxiliar, construye las columnas de la tabla, genera los números aleatorios
     * y carga el jugador desde la sesión activa.</p>
     *
     * @param url ubicación del archivo FXML (puede ser {@code null}).
     * @param rb  paquete de recursos de internacionalización (puede ser {@code null}).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        daoJugador  = new JugadorService();
        pistaActual = new Pista();
        partida     = new Partida();
        nivel       = new Niveles(1, "Fácil", TOTAL_INTENTOS, 10, false) {};

        intenoone.setVisible(false);
        intenoone.setManaged(false);

        listaResultados = FXCollections.observableArrayList();
        tablenumbers.setEditable(true);

        construirColumnas();
        tablenumbers.setItems(listaResultados);

        reiniciarEstado();
        generarNumerosAleatorios();
        cargarJugador();
    }

    // -------------------------------------------------------------------------
    // Métodos privados de configuración
    // -------------------------------------------------------------------------

    /**
     * Restablece todos los contadores de intento, pistas y fase al estado
     * inicial de una nueva partida.
     */
    private void reiniciarEstado() {
        intentosRestantes = TOTAL_INTENTOS;
        pistasRestantes   = TOTAL_PISTAS;
        faseHints         = false;

        partida.setActiva(true);
        partida.setVidasRestantes(intentosRestantes);

        veri.setDisable(false);
        pista.setDisable(true);
        pista.setText("Pista (" + pistasRestantes + ")");
        actualizarLabel();
    }

    /**
     * Actualiza el texto de {@link #lb_intentos} según la fase activa:
     * muestra pistas restantes en fase de pistas, o intentos en fase normal.
     */
    private void actualizarLabel() {
        if (faseHints) {
            lb_intentos.setText("Pistas restantes: " + pistasRestantes);
        } else {
            lb_intentos.setText("INTENTOS: " + intentosRestantes);
        }
    }

    /**
     * Construye y registra las tres columnas de {@link #tablenumbers}:
     * <ol>
     *   <li><b>#INGRESADO</b> – número generado (solo lectura).</li>
     *   <li><b>RESULTADO</b>  – campo editable donde el jugador escribe su respuesta.</li>
     *   <li><b>ESTADO</b>     – icono ✅ o ❌ tras verificar.</li>
     * </ol>
     */
    private void construirColumnas() {
        tablenumbers.getColumns().clear();

        TableColumn<Resultado, Integer> colNumero = new TableColumn<>("#INGRESADO");
        colNumero.setPrefWidth(140);
        colNumero.setEditable(false);
        colNumero.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getNumero()));

        TableColumn<Resultado, Double> colResultado = new TableColumn<>("RESULTADO");
        colResultado.setPrefWidth(180);
        colResultado.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getResultado()));
        colResultado.setCellFactory(col -> new TableCell<Resultado, Double>() {
            private final TextField tf = new TextField();
            {
                tf.textProperty().addListener((obs, oldVal, newVal) -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < listaResultados.size()) {
                        try {
                            double val = Double.parseDouble(newVal.trim());
                            listaResultados.get(idx).setResultado(val);
                        } catch (NumberFormatException ignored) {}
                    }
                });
            }
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                int idx = getIndex();
                if (empty || idx < 0 || idx >= listaResultados.size()) {
                    setGraphic(null); setText(null); return;
                }
                Resultado r = listaResultados.get(idx);
                if (r.isEditable()) {
                    tf.setText(item == null || item == 0.0
                        ? "" : String.valueOf(item.longValue()));
                    setGraphic(tf); setText(null);
                } else {
                    setGraphic(null);
                    setText(item == null || item == 0.0
                        ? "—" : String.valueOf(item.longValue()));
                }
            }
        });

        TableColumn<Resultado, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setPrefWidth(100);
        colEstado.setEditable(false);
        colEstado.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getEstado()));
        colEstado.setCellFactory(col -> new TableCell<Resultado, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");
                }
            }
        });

        tablenumbers.getColumns().setAll(colNumero, colResultado, colEstado);
    }

    /**
     * Limpia la lista de resultados y genera {@value #CANTIDAD_NUMEROS} números
     * aleatorios únicos dentro del rango [{@value #NUMERO_MIN}, {@value #NUMERO_MAX}],
     * todos marcados como editables para la nueva ronda.
     */
    private void generarNumerosAleatorios() {
        listaResultados.clear();
        List<Integer> numeros = new ArrayList<>();
        for (int i = NUMERO_MIN; i <= NUMERO_MAX; i++) numeros.add(i);
        Collections.shuffle(numeros);
        for (int i = 0; i < CANTIDAD_NUMEROS; i++)
            listaResultados.add(new Resultado(numeros.get(i), 0.0, true));
        tablenumbers.refresh();
    }

    // -------------------------------------------------------------------------
    // Manejadores de eventos FXML
    // -------------------------------------------------------------------------

    /**
     * Manejador del botón <em>Verificar</em>.
     *
     * <p>Evalúa cada fila editable comparando la respuesta del jugador con
     * {@link Partida#calcularResultadoCorrecto(int)}. Actualiza el estado visual
     * (✅ / ❌) y gestiona la transición entre la fase de intentos y la fase de
     * pistas, o finaliza la partida según corresponda.</p>
     *
     * @param event evento de acción disparado por el botón.
     */
    @FXML
    private void button_veri(ActionEvent event) {
        if (!partida.isActiva() || listaResultados.isEmpty()) return;

        int correctos   = 0;
        int incorrectos = 0;

        for (Resultado fila : listaResultados) {
            if (!fila.isEditable()) { correctos++; continue; }

            double respuestaCorrecta = partida.calcularResultadoCorrecto(fila.getNumero());

            if (fila.getResultado() == respuestaCorrecta) {
                fila.setEstado("✅");
                fila.setEditable(false);
                correctos++;
            } else {
                fila.setEstado("❌");
                incorrectos++;
            }
        }

        tablenumbers.refresh();

        if (!faseHints) {
            intentosRestantes--;
            partida.setVidasRestantes(intentosRestantes);
        }

        actualizarLabel();

        if (incorrectos == 0) {
            int puntosGanados = correctos * nivel.getPuntuacionBase();
            partida.setPuntuacion(partida.getPuntuacion() + puntosGanados);
            veri.setDisable(true);
            pista.setDisable(true);
            mostrarOpcionesAlGanar(puntosGanados);
            return;
        }

        if (!faseHints) {
            if (intentosRestantes == 0) {
                if (pistasRestantes > 0) {
                    faseHints = true;
                    actualizarLabel();
                    veri.setDisable(true);
                    pista.setDisable(false);
                    mostrarAlerta(AlertType.INFORMATION,
                        "⚠️ ¡Sin intentos!\n\n"
                        + "Tienes " + pistasRestantes + " pista(s).\n"
                        + "Usa una pista y luego pulsa Verificar.");
                } else {
                    terminarPartida();
                }
            }
        } else {
            if (pistasRestantes > 0) {
                veri.setDisable(true);
                pista.setDisable(false);
                actualizarLabel();
            } else {
                terminarPartida();
            }
        }
    }

    /**
     * Manejador del botón <em>Pista</em>.
     *
     * <p>Selecciona el primer número editable pendiente, calcula su factorial
     * mediante {@link Partida#calcularFactorial(int)}, genera el texto de la
     * pista con {@link Pista#generarPista(int, long)} y lo muestra en un diálogo.
     * Decrementa {@link #pistasRestantes} y ajusta el estado de los botones.</p>
     *
     * @param event evento de acción disparado por el botón.
     */
    @FXML
    private void hadel_pista(ActionEvent event) {
        if (!partida.isActiva() || pistasRestantes <= 0) return;

        int numeroParaPista = 0;
        for (Resultado r : listaResultados) {
            if (r.isEditable() && r.getNumero() > 0) {
                numeroParaPista = r.getNumero();
                break;
            }
        }

        if (numeroParaPista == 0) {
            mostrarAlerta(AlertType.INFORMATION, "No hay números pendientes.");
            return;
        }

        long factorial = partida.calcularFactorial(numeroParaPista);
        pistaActual.generarPista(numeroParaPista, factorial);

        int numPista = TOTAL_PISTAS - pistasRestantes + 1;
        mostrarAlerta(AlertType.INFORMATION,
            "💡 PISTA " + numPista + "/" + TOTAL_PISTAS
            + "\n\n" + pistaActual.getTexto());

        pistasRestantes--;
        actualizarLabel();

        pista.setDisable(true);
        veri.setDisable(false);
        pista.setText(pistasRestantes > 0
            ? "Pista (" + pistasRestantes + ")"
            : "Sin pistas");
    }

    /**
     * Muestra un diálogo de confirmación cuando el jugador responde correctamente
     * todas las filas, permitiéndole seguir jugando, ver el puntaje o salir.
     *
     * @param puntosGanados puntos obtenidos en la ronda recién completada.
     */
    private void mostrarOpcionesAlGanar(int puntosGanados) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("🏆 ¡Ganaste!");
        alert.setHeaderText("🎉 ¡Bien hecho! ¡Han quedado todos bien!\n\n"
                + "⭐ Puntos ganados: +" + puntosGanados + "\n"
                + "🏆 Puntuación total: " + partida.getPuntuacion());
        alert.setContentText("¿Qué deseas hacer ahora?");

        ButtonType btnSeguir   = new ButtonType("🔄 Seguir jugando");
        ButtonType btnPuntaje  = new ButtonType("🏆 Puntaje");
        ButtonType btnSalir    = new ButtonType("🚪 Salir", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnSeguir, btnPuntaje, btnSalir);

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnSeguir) {
                partida.reiniciar();
                pistaActual.reiniciar();
                reiniciarEstado();
                generarNumerosAleatorios();
            } else if (respuesta == btnPuntaje) {
                try {
                    PuntajeController.puntajeActual = partida.getPuntuacion();
                    PuntajeController.nivelActual = "Fácil";
                    App.openWindow("puntaje", "🏆 Puntaje");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                tablenumbers.getScene().getWindow().hide();
            }
        });
    }

    /**
     * Detiene la partida cuando el jugador agota todos los intentos y pistas
     * sin resolver todas las filas. Deshabilita controles, muestra el puntaje
     * final y ofrece opciones para reiniciar, ver puntaje o salir.
     */
    private void terminarPartida() {
        partida.setActiva(false);
        veri.setDisable(true);
        pista.setDisable(true);
        pista.setText("Sin pistas");

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Partida finalizada");
        alert.setHeaderText("🔴 PARTIDA FINALIZADA");
        alert.setContentText("Se agotaron todos los intentos y las pistas.\n\n"
                + "🏆 Puntuación final: " + partida.getPuntuacion());

        ButtonType btnReiniciar = new ButtonType("🔄 Volver a jugar");
        ButtonType btnPuntaje   = new ButtonType("🏆 Puntaje");
        ButtonType btnSalir     = new ButtonType("🚪 Salir", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnReiniciar, btnPuntaje, btnSalir);

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnReiniciar) {
                partida.reiniciar();
                pistaActual.reiniciar();
                reiniciarEstado();
                generarNumerosAleatorios();
            } else if (respuesta == btnPuntaje) {
                try {
                    PuntajeController.puntajeActual = partida.getPuntuacion();
                    PuntajeController.nivelActual = "Fácil";
                    App.openWindow("puntaje", "🏆 Puntaje");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                tablenumbers.getScene().getWindow().hide();
            }
        });
    }

    /**
     * Manejador del botón <em>Reiniciar</em>.
     *
     * <p>Reinicia la partida y la pista actuales, restablece todos los contadores
     * y genera una nueva serie de números aleatorios.</p>
     *
     * @param event evento de acción disparado por el botón.
     */
    @FXML
    private void buttonreiniciar(ActionEvent event) {
        partida.reiniciar();
        pistaActual.reiniciar();
        reiniciarEstado();
        generarNumerosAleatorios();
    }

    /**
     * Carga el jugador activo desde {@link IngresoController#sesionActual}.
     * Si no existe una sesión activa, intenta recuperar al jugador con apodo
     * {@code "jugador1"} del repositorio; si tampoco existe, lo crea.
     */
    private void cargarJugador() {
        Sesion sesion = IngresoController.sesionActual;
        if (sesion != null && sesion.isActiva() && IngresoController.jugadorSesion != null) {
            jugadorActual = IngresoController.jugadorSesion;
        } else {
            try {
                jugadorActual = daoJugador.readone("jugador1");
                if (jugadorActual == null) {
                    jugadorActual = new Jugador();
                    jugadorActual.setApodo("jugador1");
                    daoJugador.create(jugadorActual);
                }
            } catch (Exception e) {
                mostrarAlerta(AlertType.ERROR, "Error al cargar jugador: " + e.getMessage());
            }
        }
    }

    /**
     * Manejador de respuesta reservado para uso futuro; actualmente sin implementación.
     *
     * @param event evento de acción disparado por el control asociado.
     */
    @FXML
    private void hadlerespuesta(ActionEvent event) {}

    /**
     * Muestra un cuadro de diálogo genérico con el mensaje indicado.
     *
     * @param tipo    tipo de alerta ({@link AlertType#ERROR}, {@link AlertType#INFORMATION}, etc.).
     * @param mensaje texto a mostrar en el cuerpo del diálogo.
     */
    private void mostrarAlerta(AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Resultado");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}