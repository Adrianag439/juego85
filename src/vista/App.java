package vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class App extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        setRoot("ingreso");
        stage.setTitle("Mi Juego");
        stage.show();
    }
    
    public static void setRoot(String fxml) throws IOException {
        // Busca el archivo en diferentes rutas
        URL location = App.class.getResource("/fx/" + fxml + ".fxml");
        if (location == null) {
            location = App.class.getResource("/" + fxml + ".fxml");
        }
        if (location == null) {
            location = App.class.getResource(fxml + ".fxml");
        }
        if (location == null) {
            throw new IOException("No se encontró el archivo: " + fxml + ".fxml");
        }
        
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }
    
    public static void openWindow(String fxml, String titulo) throws IOException {
        URL location = App.class.getResource("/fx/" + fxml + ".fxml");
        if (location == null) {
            location = App.class.getResource("/" + fxml + ".fxml");
        }
        if (location == null) {
            location = App.class.getResource(fxml + ".fxml");
        }
        
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}