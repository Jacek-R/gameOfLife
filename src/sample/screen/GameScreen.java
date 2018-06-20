package sample.screen;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.screen.utils.GridConstraints;
import sample.world.Cell;
import sample.world.World;

public class GameScreen {

    private int width;
    private int height;

    private static final double PADDING = 6.0;
    private static final double OPTIONS_HEIGHT = 10.0;
    private static final double CONFIGURATION_LABELS_WIDTH = 15.0;
    private static final double CONFIGURATION_SELECTIONS_WIDTH = 10.0;
    private static final double CONTROL_WIDTH = 25.0;
    private static final double MAP_WIDTH = 50.0;

    private static final double ELEMENTS_HEIGHT = 100.0;

    public Scene createScene(World world) {
        this.width = world.getWidth();
        this.height = world.getHeight();
        GridPane root = createRoot(world);
        return new Scene(root);
    }

    private GridPane createRoot(World world) {
        GridPane root = new GridPane();
        GridPane configurationLabelsBox = createConfigurationLabels();
        GridPane configurationSelectionsBox = createConfigurationSelections();
        GridPane controlPanelBox = createButtons();
        GridPane gameMap = createMap(world);

        GridConstraints.column(root, CONFIGURATION_LABELS_WIDTH, CONFIGURATION_SELECTIONS_WIDTH, MAP_WIDTH, CONTROL_WIDTH);
        GridConstraints.row(root, ELEMENTS_HEIGHT);
        root.addRow(0, configurationLabelsBox, configurationSelectionsBox, gameMap, controlPanelBox);
        return root;
    }

    private GridPane createButtons() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(PADDING));
        Button start = createButton("Start");
        Button pause = createButton("Pause");
        Button restart = createButton("Restart");
        GridConstraints.row(gridPane, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT);
        gridPane.addColumn(0, start, pause, restart);
        return gridPane;
    }

    private Button createButton(String text) {
        return new Button(text);
    }

    private GridPane createConfigurationSelections() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(PADDING));
        GridConstraints.row(gridPane, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT, OPTIONS_HEIGHT);
        ComboBox<Integer> widths = createComboBox(5, 10, 15, 20, 25, 30);
        ComboBox<Integer> heights = createComboBox(5, 10, 15, 20, 25, 30);
        ComboBox<Integer> intervals = createComboBox(150, 250, 500, 750, 1000, 1500, 2000, 2500, 3000);
        ComboBox<Integer> cellsInPercentage = createComboBox(5, 10, 15, 20, 25, 30);
        gridPane.addColumn(0, widths, heights, intervals, cellsInPercentage);
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

    private <T> ComboBox<T> createComboBox(T... options) {
        ComboBox<T> comboBox = new ComboBox<>();
        for (T option : options) {
            comboBox.getItems().add(option);
        }
        return comboBox;
    }

    private GridPane createMap(World world) {
        GridPane gridPane = new GridPane();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = world.getCellAt(x, y);
                ImageView imageView = cell.getImageView();
                StackPane stackPane = createStackPaneWithCellImage(imageView, cell.getImage());
                gridPane.add(stackPane, x, y);
            }
        }
        return gridPane;
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
