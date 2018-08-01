package nz.ac.wew248.simplethesmin.model.Graham;

import java.util.Dictionary;
import java.util.Hashtable;

public class GameLevel {
    private String[] allRows = {};
    private String[] allCols = {};
    public GameLevelTile[][] levelGameLevelTiles = {};
    private Dictionary<String, String> dictKeyInFileData = new Hashtable<String, String>();
    private String separator = ",";
    private Point minotaurStartPosition;
    private Point playerStartPosition;
    private Point exitPos;

    public GameLevel() {
        dictKeyInFileData.put("Above", "U");
        dictKeyInFileData.put("Left", "L");
        dictKeyInFileData.put("Theseus", "T");
        dictKeyInFileData.put("CharacterMinotaur", "M");
        dictKeyInFileData.put("Exit", "E");
    }

    public GameLevelTile[][] getLevelGameLevelTiles() {
        return levelGameLevelTiles;
    }

    public int getMazeWidth() {
        return allCols[0].length() - 1;
    }

    public int getMazeHeight() {
        return allRows.length - 1;
    }

    private void setExitPosition(Point newPosition) {
        exitPos = newPosition;
    }

    public Point getExitPosition() {
        return exitPos;
    }

    public Point getMinotaurStartPosition() {
        return minotaurStartPosition;
    }

    public Point getPlayerStartPosition() {
        return playerStartPosition;
    }

    private void setMinotaurStartPosition(Point newStartPosition) {
        minotaurStartPosition = newStartPosition;
    }

    private void setPlayerStartPosition(Point newStartPosition) {
        playerStartPosition = newStartPosition;
    }

    public boolean isWallAbove(int x, int y) {
        boolean output = false;

        try {
            output = (levelGameLevelTiles[x][y].topOfTile() == EnumWall.WALL);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            output = true;
        }

        return output;
    }

    public boolean isWallBelow(int x, int y) {
        boolean output = false;

        try {
            output = (levelGameLevelTiles[x][y + 1].topOfTile() == EnumWall.WALL);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            output = true;
        }

        return output;
    }

    public boolean isWallLeft(int x, int y) {
        boolean output = false;

        try {
            output = (levelGameLevelTiles[x][y].leftOfTile() == EnumWall.WALL);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            output = true;
        }

        return output;
    }

    public boolean isWallRight(int x, int y) {
        boolean output = false;

        try {
            output = (levelGameLevelTiles[x + 1][y].leftOfTile() == EnumWall.WALL);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            output = true;
        }

        return output;
    }

    private String[] splitString(String toSplit, String separator) {
        String[] output = null;

        if (toSplit != null)
            output = toSplit.split(separator);

        return output;
    }

    private Point getLocationOfItem(String item,  Dictionary<String, String> levelData) {
        String toSplit = levelData.get(dictKeyInFileData.get(item));
        String[] objectPosition = splitString(toSplit, separator);
        int x = Integer.parseInt(objectPosition[0]);
        int y = Integer.parseInt(objectPosition[1]);

        return new Point(x, y);
    }

    private void setupLevelWalls(Dictionary<String, String> levelData) {
        String colsString = levelData.get(dictKeyInFileData.get("Above"));
        String rowsString = levelData.get(dictKeyInFileData.get("Left"));

        allCols = colsString.split(separator);
        allRows = rowsString.split(separator);

        levelGameLevelTiles = new GameLevelTile[getMazeWidth() + 1][getMazeHeight() + 1];

        for (int row = 0; row < getMazeHeight() + 1; row++) {
            for (int col = 0; col < getMazeWidth() + 1; col++) {
                char symbolAbove = allCols[row].charAt(col);
                char symbolLeft = allRows[row].charAt(col);

                levelGameLevelTiles[col][row] = new GameLevelTile();

                if (symbolAbove == 'x') {
                    levelGameLevelTiles[col][row].setWallOnTop();
                }
                if (symbolLeft == 'x') {
                    levelGameLevelTiles[col][row].setWallOnLeft();
                }
            }
        }
    }

    private void setupLevelObjects(Dictionary<String, String> levelData) {
        setMinotaurStartPosition(getLocationOfItem("CharacterMinotaur", levelData));
        setPlayerStartPosition(getLocationOfItem("Theseus", levelData));
        setExitPosition(getLocationOfItem("Exit", levelData));
    }

    public void setupLevel(Dictionary<String, String> newLevelData) {
        Dictionary<String, String> levelData = newLevelData;
        System.out.println(levelData);
        setupLevelWalls(levelData);
        setupLevelObjects(levelData);
    }
}
