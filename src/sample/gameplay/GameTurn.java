package sample.gameplay;

import sample.world.Cell;
import sample.world.World;

public class GameTurn implements Runnable {
    private World world;
    private int turn;
    private int aliveCells;

    public GameTurn(World world) {
        this.world = world;
        turn = 1;
        aliveCells = 0;
    }

    @Override
    public void run() {
        checkCells();
        updateCells();
        printTurnSummary();
        aliveCells = 0;
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
        if (cell.isAlive() && (neighbours < 2 || neighbours > 3)) {
            cell.setChangeStateInNextTurn(true);
        }
    }

    private void checkIfCellWillBorn(Cell cell, int neighbours) {
        if (!cell.isAlive() && neighbours == 3) {
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

    private void printTurnSummary() {
        System.out.println(String.format("Turn : %d ; Alive cells : %d", turn++, aliveCells));
    }
}
