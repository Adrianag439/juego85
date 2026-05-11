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
 * Controlador de la pantalla de descubrimiento de la regla secreta en el nivel Difícil.
 * <p>
 * Permite al jugador ingresar números en el rango [10, 55] para observar
 * la transformación aplicada por la regla del juego (suma de factoriales
 * de los dígitos) antes de pasar a la fase de verificación.
 * Utiliza {@link Partida#calcularFactorial(int)} para cada dígito y
 * {@link Niveles} para configurar los parámetros del nivel Difícil.
 * </p>
 *
 * @author Equipo de desarrollo
 * @version 1.0
 */
public class AdivinardifController implements Initializable {

    /** Campo de texto donde el jugador ingresa el número a evaluar. */
    @FXML private TextField txtNumero;

    /** Tabla que muestra los pares entrada/salida registrados durante la sesión. */
    @FXML private TableView<FilaRegla> tablaRegla;

    /** Columna que muestra el número ingresado por el jugador. */
    @FXML private TableColumn<FilaRegla, String> colEntrada;

    /** Columna que muestra el resultado calculado según la regla secreta. */
    @FXML private TableColumn<FilaRegla, String> colSalida;

    /** Etiqueta de bienvenida que muestra el apodo del jugador, el nivel e intentos disponibles. */
    @FXML private Label lblBienvenida; // Agrega este Label en tu FXML

    /** Lista observable que respalda la {@link #tablaRegla}. */
    private ObservableList<FilaRegla> listaFilas;

    /** Instancia de partida usada para calcular los factoriales de los dígitos. */
    private Partida partida;

    /** Nivel difícil con sus datos: id=2, 3 vidas, 20 pts base, con temporizador. */
    // ── Nivel difícil con sus datos reales ───────────────────
    private final Niveles nivelDificil = new Niveles(2, "Difícil", 3, 20, true) {};

    /** Valor mínimo permitido para el número ingresado en modo difícil. */
    private static final int MIN_NUMERO = 10;

    /** Valor máximo permitido para el número ingresado en modo difícil. */
    private static final int MAX_NUMERO = 55;

    /**
     * Modelo de una fila en la tabla de descubrimiento del nivel difícil.
     * <p>
     * Cada fila almacena un número de entrada y su resultado (suma de
     * factoriales de los dígitos) como propiedades JavaFX observables.
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
         * @param numero    número entero ingresado por el jugador
         * @param resultado suma de factoriales de los dígitos del número
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
     * Configura la tabla de reglas, muestra un saludo con datos de la sesión activa
     * y registra el listener de tecla ENTER en el campo de texto.
     * </p>
     *
     * @param url URL de localización del archivo FXML (no utilizado directamente)
     * @param rb  paquete de recursos de internacionalización (no utilizado directamente)
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
                + "! Nivel: " + nivelDificil.getNombre()
                + " | Intentos: " + nivelDificil.getVidas());
        }

        colEntrada.setCellValueFactory(data -> data.getValue().entradaProperty());
        colEntrada.setPrefWidth(140);
        colSalida.setCellValueFactory(data -> data.getValue().salidaProperty());
        colSalida.setPrefWidth(200);

        tablaRegla.setItems(listaFilas);
        tablaRegla.setEditable(false);

        txtNumero.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) agregarNumero();
        });
    }

    /**
     * Valida el número ingresado por el jugador, calcula la suma de factoriales
     * de sus dígitos y agrega la fila a la tabla.
     * <p>
     * Muestra alertas cuando el campo está vacío, contiene caracteres no numéricos,
     * el número es menor que {@value #MIN_NUMERO} o mayor que {@value #MAX_NUMERO}.
     * </p>
     */
    private void agregarNumero() {
        String input = txtNumero.getText().trim();

        if (input.isEmpty()) {
            mostrarAlerta("Campo vacío", "Por favor, ingrese un número.");
            return;
        }
        if (!input.matches("\\d+")) {
            mostrarAlerta("Entrada inválida",
                "Solo se permiten números enteros.\n\n"
                + "Modo difícil: números del "
                + MIN_NUMERO + " al " + MAX_NUMERO);
            txtNumero.clear();
            return;
        }

        try {
            int num = Integer.parseInt(input);

            if (num < MIN_NUMERO) {
                mostrarAlerta("Número inválido",
                    "El número debe ser mayor o igual a " + MIN_NUMERO);
                txtNumero.clear();
                return;
            }
            if (num > MAX_NUMERO) {
                mostrarAlerta("Número inválido",
                    "El número debe ser menor o igual a " + MAX_NUMERO);
                txtNumero.clear();
                return;
            }

            // ── Usar Partida.calcularFactorial para cada dígito ──
            long resultado = calcularSumaFactorialesDigitos(num);
            listaFilas.add(new FilaRegla(num, resultado));
            tablaRegla.scrollTo(listaFilas.size() - 1);
            txtNumero.clear();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Número inválido.");
            txtNumero.clear();
        }
    }

    /**
     * Calcula la suma de los factoriales de cada dígito del número dado.
     * <p>
     * Delega el cálculo de cada factorial a {@link Partida#calcularFactorial(int)}.
     * </p>
     *
     * @param numero número entero del que se calcularán los factoriales por dígito
     * @return suma de los factoriales de todos los dígitos del número
     */
    // ── Usa Partida.calcularFactorial en vez del switch local ─
    private long calcularSumaFactorialesDigitos(int numero) {
        String numStr = String.valueOf(numero);
        long suma = 0;
        for (char c : numStr.toCharArray()) {
            int digito = Character.getNumericValue(c);
            suma += partida.calcularFactorial(digito); // ← usa Partida
        }
        return suma;
    }

    /**
     * Muestra un diálogo de advertencia asociado al nivel Difícil.
     *
     * @param titulo  texto mostrado como encabezado de la alerta
     * @param mensaje texto mostrado como contenido de la alerta
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Modo " + nivelDificil.getNombre()); // ← usa Niveles
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Maneja el evento del botón "ESTOY LISTO".
     * <p>
     * Navega a la pantalla de juego del nivel Difícil ({@code dificil.fxml}).
     * </p>
     *
     * @param event evento de acción disparado por el botón
     */
    @FXML
    private void handleEstoyListo(ActionEvent event) {
        try {
            App.setRoot("dificil");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento del botón "REGLAS".
     * <p>
     * Abre una ventana secundaria con las reglas del nivel Difícil ({@code reglasdif.fxml}).
     * </p>
     *
     * @param event evento de acción disparado por el botón
     */
    @FXML
    private void handleReglas(ActionEvent event) {
        try {
            App.openWindow("regladif", "Reglas - Modo " + nivelDificil.getNombre());
        } catch (IOException e) {
            System.err.println("Error al abrir reglas: " + e.getMessage());
        }
    }
}