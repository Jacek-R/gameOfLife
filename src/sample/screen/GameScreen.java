package sample.screen;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import sample.application.App;
import sample.screen.utils.GridConstraints;
import sample.world.Cell;
import sample.world.Configuration;
import sample.world.World;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameScreen {

    private static final double PADDING = 6.0;
    private static final double OPTIONS_HEIGHT = 7.5;
    private static final double CONFIGURATION_LABELS_WIDTH = 15.0;
    private static final double CONFIGURATION_SELECTIONS_WIDTH = 10.0;
    private static final double CONTROL_WIDTH = 25.0;
    private static final double MAP_WIDTH = 50.0;

    private static final double ELEMENTS_HEIGHT = 100.0;
    private static final double TEXT_HEIGHT = 30.0;
    private static final String HELP_TEXT = "Click start to begin simulation.\n\nRestart if you want to refresh playing field.\n\nYou can pause and continue" +
            " the game with Pause button.\n\nWhen you click the stop button, the simulation will end, and you need to start it again\n\n" +
            "You can also click the cells in the grid to toggle their status (when the simulation is not in progress)";
    private static final double FONT_SIZE = 12;
    private static final Color FONT_COLOR = Color.YELLOW;

    private App app;
    private GridPane gameMap;
    private Label log;

    private ComboBox<Integer> widthBox;
    private ComboBox<Integer> heightBox;
    private ComboBox<Integer> intervalBox;
    private ComboBox<Integer> cellsBox;

    private Font font;

    private boolean gameParametersChanged;

    public GameScreen(App app) {
        this.app = app;
        font = Font.font(null, FontWeight.BOLD, FONT_SIZE);
    }

    public Scene createScene() {
        GridPane root = createRoot();
        setRootBackground(root);
        return new Scene(root);
    }

    private void setRootBackground(GridPane root) {
        Image image = getImage("resources/background.png");
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
        Background background = new Background(backgroundImage);
        root.setBackground(background);
    }


    private Image getImage(String path) {
        Image image = null;
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }

    private GridPane createRoot() {
        GridPane root = new GridPane();
        root.setPadding(new Insets(PADDING));
        GridPane configurationLabelsBox = createConfigurationLabels();
        GridPane configurationSelectionsBox = createConfigurationSelections();
        GridPane controlPanelBox = createRightPanel();
        gameMap = new GridPane();

        GridConstraints.column(root, CONFIGURATION_LABELS_WIDTH, CONFIGURATION_SELECTIONS_WIDTH, MAP_WIDTH, CONTROL_WIDTH);
        GridConstraints.row(root, ELEMENTS_HEIGHT);
        root.addRow(0, configurationLabelsBox, configurationSelectionsBox, gameMap, controlPanelBox);
        populateMapFromConfiguration();
        return root;
    }

    private GridPane createRightPanel() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(PADDING);
        gridPane.setPadding(new Insets(PADDING));

        StackPane start = createButton("Start");
        start.setOnMousePressed(createStartListener(start));

        StackPane pause = createButton("Pause");
        pause.setOnMousePressed(createPauseListener(pause));

        StackPane restart = createButton("Refresh");
        restart.setOnMousePressed(createRestartListener(restart));

        StackPane stop = createButton("Stop");
        stop.setOnMousePressed(createStopListener(stop));

        Label help = new Label(HELP_TEXT);
        help.setTextFill(FONT_COLOR);
        help.setFont(font);
        help.setWrapText(true);
        help.setTextAlignment(TextAlignment.JUSTIFY);

        ScrollPane scrollPane = createLog();
        GridConstraints.row(gridPane, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, TEXT_HEIGHT, TEXT_HEIGHT);
        gridPane.addColumn(0, start, pause, restart, stop, help, scrollPane);
        return gridPane;
    }

    private ScrollPane createLog() {
        log = new Label();
        log.setTextFill(FONT_COLOR);
        ScrollPane scrollPane = new ScrollPane(log);

        log.prefHeightProperty().bind(scrollPane.heightProperty());
        log.prefWidthProperty().bind(scrollPane.widthProperty());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Image image = getImage("resources/scrollbg.png");
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
        Background background = new Background(backgroundImage);

        log.setBackground(background);
        return scrollPane;
    }

    private EventHandler<MouseEvent> createStopListener(StackPane stackPane) {
        return event -> {
            stackPane.setOpacity(0.6f);
            if (app.isRunning()) {
                app.stop();
            }
        };
    }

    private void addListenerToPane(StackPane stackPane, Text text) {
        stackPane.setOnMouseEntered(event -> text.setFill(FONT_COLOR));
        stackPane.setOnMouseReleased(event -> stackPane.setOpacity(1f));
        stackPane.setOnMouseExited(event -> text.setFill(Color.BLACK));
    }

    private StackPane createButton(String message) {
        StackPane stackPane = new StackPane();
        stackPane.setMinSize(0, 0);
        Text text = new Text(message);
        text.setFont(font);
        ImageView imageView = new ImageView();
        imageView.setImage(getImage("resources/button.png"));
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        stackPane.getChildren().addAll(imageView, text);
        addListenerToPane(stackPane, text);
        return stackPane;
    }

    private EventHandler<MouseEvent> createStartListener(StackPane stackPane) {
        return event -> {
            stackPane.setOpacity(0.6f);
            if (!app.isRunning()) {
                if (gameParametersChanged) {
                    populateMapFromConfiguration();
                }
                log.setText("");
                app.start(log);
            }
        };
    }

    private EventHandler<MouseEvent> createRestartListener(StackPane stackPane) {
        return event -> {
            stackPane.setOpacity(0.6f);
            if (!app.isRunning()) {
                populateMapFromConfiguration();
            }
        };
    }

    private EventHandler<MouseEvent> createPauseListener(StackPane stackPane) {
        return event -> {
            stackPane.setOpacity(0.6f);
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
        widthBox = createComboBox(gridPane, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        heightBox = createComboBox(gridPane, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        intervalBox = createComboBox(gridPane, 150, 250, 500, 750, 1000, 1500, 2000, 2500, 3000);
        cellsBox = createComboBox(gridPane, 0, 5, 10, 15, 20, 25, 30);

        gridPane.addColumn(0, widthBox, heightBox, intervalBox, cellsBox);
        return gridPane;
    }

    private GridPane createConfigurationLabels() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(PADDING));
        String[] labels = {"Width", "Height", "Interval", "Starting cells in percent"};
        GridConstraints.row(gridPane, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT);
        for (int i = 0; i < labels.length; i++) {
            Text text = new Text(labels[i]);
            text.setFill(FONT_COLOR);
            text.setFont(font);
            gridPane.add(text, 0, i);
        }
        return gridPane;
    }

    private ComboBox<Integer> createComboBox(GridPane parent, int... options) {
        int DEFAULT_SELECTION = 3;
        ComboBox<Integer> comboBox = new ComboBox<>();
        for (int option : options) {
            comboBox.getItems().add(option);
        }
        comboBox.getSelectionModel().select(DEFAULT_SELECTION);
        comboBox.setBackground(new Background(
                new BackgroundImage(getImage("resources/combo.png"), null, null, null, null)));

        comboBox.valueProperty().addListener(createComboBoxChangeListener());
        comboBox.prefWidthProperty().bind(parent.widthProperty());
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
