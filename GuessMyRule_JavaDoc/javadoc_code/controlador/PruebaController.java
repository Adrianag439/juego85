package controlador;

import con.mijuego.Resultado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import servicios.JugadorService;
import modelo.Jugador;
import modelo.Partida;
import modelo.Pista;

/**
 * Controlador del Nivel Fácil del juego Guess My Rule.
 * <p>
 * El jugador debe descubrir la regla matemática: {@code resultado = n! / 2}.
 * Dispone de 5 intentos por número y hasta 3 pistas progresivas
 * (sin límite de tiempo).
 * </p>
 *
 * <p><b>Flujo de juego:</b></p>
 * <ol>
 *   <li>El jugador ingresa un número en el {@code TextField}.</li>
 *   <li>La tabla muestra el número y su resultado correcto como referencia.</li>
 *   <li>Se añade una fila editable para que el jugador adivine el resultado de otro número.</li>
 *   <li>Si acierta, suma puntos y continúa. Si falla, pierde un intento.</li>
 *   <li>A los 0 intentos se habilitan las pistas (si quedan disponibles).</li>
 * </ol>
 *
 * <p><b>FXML asociado:</b> {@code /fx/facil.fxml}</p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class PruebaController implements Initializable {

    /** Etiqueta que muestra los intentos restantes para el número actual. */
    @FXML private Label lb_intentos;

    /** Campo de texto donde el jugador ingresa el primer número. */
    @FXML private TextField intenoone;

    /** Tabla que muestra el historial de números y resultados. */
    @FXML private TableView<Resultado> tablenumbers;

    /** Botón para verificar la respuesta ingresada en la fila editable. */
    @FXML private Button veri;

    /** Botón para reiniciar la partida completa. */
    @FXML private Button reini;

    /** Botón para solicitar una pista (se habilita a los 0 intentos). */
    @FXML private Button pista;

    /** Lista observable enlazada al TableView para mostrar resultados. */
    private ObservableList<Resultado> listaResultados;

    /** Servicio DAO para cargar y guardar datos del jugador. */
    private JugadorService daoJugador;

    /** Jugador actual cargado desde la base de datos. */
    private Jugador jugadorActual;

    /** Objeto que gestiona el estado y la lógica de la partida. */
    private Partida partida;

    /** Objeto que gestiona las pistas disponibles para la partida. */
    private Pista pistaActual;

    /** Contador de intentos restantes para el número actual (inicia en 5). */
    private int intentosRestantes = 5;

    /**
     * Indica si el modo de pista está activo.
     * Se activa cuando {@code intentosRestantes} llega a 0 y quedan pistas.
     */
    private boolean pistaHabilitada = false;

    /**
     * Inicializa el controlador al cargar el FXML.
     * <p>
     * Configura las columnas editables del TableView, el listener
     * de teclado (ENTER en el TextField), carga el jugador desde
     * la BD e inicializa la Partida y la Pista.
     * </p>
     *
     * @param url ubicación del FXML (no utilizada)
     * @param rb  paquete de recursos (no utilizado)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        daoJugador = new JugadorService();

        // Configuración inicial de la partida con 5 vidas
        partida = new Partida();
        partida.setVidasRestantes(5);
        partida.setActiva(true);

        // Pista deshabilitada al inicio; se habilita tras agotar intentos
        pistaActual = new Pista();
        pista.setDisable(true);
        pista.setText("Pista (3)");
        pistaHabilitada = false;

        listaResultados = FXCollections.observableArrayList();
        tablenumbers.setEditable(true);

        // ── Columna #INGRESADO ──────────────────────────────
        // Muestra el número como referencia (bloqueado) o como TextField editable
        TableColumn<Resultado, Integer> colNumero = new TableColumn<>("#INGRESADO");
        colNumero.setPrefWidth(140);
        colNumero.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getNumero()));
        colNumero.setCellFactory(col -> new TableCell<Resultado, Integer>() {
            private final TextField tf = new TextField();
            {
                // Actualiza el modelo cuando el jugador edita el número
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
        // Muestra el resultado como referencia o como TextField editable
        TableColumn<Resultado, Double> colResultado = new TableColumn<>("RESULTADO");
        colResultado.setPrefWidth(160);
        colResultado.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getResultado()));
        colResultado.setCellFactory(col -> new TableCell<Resultado, Double>() {
            private final TextField tf = new TextField();
            {
                // Actualiza el modelo cuando el jugador edita el resultado
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
        veri.setDisable(true);
        lb_intentos.setText("INTENTOS: " + intentosRestantes);

        // Permite ingresar el número presionando ENTER en el TextField
        intenoone.setOnKeyPressed((KeyEvent event) -> {
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
            mostrarAlerta(AlertType.ERROR, "Error al cargar jugador: " + e.getMessage());
        }
    }

    /**
     * Procesa el número ingresado en el TextField y lo agrega a la tabla.
     * <p>
     * Calcula el resultado correcto con la regla {@code n!/2} y agrega
     * dos filas: una de referencia bloqueada y una editable para la respuesta.
     * Resetea los intentos a 5 y deshabilita las pistas para el nuevo número.
     * </p>
     * Solo actúa si la partida está activa y el número es positivo.
     */
    private void ingresarPrimerNumero() {
        if (!partida.isActiva()) return;

        String input = intenoone.getText().trim();
        if (input.isEmpty()) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 0) return;

            // Calcula el resultado correcto para mostrarlo como referencia
            double resultadoCalculado = partida.calcularResultadoCorrecto(num);

            // Fila bloqueada: muestra el número y su resultado correcto
            listaResultados.add(new Resultado(num, resultadoCalculado, false));
            // Fila editable: el jugador ingresa número y resultado a adivinar
            listaResultados.add(new Resultado(0, 0.0, true));

            tablenumbers.refresh();
            tablenumbers.setDisable(false);
            veri.setDisable(false);
            intenoone.setDisable(true);
            intenoone.clear();

            // Resetea intentos para el nuevo número
            intentosRestantes = 5;
            partida.setVidasRestantes(5);
            lb_intentos.setText("INTENTOS: " + intentosRestantes);
            pistaHabilitada = false;
            pista.setDisable(true);
            pista.setText("Pista (" + pistaActual.getPistasRestantes() + ")");

        } catch (NumberFormatException e) {
            // El usuario ingresó texto no numérico; se ignora silenciosamente
        }
    }

    /**
     * Verifica la respuesta ingresada por el jugador en la fila editable.
     * <p>
     * Si es correcta: bloquea la fila, añade una nueva fila editable
     * y resetea los intentos. Si es incorrecta: descuenta un intento
     * y, al llegar a 0, habilita las pistas (si quedan disponibles)
     * o termina el juego.
     * </p>
     *
     * @param event evento de acción del botón VERIFICAR
     */
    @FXML
    private void button_veri(ActionEvent event) {
        if (listaResultados.isEmpty()) return;
        if (!partida.isActiva()) return;

        // Busca la última fila editable (donde el jugador escribió su respuesta)
        Resultado filaEditable = null;
        for (Resultado r : listaResultados) {
            if (r.isEditable()) filaEditable = r;
        }

        if (filaEditable == null) return;

        int numUsuario = filaEditable.getNumero();
        if (numUsuario <= 0) return;

        double respuestaUsuario = filaEditable.getResultado();
        boolean correcto = partida.verificarRespuesta(numUsuario, respuestaUsuario);

        if (correcto) {
            // Bloquea todas las filas y añade nueva fila editable para continuar
            filaEditable.setEditable(false);
            for (Resultado r : listaResultados) r.setEditable(false);
            tablenumbers.refresh();
            listaResultados.add(new Resultado(0, 0.0, true));
            tablenumbers.refresh();

            // Resetea intentos y pistas para el siguiente número
            intentosRestantes = 5;
            partida.setVidasRestantes(5);
            lb_intentos.setText("INTENTOS: " + intentosRestantes);
            pistaHabilitada = false;
            pista.setDisable(true);
            pista.setText("Pista (" + pistaActual.getPistasRestantes() + ")");

            mostrarAlerta(AlertType.INFORMATION, "✅ CORRECTO");

        } else {
            if (pistaHabilitada) {
                // En modo pista: muestra error y re-habilita el botón si quedan pistas
                mostrarAlerta(AlertType.WARNING, "❌ INCORRECTO");

                if (pistaActual.getPistasRestantes() > 0) {
                    pista.setDisable(false);
                } else {
                    // Sin pistas: termina el juego
                    partida.setActiva(false);
                    veri.setDisable(true);
                    pista.setDisable(true);
                    mostrarAlerta(AlertType.ERROR, "JUEGO TERMINADO");
                }
            } else {
                // Decrementa un intento y verifica si se debe activar modo pista
                intentosRestantes--;
                partida.setVidasRestantes(intentosRestantes);
                lb_intentos.setText("INTENTOS: " + intentosRestantes);
                mostrarAlerta(AlertType.WARNING, "❌ INCORRECTO");

                if (intentosRestantes == 0 && pistaActual.getPistasRestantes() > 0) {
                    // Sin intentos pero con pistas disponibles: activa modo pista
                    pistaHabilitada = true;
                    pista.setDisable(false);
                    mostrarAlerta(AlertType.INFORMATION, "¡Sin intentos! Usa una pista.");
                }

                if (intentosRestantes == 0 && pistaActual.getPistasRestantes() == 0) {
                    // Sin intentos y sin pistas: fin de juego
                    partida.setActiva(false);
                    veri.setDisable(true);
                    pista.setDisable(true);
                    mostrarAlerta(AlertType.ERROR, "JUEGO TERMINADO");
                }
            }
        }
    }

    /**
     * Maneja el evento del botón PISTA.
     * <p>
     * Genera y muestra la pista correspondiente al último número ingresado.
     * Deshabilita el botón tras el uso. Si no quedan pistas y el jugador
     * no tiene intentos, termina la partida.
     * </p>
     *
     * @param event evento de acción del botón PISTA
     */
    @FXML
    private void hadel_pista(ActionEvent event) {
        if (!partida.isActiva()) return;

        // Verifica que queden pistas disponibles
        if (pistaActual.getPistasRestantes() <= 0) {
            pista.setDisable(true);
            pista.setText("Sin pistas");
            if (intentosRestantes == 0) {
                partida.setActiva(false);
                veri.setDisable(true);
                mostrarAlerta(AlertType.ERROR, "JUEGO TERMINADO");
            }
            return;
        }

        // Obtiene el último número de referencia de la tabla (fila bloqueada)
        int ultimoNumero = 0;
        for (Resultado r : listaResultados) {
            if (!r.isEditable() && r.getNumero() > 0) {
                ultimoNumero = r.getNumero();
            }
        }

        if (ultimoNumero > 0) {
            // Genera la pista basada en el factorial del número de referencia
            long factorial = partida.calcularFactorial(ultimoNumero);
            pistaActual.generarPista(ultimoNumero, factorial);
            mostrarAlerta(AlertType.INFORMATION, pistaActual.getTexto());

            // Deshabilita el botón tras usar la pista
            pista.setDisable(true);

            if (pistaActual.getPistasRestantes() > 0) {
                pista.setText("Pista (" + pistaActual.getPistasRestantes() + ")");
            } else {
                pista.setText("Sin pistas");
                if (intentosRestantes == 0) {
                    partida.setActiva(false);
                    veri.setDisable(true);
                    mostrarAlerta(AlertType.ERROR, "JUEGO TERMINADO");
                }
            }
        } else {
            mostrarAlerta(AlertType.INFORMATION, "Primero ingresa un número.");
        }
    }

    /**
     * Reinicia la partida completa a su estado inicial.
     * <p>
     * Limpia la tabla, resetea la Partida y la Pista, restaura
     * los intentos a 5 y habilita el TextField para un nuevo número.
     * </p>
     *
     * @param event evento de acción del botón REINICIAR
     */
    @FXML
    private void buttonreiniciar(ActionEvent event) {
        partida.reiniciar();
        pistaActual.reiniciar();
        listaResultados.clear();
        intentosRestantes = 5;
        pistaHabilitada = false;
        partida.setVidasRestantes(5);
        partida.setActiva(true);
        tablenumbers.setDisable(true);
        tablenumbers.refresh();
        intenoone.setDisable(false);
        intenoone.clear();
        intenoone.requestFocus();
        veri.setDisable(true);
        pista.setDisable(true);
        pista.setText("Pista (3)");
        lb_intentos.setText("INTENTOS: " + intentosRestantes);
    }

    /**
     * Delegado del evento de acción del TextField (tecla ENTER o botón asociado).
     * Llama a {@link #ingresarPrimerNumero()}.
     *
     * @param event evento de acción
     */
    @FXML
    private void hadlerespuesta(ActionEvent event) {
        ingresarPrimerNumero();
    }

    /**
     * Muestra un diálogo de alerta al usuario.
     *
     * @param tipo    tipo de alerta (INFORMATION, WARNING, ERROR)
     * @param mensaje texto a mostrar en el cuerpo del diálogo
     */
    private void mostrarAlerta(AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Resultado");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
