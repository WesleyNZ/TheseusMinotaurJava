package nz.ac.wew248.theseusandtheminotaur.model;

public enum Direction {
    UP (0, -1),
    DOWN (0, 1),
    LEFT (-1, 0),
    RIGHT (1, 0),
    PAUSE (0, 0);

    private final Point p;

    private Direction(int x, int y) {
        p = new Point(x, y);
    }

    public Point move() {
        return p;
    }
}

