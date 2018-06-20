package sample.application;

import javafx.scene.control.Label;
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

    private static final int DEFAULT_DELAY = 1500;

    private ScheduledExecutorService scheduler;

    private Configuration configuration;
    private Stage primaryStage;
    private World world;
    private GameTurn gameTurn;

    private boolean isRunning;

    public App(Configuration configuration, Stage primaryStage) {
        this.configuration = configuration;
        this.primaryStage = primaryStage;
    }

    public void setup() {
        Generator generator = new Generator(configuration);
        world = generator.getRandomlyPopulatedWorld();
    }

    public void prepareScene() {
        GameScreen screen = new GameScreen(this);

        primaryStage.setScene(screen.createScene());
        primaryStage.setFullScreen(true);
        setOnCloseHandler();
        primaryStage.show();
    }

    private void setOnCloseHandler() {
        primaryStage.setOnCloseRequest(event -> {
            if (isRunning) {
                scheduler.shutdown();
            }
        });
    }

    public void start(Label log) {
        isRunning = true;
        scheduler = Executors.newScheduledThreadPool(1);
        gameTurn = new GameTurn(world, log);
        scheduler.scheduleAtFixedRate(gameTurn, DEFAULT_DELAY, configuration.getTurnInterval(), TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdown();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public World getWorld() {
        return world;
    }

    public GameTurn getGameTurn() {
        return gameTurn;
    }
}
