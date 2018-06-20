package sample.gameplay.move;

public enum Direction {
    EAST(1, 0), WEST(-1, 0), NORTH(0, -1), SOUTH(0, 1), NORTH_EAST(1, -1), SOUTH_EAST(1, 1), NORTH_WEST(-1, -1), SOUTH_WEST(-1, 1);

    private int[] offset;
    private static final int X = 0;
    private static final int Y = 1;

    Direction(int x, int y) {
        offset = new int[]{x, y};
    }

    public int[] getOffset() {
        return offset;
    }

    public int getX() {
        return offset[X];
    }

    public int getY() {
        return offset[Y];
    }
}
