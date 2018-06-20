package sample.world;

public class Configuration {

    private int width;
    private int height;
    private int startingAliveCells;
    private int turnInterval;

    public Configuration(int width, int height, int startingAliveCells, int turnInterval) {
        this.width = width;
        this.height = height;
        this.startingAliveCells = startingAliveCells;
        this.turnInterval = turnInterval;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartingAliveCells() {
        return startingAliveCells;
    }

    public int getTurnInterval() {
        return turnInterval;
    }
}
