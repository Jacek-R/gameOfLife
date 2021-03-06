package sample.gameplay;

import javafx.application.Platform;
import javafx.scene.control.Label;
import sample.world.Cell;
import sample.world.Configuration;
import sample.world.World;

import java.util.ArrayList;

public class GameTurn implements Runnable {
    private World world;
    private int turn;
    private int aliveCells;
    private boolean pause;
    private Label log;
    private Configuration configuration;

    public GameTurn(World world, Configuration configuration, Label log) {
        this.world = world;
        turn = 1;
        aliveCells = 0;
        this.log = log;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        if (!pause) {
            checkCells();
            updateCells();
            printTurnSummary(aliveCells);
            aliveCells = 0;
        }
    }

    private void checkCells() {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCellAt(x, y);
                checkCell(cell);
            }
        }
    }

    private void checkCell(Cell cell) {
        int neighbours = cell.getNumberOfNeighbours(world);
        checkIfCellWillDie(cell, neighbours);
        checkIfCellWillBorn(cell, neighbours);
        increaseCounterIfAlive(cell);
    }

    private void checkIfCellWillDie(Cell cell, int neighbours) {
        ArrayList<Integer> neighboursRequirements = configuration.getNeighboursForCellToStayAlive();
        if (cell.isAlive() && !neighboursRequirements.contains(neighbours)) {
            cell.setChangeStateInNextTurn(true);
        }
    }

    private void checkIfCellWillBorn(Cell cell, int neighbours) {
        ArrayList<Integer> neighboursRequirements = configuration.getNeighboursToBornNewCell();
        if (!cell.isAlive() && neighboursRequirements.contains(neighbours)) {
            cell.setChangeStateInNextTurn(true);
        }
    }

    private void increaseCounterIfAlive(Cell cell) {
        if (cell.isAlive()) {
            aliveCells++;
        }
    }

    private void updateCells() {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCellAt(x, y);
                updateCellIfNecessary(cell);
            }
        }
    }

    private void updateCellIfNecessary(Cell cell) {
        if (cell.isChangeStateInNextTurn()) {
            cell.setChangeStateInNextTurn(false);
            boolean isAlive = cell.isAlive();
            cell.setAlive(!isAlive);
        }
    }

    private void printTurnSummary(int aliveCells) {
        Platform.runLater(() -> log.setText(String.format("Turn : %d ; Alive cells : %d\n", turn++, aliveCells) + log.getText()));
    }

    public void togglePause() {
        pause = !pause;
    }
}
