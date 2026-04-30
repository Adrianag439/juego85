package controlador;

import con.mijuego.Resultado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import modelo.Jugador;
import modelo.Partida;
import servicios.JugadorService;

/**
 * Controlador del Nivel Difícil del juego Guess My Rule.
 * <p>
 * El jugador debe descubrir la regla matemática avanzada:
 * {@code resultado = (n!/2) + (n!/(n+1))}.
 * </p>
 *
 * <p><b>Características del nivel:</b></p>
 * <ul>
 *   <li>Sin pistas disponibles.</li>
 *   <li>Solo 3 vidas/intentos por partida.</li>
 *   <li>Cronómetro de 3 minutos implementado con {@link Timeline} de JavaFX.</li>
 *   <li>El cronómetro cambia a naranja al llegar a 30 s y a rojo a 10 s.</li>
 * </ul>
 *
 * <p><b>FXML asociado:</b> {@code /fx/dificil.fxml}</p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class DificilController implements Initializable {

    /** Botón para verificar la respuesta del jugador. */
    @FXML private Button btn_verificar;

    /** Botón para reiniciar la partida completa. */
    @FXML private Button reinici;

    /** Etiqueta del cronómetro en formato MM:SS (cambia de color según el tiempo). */
    @FXML private Label lb_cronometro;

    /** Etiqueta que muestra las vidas/intentos restantes. */
    @FXML private Label lb_intentos;

    /** Campo de texto donde el jugador ingresa el primer número. */
    @FXML private TextField text_dificil;

    /** Tabla que muestra el historial de números y resultados. */
    @FXML private TableView<Resultado> tablenumbers;

    /** Lista observable enlazada al TableView. */
    private ObservableList<Resultado> listaResultados;

    /** Servicio DAO para cargar y guardar datos del jugador. */
    private JugadorService daoJugador;

    /** Jugador actual cargado desde la base de datos. */
    private Jugador jugadorActual;

    /** Estado y lógica de la partida actual. */
    private Partida partida;

    /**
     * Timeline de JavaFX que dispara un {@link KeyFrame} cada segundo
     * para actualizar y verificar el cronómetro.
     */
    private Timeline timeline;

    /**
     * Inicializa el controlador al cargar el FXML.
     * <p>
     * Configura las columnas editables del TableView, inicializa la Partida
     * con 3 vidas, carga el jugador desde la BD y arranca el cronómetro.
     * </p>
     *
     * @param url ubicación del FXML (no utilizada)
     * @param rb  paquete de recursos (no utilizado)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        daoJugador = new JugadorService();

        // Configura la partida con 3 vidas (dificultad alta)
        partida = new Partida();
        partida.setVidasRestantes(3);
        partida.setActiva(true);

        listaResultados = FXCollections.observableArrayList();
        tablenumbers.setEditable(true);

        // ── Columna #INGRESADO ──────────────────────────────
        // Muestra el número como referencia o como TextField editable
        TableColumn<Resultado, Integer> colNumero = new TableColumn<>("#INGRESADO");
        colNumero.setPrefWidth(140);
        colNumero.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getNumero()));
        colNumero.setCellFactory(col -> new TableCell<Resultado, Integer>() {
            private final TextField tf = new TextField();
            {
                // Sincroniza el valor del TextField con el modelo de datos
                tf.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (getIndex() >= 0 && getIndex() < listaResultados.size()) {
                        try {
                            int val = Integer.parseInt(newVal.trim());
                            listaResultados.get(getIndex()).setNumero(val);
                        } catch (NumberFormatException ex) {}
                    }
                });
            }
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= listaResultados.size()) {
                    setGraphic(null); setText(null); return;
                }
                Resultado r = listaResultados.get(getIndex());
                if (r.isEditable()) {
                    tf.setText(item == null || item == 0 ? "" : String.valueOf(item));
                    setGraphic(tf); setText(null);
                } else {
                    setGraphic(null);
                    setText(item == null || item == 0 ? "" : String.valueOf(item));
                }
            }
        });

        // ── Columna RESULTADO ───────────────────────────────
        TableColumn<Resultado, Double> colResultado = new TableColumn<>("RESULTADO");
        colResultado.setPrefWidth(160);
        colResultado.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getResultado()));
        colResultado.setCellFactory(col -> new TableCell<Resultado, Double>() {
            private final TextField tf = new TextField();
            {
                tf.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (getIndex() >= 0 && getIndex() < listaResultados.size()) {
                        try {
                            double val = Double.parseDouble(newVal.trim());
                            listaResultados.get(getIndex()).setResultado(val);
                        } catch (NumberFormatException ex) {}
                    }
                });
            }
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= listaResultados.size()) {
                    setGraphic(null); setText(null); return;
                }
                Resultado r = listaResultados.get(getIndex());
                if (r.isEditable()) {
                    tf.setText(item == null || item == 0.0 ? "" : String.valueOf(item));
                    setGraphic(tf); setText(null);
                } else {
                    setGraphic(null);
                    setText(item == null || item == 0.0 ? "" : String.valueOf(item));
                }
            }
        });

        tablenumbers.getColumns().setAll(colNumero, colResultado);
        tablenumbers.setItems(listaResultados);
        tablenumbers.setDisable(true);
        btn_verificar.setDisable(true);
        lb_intentos.setText("INTENTOS: 3");

        // Permite ingresar el número presionando ENTER
        text_dificil.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                ingresarPrimerNumero();
            }
        });

        // Carga el jugador desde la BD (o crea uno por defecto)
        try {
            jugadorActual = daoJugador.readone("jugador1");
            if (jugadorActual == null) {
                jugadorActual = new Jugador();
                jugadorActual.setApodo("jugador1");
                daoJugador.create(jugadorActual);
            }
        } catch (Exception e) {
            mostrarAlerta("Error al cargar jugador: " + e.getMessage());
        }

        // Inicia el cronómetro de 3 minutos
        iniciarReloj();
    }

    /**
     * Procesa el número ingresado en el TextField con la regla del Nivel Difícil.
     * <p>
     * Calcula el resultado usando {@code (n!/2) + (n!/(n+1))} y agrega
     * dos filas a la tabla: una de referencia bloqueada y una editable
     * para que el jugador adivine el resultado de otro número.
     * </p>
     * Solo actúa si la partida está activa y el número es positivo.
     */
    private void ingresarPrimerNumero() {
        if (!partida.isActiva()) return;

        String input = text_dificil.getText().trim();
        if (input.isEmpty()) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 0) return;

            // Calcula usando la fórmula del nivel difícil
            double resultadoCalculado = partida.calcularResultadoDificil(num);

            // Fila bloqueada: referencia visible para el jugador
            listaResultados.add(new Resultado(num, resultadoCalculado, false));
            // Fila editable: el jugador escribe su respuesta aquí
            listaResultados.add(new Resultado(0, 0.0, true));

            tablenumbers.refresh();
            tablenumbers.setDisable(false);
            btn_verificar.setDisable(false);
            text_dificil.setDisable(true);
            text_dificil.clear();

        } catch (NumberFormatException e) {
            // Entrada no numérica; se ignora silenciosamente
        }
    }

    /**
     * Delegado del evento de acción del TextField (botón del FXML).
     * Llama a {@link #ingresarPrimerNumero()}.
     *
     * @param event evento de acción
     */
    @FXML
    private void hadleingreso(ActionEvent event) {
        ingresarPrimerNumero();
    }

    /**
     * Delegado del evento de acción del TextField (onAction en FXML).
     * Llama a {@link #ingresarPrimerNumero()}.
     *
     * @param event evento de acción
     */
    @FXML
    private void handleIngreso(ActionEvent event) {
        ingresarPrimerNumero();
    }

    /**
     * Verifica la respuesta del jugador en la fila editable del TableView.
     * <p>
     * Si es correcta: bloquea la fila, incrementa la puntuación y
     * añade una nueva fila editable. Si es incorrecta: descuenta una vida;
     * al llegar a 0 detiene el cronómetro y termina la partida.
     * </p>
     *
     * @param event evento de acción del botón VERIFICAR
     */
    @FXML
    private void hadlebutton(ActionEvent event) {
        if (!partida.isActiva()) return;
        if (listaResultados.isEmpty()) return;

        // Busca la fila editable activa
        Resultado filaEditable = null;
        for (Resultado r : listaResultados) {
            if (r.isEditable()) filaEditable = r;
        }

        if (filaEditable == null) return;

        int numUsuario = filaEditable.getNumero();
        if (numUsuario <= 0) return;

        double respuestaUsuario = filaEditable.getResultado();
        // Calcula el resultado correcto para el número ingresado por el jugador
        double correcto = partida.calcularResultadoDificil(numUsuario);

        if (respuestaUsuario == correcto) {
            // Respuesta correcta: bloquea y añade nueva fila
            filaEditable.setEditable(false);
            for (Resultado r : listaResultados) r.setEditable(false);
            tablenumbers.refresh();
            partida.setPuntuacion(partida.getPuntuacion() + 1);

            mostrarAlerta("✅ CORRECTO");

            listaResultados.add(new Resultado(0, 0.0, true));
            tablenumbers.refresh();
            btn_verificar.setDisable(false);

        } else {
            // Respuesta incorrecta: descuenta una vida
            partida.setVidasRestantes(partida.getVidasRestantes() - 1);
            lb_intentos.setText("INTENTOS: " + partida.getVidasRestantes());

            mostrarAlerta("❌ INCORRECTO");

            if (partida.sinVidas()) {
                // Sin vidas: detiene el cronómetro y bloquea la interfaz
                timeline.stop();
                btn_verificar.setDisable(true);
                text_dificil.setDisable(true);
                tablenumbers.setDisable(true);
                partida.setActiva(false);
                mostrarAlerta("Juego terminado.");
            }
        }
    }

    /**
     * Crea e inicia el cronómetro del Nivel Difícil usando un {@link Timeline}.
     * <p>
     * Cada segundo:
     * </p>
     * <ul>
     *   <li>Descuenta un segundo de {@code Partida.segundosRestantes}.</li>
     *   <li>Actualiza el Label con el formato {@code MM:SS}.</li>
     *   <li>Cambia el color del label (amarillo → naranja → rojo).</li>
     *   <li>Al llegar a 0: detiene el juego y muestra alerta con {@link Platform#runLater}.</li>
     * </ul>
     */
    private void iniciarReloj() {
        lb_cronometro.setText("TIEMPO: 03:00");
        lb_cronometro.setStyle("-fx-text-fill: #ffea00; -fx-font-size: 14px; -fx-font-weight: bold;");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            partida.descontarSegundo();
            int min = partida.getSegundosRestantes() / 60;
            int seg = partida.getSegundosRestantes() % 60;
            lb_cronometro.setText("TIEMPO: " + String.format("%02d:%02d", min, seg));

            // Cambia a naranja cuando quedan 30 segundos
            if (partida.tiempoEnAdvertencia())
                lb_cronometro.setStyle("-fx-text-fill: orange; -fx-font-size: 14px; -fx-font-weight: bold;");

            // Cambia a rojo cuando quedan 10 segundos
            if (partida.tiempoEnPeligro())
                lb_cronometro.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");

            if (partida.tiempoAgotado()) {
                // Detiene la partida y muestra alerta en el hilo de la UI
                timeline.stop();
                btn_verificar.setDisable(true);
                text_dificil.setDisable(true);
                tablenumbers.setDisable(true);
                partida.setActiva(false);
                lb_cronometro.setText("TIEMPO: 00:00");

                // Platform.runLater garantiza que la alerta se muestre en el hilo JavaFX
                Platform.runLater(() -> {
                    Alert alertaTiempo = new Alert(AlertType.WARNING);
                    alertaTiempo.setTitle("Tiempo Agotado");
                    alertaTiempo.setHeaderText("SE ACABO EL TIEMPO!");
                    alertaTiempo.setContentText("El tiempo llego a 00:00. Juego terminado.");
                    alertaTiempo.showAndWait();
                });
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Muestra un diálogo de información al usuario.
     *
     * @param mensaje texto a mostrar en el cuerpo del diálogo
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Resultado");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Reinicia completamente la partida del Nivel Difícil.
     * <p>
     * Detiene el cronómetro actual, limpia la tabla, resetea las vidas
     * a 3 y arranca un nuevo cronómetro de 3 minutos.
     * </p>
     *
     * @param event evento de acción del botón REINICIAR
     */
    @FXML
    private void hadledificil(ActionEvent event) {
        partida.reiniciar();
        partida.setVidasRestantes(3);
        listaResultados.clear();
        tablenumbers.setDisable(true);
        tablenumbers.refresh();
        text_dificil.clear();
        text_dificil.setDisable(false);
        text_dificil.requestFocus();
        btn_verificar.setDisable(true);
        lb_intentos.setText("INTENTOS: 3");
        lb_cronometro.setStyle("-fx-text-fill: #ffea00; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Detiene el Timeline anterior antes de crear uno nuevo
        if (timeline != null) timeline.stop();
        iniciarReloj();
    }
}
