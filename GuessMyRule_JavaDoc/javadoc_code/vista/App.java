package vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Punto de entrada principal de la aplicación Guess My Rule.
 * <p>
 * Extiende {@link javafx.application.Application} e inicializa
 * la escena principal con la pantalla de ingreso de apodo.
 * Gestiona la navegación entre vistas mediante el método estático
 * {@link #setRoot(String)}, que reemplaza el contenido de la escena
 * sin abrir nuevas ventanas.
 * </p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class App extends Application {

    /** Escena única compartida por toda la aplicación. */
    private static Scene scene;

    /**
     * Método invocado por JavaFX al arrancar la aplicación.
     * <p>
     * Carga el FXML {@code ingreso} como vista inicial,
     * configura la escena con tamaño 640×480 y muestra
     * la ventana principal.
     * </p>
     *
     * @param stage ventana principal proporcionada por JavaFX
     * @throws IOException si el archivo ingreso.fxml no puede cargarse
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("ingreso"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Guess My Rule!");
        stage.show();
    }

    /**
     * Cambia la vista raíz de la escena actual cargando el FXML indicado.
     * <p>
     * Permite navegar entre pantallas sin abrir nuevas ventanas,
     * reutilizando siempre la misma escena.
     * Ejemplo de uso desde un controlador:
     * </p>
     * <pre>{@code
     *   App.setRoot("niveles"); // navega a niveles.fxml
     * }</pre>
     *
     * @param fxml nombre del archivo FXML (sin extensión) en la carpeta /fx/
     * @throws IOException si el archivo FXML no puede cargarse
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carga un archivo FXML desde la carpeta de recursos {@code /fx/}.
     *
     * @param fxml nombre del archivo FXML sin extensión
     * @return nodo raíz del FXML cargado
     * @throws IOException si el archivo no se encuentra o no puede parsearse
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fx/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Método principal. Inicia el ciclo de vida de la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        launch();
    }
}
