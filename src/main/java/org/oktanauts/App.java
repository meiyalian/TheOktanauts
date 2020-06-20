package org.oktanauts;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static Stage stage;

    /**
     * Starts the UI of the application
     *
     * @param stage the stage for the start of the UI of the application
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("userLogin"));
        App.stage = stage;

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Sets the root scene of the application
     *
     * @param fxml the string of the root scene of the application
     * @throws IOException
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Sets the root scene of the application
     *+
     * @param fxmlLoader the loader which loads the new root scene of the application
     * @throws IOException
     */
    public static void setRoot(FXMLLoader fxmlLoader) throws IOException {
        scene.setRoot(fxmlLoader.load());
    }

    /**
     * Loads the scene of the application
     *
     * @param fxml the string representing the scene file
     * @return the scene parent of the application view
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Gets the current stage of the application UI
     *
     * @return the current stage of the application UI
     */
    public static Stage getStage(){
        return stage;
    }

    /**
     * Launches the application
     *
     * @param args assorted arguments
     */
    public static void main(String[] args) {
        launch();
    }

}