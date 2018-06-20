package sample.world;

public class Configuration {

    private int width;
    private int height;
    private int startingAliveCells;
    private int turnInterval;
    private int neighboursToResurrectCell;
    private int minimalCellsToStayAlive;
    private int maximumCellsToStayAlive;

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int getStartingAliveCells() {
        return startingAliveCells;
    }

    public int getTurnInterval() {
        return turnInterval;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setStartingAliveCells(int startingAliveCells) {
        this.startingAliveCells = startingAliveCells;
    }

    public void setTurnInterval(int turnInterval) {
        this.turnInterval = turnInterval;
    }

    public int getNeighboursToResurrectCell() {
        return neighboursToResurrectCell;
    }

    public void setNeighboursToResurrectCell(int neighboursToResurrectCell) {
        this.neighboursToResurrectCell = neighboursToResurrectCell;
    }

    public int getMinimalCellsToStayAlive() {
        return minimalCellsToStayAlive;
    }

    public void setMinimalCellsToStayAlive(int minimalCellsToStayAlive) {
        this.minimalCellsToStayAlive = minimalCellsToStayAlive;
    }

    public int getMaximumCellsToStayAlive() {
        return maximumCellsToStayAlive;
    }

    public void setMaximumCellsToStayAlive(int maximumCellsToStayAlive) {
        this.maximumCellsToStayAlive = maximumCellsToStayAlive;
    }
}
