package sample.world;

public class World {

    private int width;
    private int height;
    private Cell[][] cells;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        createEmptyWorld();
    }

    private void createEmptyWorld() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public Cell getCellAt(int x, int y) {
        return cells[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
