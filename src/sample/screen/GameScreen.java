package sample.screen;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import sample.application.App;
import sample.screen.utils.GridConstraints;
import sample.world.Cell;
import sample.world.Configuration;
import sample.world.World;

public class GameScreen {

    private static final double PADDING = 6.0;
    private static final double OPTIONS_HEIGHT = 10.0;
    private static final double CONFIGURATION_LABELS_WIDTH = 15.0;
    private static final double CONFIGURATION_SELECTIONS_WIDTH = 10.0;
    private static final double CONTROL_WIDTH = 25.0;
    private static final double MAP_WIDTH = 50.0;

    private static final double ELEMENTS_HEIGHT = 100.0;
    private static final double HELP_HEIGHT = 50.0;

    private App app;
    private GridPane gameMap;

    private ComboBox<Integer> widthBox;
    private ComboBox<Integer> heightBox;
    private ComboBox<Integer> intervalBox;
    private ComboBox<Integer> cellsBox;

    private boolean gameParametersChanged;

    public GameScreen(App app) {
        this.app = app;
    }

    public Scene createScene() {
        GridPane root = createRoot();
        return new Scene(root);
    }

    private GridPane createRoot() {
        GridPane root = new GridPane();
        GridPane configurationLabelsBox = createConfigurationLabels();
        GridPane configurationSelectionsBox = createConfigurationSelections();
        GridPane controlPanelBox = createButtons();
        gameMap = new GridPane();

        GridConstraints.column(root, CONFIGURATION_LABELS_WIDTH, CONFIGURATION_SELECTIONS_WIDTH, MAP_WIDTH, CONTROL_WIDTH);
        GridConstraints.row(root, ELEMENTS_HEIGHT);
        root.addRow(0, configurationLabelsBox, configurationSelectionsBox, gameMap, controlPanelBox);
        populateMapFromConfiguration();
        return root;
    }

    private GridPane createButtons() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(PADDING));
        Button start = createButton("Start");
        start.setOnMousePressed(createStartListener());

        Button pause = createButton("Pause");
        pause.setOnMousePressed(createPauseListener());

        Button restart = createButton("Restart");
        restart.setOnMousePressed(createRestartListener());

        Button stop = createButton("Stop");
        stop.setOnMousePressed(createStopListener());

        Label help = new Label("Click start to begin simulation.\n\nRestart if you want to refresh playing field.\n\nYou can pause and continue" +
                " the game with Pause button.\n\nWhen you click the stop button, the simulation will end, and you need to start it again\n\n" +
                "You can also click the cells in the grid to toggle their status (when the simulation is not in progress)");
        help.setWrapText(true);
        help.setTextAlignment(TextAlignment.JUSTIFY);

        GridConstraints.row(gridPane, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, HELP_HEIGHT);
        gridPane.addColumn(0, start, pause, restart, stop, help);
        return gridPane;
    }

    private EventHandler<MouseEvent> createStopListener() {
        return event -> {
            if (app.isRunning()) {
                app.stop();
            }
        };
    }

    private Button createButton(String text) {
        return new Button(text);
    }

    private EventHandler<MouseEvent> createStartListener() {
        return event -> {
            if (!app.isRunning()) {
                if (gameParametersChanged) {
                    populateMapFromConfiguration();
                }
                app.start();
            }
        };
    }

    private EventHandler<MouseEvent> createRestartListener() {
        return event -> {
            if (!app.isRunning()) {
                populateMapFromConfiguration();
            }
        };
    }

    private EventHandler<MouseEvent> createPauseListener() {
        return event -> {
            if (app.isRunning()) {
                app.getGameTurn().togglePause();
            }
        };
    }

    private void populateMapFromConfiguration() {
        Configuration configuration = app.getConfiguration();
        configuration.setHeight(heightBox.getValue());
        configuration.setWidth(widthBox.getValue());
        configuration.setTurnInterval(intervalBox.getValue());
        configuration.setStartingAliveCells(cellsBox.getValue());
        app.setup();
        createMap(app.getWorld(), gameMap);
        gameParametersChanged = false;
    }

    private GridPane createConfigurationSelections() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(PADDING));
        GridConstraints.row(gridPane, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT);
        widthBox = createComboBox(5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        heightBox = createComboBox(5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        intervalBox = createComboBox(150, 250, 500, 750, 1000, 1500, 2000, 2500, 3000);
        cellsBox = createComboBox(0, 5, 10, 15, 20, 25, 30);
        gridPane.addColumn(0, widthBox, heightBox, intervalBox, cellsBox);
        return gridPane;
    }

    private GridPane createConfigurationLabels() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(PADDING));
        String[] labels = {"Width", "Height", "Interval", "Starting cells in percent"};
        GridConstraints.row(gridPane, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT);
        for (int i = 0; i < labels.length; i++) {
            gridPane.add(new Text(labels[i]), 0, i);
        }
        return gridPane;
    }

    private ComboBox<Integer> createComboBox(int... options) {
        int DEFAULT_SELECTION = 3;
        ComboBox<Integer> comboBox = new ComboBox<>();
        for (int option : options) {
            comboBox.getItems().add(option);
        }
        comboBox.getSelectionModel().select(DEFAULT_SELECTION);
        comboBox.valueProperty().addListener(createComboBoxChangeListener());
        return comboBox;
    }

    private ChangeListener<Integer> createComboBoxChangeListener() {
        return (observable, oldValue, newValue) -> gameParametersChanged = true;
    }

    private void createMap(World world, GridPane gridPane) {
        gridPane.getChildren().clear();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCellAt(x, y);
                ImageView imageView = cell.getImageView();
                StackPane stackPane = createStackPaneWithCellImage(imageView, cell.getImage());
                stackPane.setOnMousePressed(addCellClickListener(cell));
                gridPane.add(stackPane, x, y);
            }
        }
    }

    private EventHandler<MouseEvent> addCellClickListener(Cell cell) {
        return event -> {
            if (!app.isRunning()) {
                cell.setAlive(!cell.isAlive());
            }
        };
    }

    private StackPane createStackPaneWithCellImage(ImageView imageView, Image image) {
        imageView.setImage(image);
        StackPane stackPane = new StackPane(imageView);
        stackPane.setMinSize(0, 0);
        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        return stackPane;
    }
}
