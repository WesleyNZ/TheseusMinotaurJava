package nz.ac.wew248.simplethesmin.model.Graham;

public enum EnumDirection {
    UP (0, -1),
    RIGHT (1, 0),
    DOWN (0, 1),
    LEFT (-1, 0),
    SKIP (0, 0);

    private int x, y;

    EnumDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getNewX() {
        return x;
    }

    public int getNewY() {
        return y;
    }
}
