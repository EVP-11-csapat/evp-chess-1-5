package chess15.gui;

import chess15.gui.newui.Variables;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main entrypoint of the UI application.
 * The Main class calls its open method
 */
public class ChessApplication extends Application {
    /**
     * The UI window setup code
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception if mainMenu.fxml is not found
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ChessApplication.class.getResource("scenes/newMainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        primaryStage.setTitle("Chess 1.5 ver 1.2" + (Variables.DEVMODE ? " Dev" : ""));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * The main entrypoint of the application
     */
    public static void open() {
        launch();
    }
}
