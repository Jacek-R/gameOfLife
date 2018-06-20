package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.application.App;
import sample.world.Configuration;

public class Main extends Application {

    private int aliveCells = 400;
    private int interval = 500;
    private int width = 5;
    private int height = 5;

    @Override
    public void start(Stage primaryStage) {

        Configuration configuration = new Configuration(width, height, aliveCells, interval);
        App app = new App(configuration, primaryStage);
        app.setup();
        app.prepareScene();
        app.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
