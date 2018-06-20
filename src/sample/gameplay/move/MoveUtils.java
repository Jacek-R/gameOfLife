package sample.gameplay.move;

import sample.world.Cell;
import sample.world.World;

import java.util.ArrayList;
import java.util.List;

public class MoveUtils {

    private World world;

    public MoveUtils(World world) {
        this.world = world;
    }

    private boolean isCoordinateInBounds(int coordinate, int worldSize) {
        return coordinate >= 0 && coordinate < worldSize;
    }

    private boolean containsCell(int x, int y) {
        return isCoordinateInBounds(x, world.getWidth()) && isCoordinateInBounds(y, world.getHeight());
    }

    public boolean isDirectionValid(Direction direction, int x, int y) {
        return containsCell(x + direction.getX(), y + direction.getY());
    }

    private int[] getOffset(Direction direction) {
        return direction.getOffset();
    }

    public Cell getCellTo(Direction direction, int x, int y) {
        Cell neighbouringCell = null;
        if (isDirectionValid(direction, x, y)) {
            neighbouringCell = world.getCellAt(x + direction.getX(), y + direction.getY());
        }
        return neighbouringCell;
    }

    public List<Cell> getNeighbouringCells(int x, int y) {
        List<Cell> neighbours = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (isDirectionValid(direction, x, y)) {
                neighbours.add(getCellTo(direction, x, y));
            }
        }
        return neighbours;
    }

}
