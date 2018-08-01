package nz.ac.wew248.simplethesmin.model.Graham;

public class GameLevelTile {
    private EnumWall enumWallOnTop;
    private EnumWall enumWallOnLeft;

    public GameLevelTile() {
        enumWallOnTop = EnumWall.EMPTY;
        enumWallOnLeft = EnumWall.EMPTY;
    }

    public void setWallOnTop() {
        enumWallOnTop = EnumWall.WALL;
    }

    public void setWallOnLeft() {
        enumWallOnLeft = EnumWall.WALL;
    }

    public EnumWall topOfTile() {
        return enumWallOnTop;
    }

    public EnumWall leftOfTile() {
        return enumWallOnLeft;
    }
}
