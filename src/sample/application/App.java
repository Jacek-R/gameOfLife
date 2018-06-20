package sample.application;

import javafx.stage.Stage;
import sample.gameplay.GameTurn;
import sample.screen.GameScreen;
import sample.world.Configuration;
import sample.world.Generator;
import sample.world.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    private Configuration configuration;
    private Stage primaryStage;
    private World world;

    public App(Configuration configuration, Stage primaryStage) {
        this.configuration = configuration;
        this.primaryStage = primaryStage;
    }

    public void setup() {
        Generator generator = new Generator(configuration);
        world = generator.getRandomlyPopulatedWorld();
    }

    public void prepareScene() {
        GameScreen screen = new GameScreen();

        primaryStage.setScene(screen.createScene(world));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new GameTurn(world), 2000, configuration.getTurnInterval(), TimeUnit.MILLISECONDS);
    }
}
