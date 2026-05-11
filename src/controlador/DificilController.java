package controlador;

import con.mijuego.Resultado;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import modelo.Jugador;
import modelo.Niveles;
import modelo.Partida;
import modelo.Sesion;
import servicios.JugadorService;
import java.io.IOException;
import vista.App;

/**
 * Controlador JavaFX para el nivel <b>Difícil</b> del juego de factoriales por dígitos.
 *
 * <p>El jugador debe calcular la suma de los factoriales de cada dígito de números
 * de dos cifras (entre {@value #NUMERO_MIN} y {@value #NUMERO_MAX}).
 * Se presentan {@value #CANTIDAD_NUMEROS} números simultáneamente, con
 * {@value #TOTAL_INTENTOS} intentos disponibles y un temporizador de 3 minutos.</p>
 *
 * <p>Ejemplo: para el número {@code 23} → {@code 2! + 3! = 2 + 6 = 8}.</p>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 * @see PruebaController
 * @see PuntajeController
 */
public class DificilController implements Initializable {

    // -------------------------------------------------------------------------
    // Componentes FXML
    // -------------------------------------------------------------------------

    /** Botón que dispara la verificación de respuestas ingresadas. */
    @FXML private Button btn_verificar;

    /** Botón que reinicia la partida actual sin cambiar el puntaje acumulado. */
    @FXML private Button reinici;

    /** Etiqueta que muestra el tiempo restante en formato {@code MM:SS}. */
    @FXML private Label lb_cronometro;

    /** Etiqueta que muestra la cantidad de intentos restantes. */
    @FXML private Label lb_intentos;

    /**
     * Tabla principal del juego; cada fila contiene el número generado,
     * el campo editable para la respuesta y el estado (✅ / ❌).
     */
    @FXML private TableView<Resultado> tablenumbers;

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

    /** Configuración del nivel Difícil (id=2, temporizador activo). */
    private Niveles nivel;

    /** Animación de cuenta regresiva; {@code null} si aún no se ha iniciado. */
    private Timeline timeline;

    // -------------------------------------------------------------------------
    // Constantes del nivel
    // -------------------------------------------------------------------------

    /** Número de filas/números mostrados en la tabla por ronda. */
    private static final int CANTIDAD_NUMEROS = 8;

    /** Valor mínimo (inclusive) del rango de números generados. */
    private static final int NUMERO_MIN = 10;

    /** Valor máximo (inclusive) del rango de números generados. */
    private static final int NUMERO_MAX = 55;

    /** Total de intentos de verificación antes de terminar la partida. */
    private static final int TOTAL_INTENTOS = 3;

    /** Puntos otorgados por cada respuesta correcta en este nivel. */
    private static final int PUNTOS_ACIERTO = 25;

    /** Contador mutable de intentos que decrece con cada verificación fallida. */
    private int intentosRestantes;

    // -------------------------------------------------------------------------
    // Inicialización
    // -------------------------------------------------------------------------

    /**
     * Método de inicialización invocado automáticamente por el {@code FXMLLoader}
     * tras cargar el archivo FXML asociado.
     *
     * <p>Configura el servicio de datos, la partida, el nivel, construye las
     * columnas de la tabla, genera los números aleatorios, carga el jugador
     * desde la sesión activa y, si el nivel lo requiere, inicia el reloj.</p>
     *
     * @param url            ubicación del archivo FXML (puede ser {@code null}).
     * @param rb             paquete de recursos de internacionalización (puede ser {@code null}).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        daoJugador = new JugadorService();
        partida    = new Partida();
        nivel      = new Niveles(2, "Difícil", TOTAL_INTENTOS, PUNTOS_ACIERTO, true) {};

        partida.setActiva(true);

        listaResultados = FXCollections.observableArrayList();
        tablenumbers.setEditable(true);

        construirColumnas();
        tablenumbers.setItems(listaResultados);

        reiniciarEstado();
        generarNumerosAleatorios();
        cargarJugador();

        if (nivel.isTieneTemporizador()) {
            iniciarReloj();
        }
    }

    // -------------------------------------------------------------------------
    // Métodos privados de configuración
    // -------------------------------------------------------------------------

    /**
     * Restablece los contadores de intento y el estado de la partida al inicio
     * o al reiniciar, habilitando el botón de verificación.
     */
    private void reiniciarEstado() {
        intentosRestantes = TOTAL_INTENTOS;
        partida.setActiva(true);
        partida.setVidasRestantes(intentosRestantes);
        btn_verificar.setDisable(false);
        lb_intentos.setText("💀 INTENTOS: " + intentosRestantes);
    }

    /**
     * Construye y registra las tres columnas de {@link #tablenumbers}:
     * <ol>
     *   <li><b># INGRESADO</b> – número generado (solo lectura).</li>
     *   <li><b>RESULTADO</b>   – campo editable donde el jugador escribe su respuesta.</li>
     *   <li><b>ESTADO</b>      – icono ✅ o ❌ tras verificar.</li>
     * </ol>
     */
    private void construirColumnas() {
        tablenumbers.getColumns().clear();

        TableColumn<Resultado, Integer> colNumero = new TableColumn<>("# INGRESADO");
        colNumero.setPrefWidth(180);
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
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Resultado r = listaResultados.get(idx);
                if (r.isEditable()) {
                    tf.setText(item == null || item == 0.0 ? "" : String.valueOf(item.longValue()));
                    setGraphic(tf);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item == null || item == 0.0 ? "—" : String.valueOf(item.longValue()));
                }
            }
        });

        TableColumn<Resultado, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setPrefWidth(180);
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
    // Lógica matemática
    // -------------------------------------------------------------------------

    /**
     * Calcula el factorial de un dígito individual (0–9).
     *
     * @param n dígito cuyo factorial se quiere calcular; debe estar en [0, 9].
     * @return  valor de {@code n!} como {@code long}.
     */
    private long factorialDigito(int n) {
        long resultado = 1;
        for (int i = 2; i <= n; i++) resultado *= i;
        return resultado;
    }

    /**
     * Calcula la suma de los factoriales de cada dígito del número dado.
     *
     * <p>Ejemplo: {@code calcularSumaFactorialesDigitos(23)} → {@code 2! + 3! = 8.0}</p>
     *
     * @param numero número entero positivo del que se extraen los dígitos.
     * @return       suma de los factoriales de sus dígitos como {@code double}.
     */
    private double calcularSumaFactorialesDigitos(int numero) {
        long suma = 0;
        String digitos = String.valueOf(numero);
        for (char c : digitos.toCharArray()) {
            int digito = Character.getNumericValue(c);
            suma += factorialDigito(digito);
        }
        return (double) suma;
    }

    // -------------------------------------------------------------------------
    // Manejadores de eventos FXML
    // -------------------------------------------------------------------------

    /**
     * Manejador del botón <em>Verificar</em>.
     *
     * <p>Recorre todas las filas editables, compara la respuesta del jugador con
     * {@link #calcularSumaFactorialesDigitos(int)}, actualiza el estado visual
     * (✅ / ❌) y descuenta un intento. Si todas son correctas muestra el diálogo
     * de victoria; si los intentos llegan a cero, invoca {@link #terminarPartida()}.</p>
     *
     * @param event evento de acción disparado por el botón.
     */
    @FXML
    private void hadlebutton(ActionEvent event) {
        if (!partida.isActiva() || listaResultados.isEmpty()) return;

        int correctos   = 0;
        int incorrectos = 0;

        for (Resultado fila : listaResultados) {
            if (!fila.isEditable()) {
                correctos++;
                continue;
            }

            double respuestaCorrecta = calcularSumaFactorialesDigitos(fila.getNumero());

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

        intentosRestantes--;
        partida.setVidasRestantes(intentosRestantes);
        lb_intentos.setText("💀 INTENTOS: " + intentosRestantes);

        if (incorrectos == 0) {
            if (timeline != null) timeline.stop();
            int puntosGanados = correctos * nivel.getPuntuacionBase();
            partida.setPuntuacion(partida.getPuntuacion() + puntosGanados);
            btn_verificar.setDisable(true);
            mostrarOpcionesAlGanar(puntosGanados);
            return;
        }

        if (intentosRestantes == 0) {
            terminarPartida();
        }
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
                reiniciarEstado();
                generarNumerosAleatorios();
                if (timeline != null) timeline.stop();
                if (nivel.isTieneTemporizador()) iniciarReloj();
            } else if (respuesta == btnPuntaje) {
                try {
                    PuntajeController.puntajeActual = partida.getPuntuacion();
                    PuntajeController.nivelActual = "Difícil";
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
     * Detiene la partida cuando el jugador agota todos los intentos sin resolver
     * todas las filas. Deshabilita controles, muestra el puntaje final y ofrece
     * opciones para reiniciar, ver puntaje o salir.
     */
    private void terminarPartida() {
        if (timeline != null) timeline.stop();
        partida.setActiva(false);
        btn_verificar.setDisable(true);

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Partida finalizada");
        alert.setHeaderText("🔴 PARTIDA FINALIZADA");
        alert.setContentText("Se agotaron todos los intentos.\n\n"
                + "🏆 Puntuación final: " + partida.getPuntuacion());

        ButtonType btnReiniciar = new ButtonType("🔄 Volver a jugar");
        ButtonType btnPuntaje   = new ButtonType("🏆 Puntaje");
        ButtonType btnSalir     = new ButtonType("🚪 Salir", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnReiniciar, btnPuntaje, btnSalir);

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnReiniciar) {
                partida.reiniciar();
                reiniciarEstado();
                generarNumerosAleatorios();
                if (nivel.isTieneTemporizador()) iniciarReloj();
            } else if (respuesta == btnPuntaje) {
                try {
                    PuntajeController.puntajeActual = partida.getPuntuacion();
                    PuntajeController.nivelActual = "Difícil";
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
     * Inicia (o reinicia) el temporizador de cuenta regresiva de 3 minutos (180 s).
     *
     * <p>Cada segundo actualiza {@link #lb_cronometro}. Cuando el tiempo llega a cero,
     * detiene la partida y muestra un diálogo de tiempo agotado en el hilo de la UI
     * mediante {@link Platform#runLater(Runnable)}.</p>
     */
    private void iniciarReloj() {
        partida.setSegundosRestantes(180);
        lb_cronometro.setText("⏱ TIEMPO: 03:00");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            partida.descontarSegundo();
            int min = partida.getSegundosRestantes() / 60;
            int seg = partida.getSegundosRestantes() % 60;
            lb_cronometro.setText("⏱ TIEMPO: " + String.format("%02d:%02d", min, seg));

            if (partida.tiempoAgotado()) {
                timeline.stop();
                btn_verificar.setDisable(true);
                partida.setActiva(false);
                lb_cronometro.setText("⏱ TIEMPO: 00:00");

                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Tiempo agotado");
                    alert.setHeaderText("⏰ ¡SE ACABÓ EL TIEMPO!");
                    alert.setContentText("Puntuación final: " + partida.getPuntuacion());

                    ButtonType btnReiniciar = new ButtonType("🔄 Volver a jugar");
                    ButtonType btnPuntaje   = new ButtonType("🏆 Puntaje");
                    ButtonType btnSalir     = new ButtonType("🚪 Salir", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(btnReiniciar, btnPuntaje, btnSalir);

                    alert.showAndWait().ifPresent(respuesta -> {
                        if (respuesta == btnReiniciar) {
                            partida.reiniciar();
                            reiniciarEstado();
                            generarNumerosAleatorios();
                            iniciarReloj();
                        } else if (respuesta == btnPuntaje) {
                            try {
                                PuntajeController.puntajeActual = partida.getPuntuacion();
                                PuntajeController.nivelActual = "Difícil";
                                App.openWindow("puntaje", "🏆 Puntaje");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            tablenumbers.getScene().getWindow().hide();
                        }
                    });
                });
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Manejador del botón <em>Reiniciar</em> en nivel Difícil.
     *
     * <p>Detiene el reloj, reinicia la partida y los contadores, y genera
     * una nueva serie de números aleatorios.</p>
     *
     * @param event evento de acción disparado por el botón.
     */
    @FXML
    private void hadledificil(ActionEvent event) {
        if (timeline != null) timeline.stop();
        partida.reiniciar();
        reiniciarEstado();
        generarNumerosAleatorios();
        if (nivel.isTieneTemporizador()) iniciarReloj();
    }

    // -------------------------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------------------------

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