package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import modelo.Jugador;
import modelo.Niveles;
import modelo.Partida;
import modelo.Sesion;
import vista.App;

/**
 * Controlador de la pantalla de descubrimiento de la regla secreta en el nivel Fácil.
 * <p>
 * Permite al jugador ingresar números enteros para observar la transformación
 * aplicada por la regla del juego antes de pasar a la fase de verificación.
 * Utiliza {@link Partida} para calcular los resultados y {@link Niveles} para
 * configurar los parámetros del nivel Fácil.
 * </p>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 */
public class AdivinarController implements Initializable {

    /** Campo de texto donde el jugador ingresa el número a evaluar. */
    @FXML private TextField txtNumero;

    /** Tabla que muestra los pares entrada/salida registrados durante la sesión. */
    @FXML private TableView<FilaRegla> tablaRegla;

    /** Columna que muestra el número ingresado por el jugador. */
    @FXML private TableColumn<FilaRegla, String> colEntrada;

    /** Columna que muestra el resultado calculado según la regla secreta. */
    @FXML private TableColumn<FilaRegla, String> colSalida;

    /** Etiqueta de bienvenida que muestra el apodo del jugador y el nivel activo. */
    @FXML private Label lblBienvenida; // Agrega este Label en tu FXML

    /** Lista observable que respalda la {@link #tablaRegla}. */
    private ObservableList<FilaRegla> listaFilas;

    /** Instancia de partida usada para calcular los resultados correctos. */
    private Partida partida;

    /** Nivel fácil con sus datos: id=1, 5 vidas, 10 pts base, sin temporizador. */
    // Nivel fácil con sus datos
    private final Niveles nivelFacil = new Niveles(1, "Fácil", 5, 10, false) {};

    /**
     * Modelo de una fila en la tabla de descubrimiento.
     * <p>
     * Cada fila almacena un número de entrada y su resultado calculado
     * como propiedades JavaFX observables.
     * </p>
     */
    public static class FilaRegla {

        /** Propiedad que representa el número de entrada como cadena de texto. */
        private final SimpleStringProperty entrada;

        /** Propiedad que representa el resultado calculado como cadena de texto. */
        private final SimpleStringProperty salida;

        /**
         * Construye una nueva fila con el número ingresado y su resultado.
         *
         * @param numero   número entero ingresado por el jugador
         * @param resultado resultado calculado por la regla secreta
         */
        public FilaRegla(int numero, long resultado) {
            this.entrada = new SimpleStringProperty(String.valueOf(numero));
            this.salida  = new SimpleStringProperty(String.valueOf(resultado));
        }

        /**
         * Retorna la propiedad observable de la entrada.
         *
         * @return propiedad {@link SimpleStringProperty} de la entrada
         */
        public SimpleStringProperty entradaProperty() { return entrada; }

        /**
         * Retorna la propiedad observable de la salida.
         *
         * @return propiedad {@link SimpleStringProperty} de la salida
         */
        public SimpleStringProperty salidaProperty()  { return salida; }

        /**
         * Retorna el valor actual de la entrada como cadena.
         *
         * @return número ingresado en formato {@link String}
         */
        public String getEntrada() { return entrada.get(); }

        /**
         * Retorna el valor actual de la salida como cadena.
         *
         * @return resultado calculado en formato {@link String}
         */
        public String getSalida()  { return salida.get(); }
    }

    /**
     * Inicializa el controlador cuando se carga la vista FXML.
     * <p>
     * Configura la tabla de reglas, muestra un saludo personalizado si hay
     * una sesión activa y registra el listener de tecla ENTER en el campo
     * de texto.
     * </p>
     *
     * @param url            URL de localización del archivo FXML (no utilizado directamente)
     * @param rb             paquete de recursos de internacionalización (no utilizado directamente)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partida    = new Partida();
        listaFilas = FXCollections.observableArrayList();

        // ── Usar Jugador y Sesion para saludo ─────────────────
        Jugador jugador = IngresoController.jugadorSesion;
        Sesion  sesion  = IngresoController.sesionActual;

        if (lblBienvenida != null && sesion != null && sesion.isActiva() && jugador != null) {
            lblBienvenida.setText("¡Hola, " + jugador.getApodo()
                + "! Nivel: " + nivelFacil.getNombre());
        }

        colEntrada.setCellValueFactory(data -> data.getValue().entradaProperty());
        colEntrada.setPrefWidth(140);

        colSalida.setCellValueFactory(data -> data.getValue().salidaProperty());
        colSalida.setPrefWidth(200);
        colSalida.setCellFactory(col -> new TableCell<FilaRegla, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                setStyle("-fx-text-fill: #FFFFFF;");
            }
        });

        tablaRegla.setItems(listaFilas);
        tablaRegla.setEditable(false);

        txtNumero.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) agregarNumero();
        });
    }

    /**
     * Valida el número ingresado por el jugador, calcula el resultado mediante
     * {@link Partida#calcularResultadoCorrecto(int)} y agrega la fila a la tabla.
     * <p>
     * Muestra alertas de validación cuando el campo está vacío, contiene
     * caracteres no numéricos, el número es menor que 1 o excede el rango
     * de {@code int}.
     * </p>
     */
    private void agregarNumero() {
        String input = txtNumero.getText().trim();

        if (input.isEmpty()) {
            mostrarAlerta("Campo vacío", "Por favor, ingrese un número.");
            return;
        }
        if (!input.matches("\\d+")) {
            mostrarAlerta("Entrada inválida", "Solo se permiten números enteros.");
            txtNumero.clear();
            return;
        }

        try {
            int num = Integer.parseInt(input);
            if (num < 1) {
                mostrarAlerta("Número inválido", "El número debe ser mayor que 0.");
                txtNumero.clear();
                return;
            }

            // ── Usar Partida para calcular el resultado ───────
            long resultado = (long) partida.calcularResultadoCorrecto(num);
            listaFilas.add(new FilaRegla(num, resultado));
            tablaRegla.scrollTo(listaFilas.size() - 1);
            txtNumero.clear();

        } catch (NumberFormatException e) {
            mostrarAlerta("Número demasiado grande", "Ingrese un número más pequeño.");
            txtNumero.clear();
        }
    }

    /**
     * Muestra un diálogo de advertencia con el título y mensaje especificados.
     *
     * @param titulo  texto mostrado como encabezado de la alerta
     * @param mensaje texto mostrado como contenido de la alerta
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Validación");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Maneja el evento del botón "ESTOY LISTO".
     * <p>
     * Navega a la pantalla de juego del nivel Fácil ({@code facil.fxml}).
     * </p>
     *
     * @param event evento de acción disparado por el botón
     */
    @FXML
    private void handleEstoyListo(ActionEvent event) {
        try {
            App.setRoot("facil");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "REGLAS".
     * <p>
     * Abre una ventana secundaria con las reglas del juego ({@code reglas.fxml}).
     * </p>
     *
     * @param event evento de acción disparado por el botón
     */
    @FXML
    private void handleReglas(ActionEvent event) {
        try {
            App.openWindow("reglas", "Reglas del juego");
        } catch (IOException e) {
            System.err.println("Error al abrir reglas: " + e.getMessage());
        }
    }
}