package sample.world;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.gameplay.move.MoveUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Cell {

    private boolean alive;
    private ImageView imageView;
    private int x;
    private int y;
    private static Image aliveImage;
    private static Image deadImage;

    static {
        try {
            aliveImage = readImageFromPath("resources/alive.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            deadImage = readImageFromPath("resources/dead.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean changeStateInNextTurn;

    public Cell(int x, int y) {
        imageView = new ImageView();
        this.x = x;
        this.y = y;
    }

    public Cell(int x, int y, boolean alive) {
        this(x, y);
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        setImageView(getImage());
    }

    public ImageView getImageView() {
        return imageView;
    }

    private void setImageView(Image image) {
        Platform.runLater(() -> imageView.setImage(image));
    }

    public boolean isChangeStateInNextTurn() {
        return changeStateInNextTurn;
    }

    public void setChangeStateInNextTurn(boolean changeStateInNextTurn) {
        this.changeStateInNextTurn = changeStateInNextTurn;
    }

    public Image getImage() {
        Image image;
        if (alive) {
            image = aliveImage;
        } else {
            image = deadImage;
        }
        return image;
    }

    private static Image readImageFromPath(String path) throws FileNotFoundException {
        return new Image(new FileInputStream(path));
    }

    public int getNumberOfNeighbours(World world) {
        int neighbours = 0;
        MoveUtils moveUtils = new MoveUtils(world);
        List<Cell> neighbouringCells = moveUtils.getNeighbouringCells(x, y);
        for (Cell cell : neighbouringCells) {
            if (cell.isAlive()) {
                neighbours++;
            }
        }
        return neighbours;
    }
}
