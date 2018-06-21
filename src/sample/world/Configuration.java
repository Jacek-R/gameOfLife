package sample.world;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class Configuration {

    private int width;
    private int height;
    private int startingAliveCells;
    private int turnInterval;
    private ArrayList<Integer> neighboursToBornNewCell;
    private ArrayList<Integer> neighboursForCellToStayAlive;

    public Configuration() {
        neighboursForCellToStayAlive = new ArrayList<>();
        neighboursToBornNewCell = new ArrayList<>();
    }

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

    public void setAliveRequirements(CheckBox[] deathCheckBoxes) {
        populateArray(deathCheckBoxes, neighboursForCellToStayAlive);
    }

    public void setBornRequirements(CheckBox[] lifeCheckBoxes) {
        populateArray(lifeCheckBoxes, neighboursToBornNewCell);
    }

    public ArrayList<Integer> getNeighboursToBornNewCell() {
        return neighboursToBornNewCell;
    }

    public ArrayList<Integer> getNeighboursForCellToStayAlive() {
        return neighboursForCellToStayAlive;
    }

    private void populateArray(CheckBox[] checkBoxes, ArrayList<Integer> arrayList) {
        arrayList.clear();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                arrayList.add(Integer.parseInt(checkBox.getText()));
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add(0);
        }
    }
}
