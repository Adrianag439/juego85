package controlador;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla de selección de nivel.
 * <p>
 * Presenta al jugador dos opciones: Nivel Fácil y Nivel Difícil.
 * Al seleccionar un nivel, abre la ventana correspondiente
 * en un nuevo {@link Stage} de JavaFX.
 * </p>
 *
 * <p><b>FXML asociado:</b> {@code /fx/niveles.fxml}</p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class InicController {

    /** Botón para seleccionar el Nivel Fácil. */
    @FXML
    private Button btnfacil;

    /** Botón para seleccionar el Nivel Difícil. */
    @FXML
    private Button btndificil;

    /**
     * Abre la ventana del Nivel Fácil en un nuevo Stage.
     * <p>
     * Carga el archivo {@code facil.fxml} y lo muestra en
     * una nueva ventana independiente.
     * </p>
     *
     * @throws IOException si el archivo facil.fxml no puede cargarse
     */
    @FXML
    private void hadlebutton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/facil.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Abre la ventana del Nivel Difícil en un nuevo Stage.
     * <p>
     * Carga el archivo {@code dificil.fxml} y lo muestra en
     * una nueva ventana independiente.
     * </p>
     *
     * @throws IOException si el archivo dificil.fxml no puede cargarse
     */
    @FXML
    private void hadledificl() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/dificil.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
