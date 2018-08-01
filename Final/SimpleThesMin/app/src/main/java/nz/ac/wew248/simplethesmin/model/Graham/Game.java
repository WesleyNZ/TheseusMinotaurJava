package nz.ac.wew248.simplethesmin.model.Graham;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;

import nz.ac.wew248.simplethesmin.model.IObserver;
import nz.ac.wew248.simplethesmin.model.ISubject;

public class Game {
    private Context myContext;
    private GameLevelLoader myGameLevelLoader;
    private GameLevel myGameLevel;
    private Character myMinotaur;
    private Character myPlayer;
    private int currentLevelNumber = 0;
    private String currentLevelData;
    private int moveCount = 0;

    public Game() {
        myGameLevelLoader = new GameLevelLoader();
        myGameLevel = new GameLevel();
    }

    public GameLevel getMyGameLevel() {
        return myGameLevel;
    }

    public void setContext(Context context) {
        myContext = context;
    }

    public void setCurrentLevelNumber(int newLevelNumber) {
        currentLevelNumber = newLevelNumber;
    }

    public void setCurrentLevelData(String levelData) {
        currentLevelData = levelData;
    }

    public String getCurrentLevelData() {
        return currentLevelData;
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public String getLevelFileName() {
        AssetManager assetManager = myContext.getAssets();
        try {
            String[] theLevels = assetManager.list("levels");
            return theLevels[currentLevelNumber];//levelNumber);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED LOADING");
        }
        return "levels/level1";//levelNumber);
    }

    public Dictionary<String, String> getLevelDataFromFile() {
        String fileName = getCurrentLevelData();
        return myGameLevelLoader.loadLineFile(fileName);
    }

    private void setupLevel(String level) {
//        setCurrentLevelNumber(levelNumber);
        setCurrentLevelData(level);
        Dictionary<String, String> levelData = getLevelDataFromFile();

        myGameLevel.setupLevel(levelData);
    }

    private void setupCharacters() {
        myPlayer = new CharacterPlayer(myGameLevel.getPlayerStartPosition());
        myMinotaur = new CharacterMinotaur(myGameLevel.getMinotaurStartPosition());
    }

    public void run(String level) {
        setupLevel(level);
        setupCharacters();
    }

    public void reset() {
        setupLevel(currentLevelData);
        setupCharacters();
        moveCount = 0;
    }


    public void increaseMoveCount() {
        moveCount++;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getMazeWidth() {
        return myGameLevel.getMazeWidth();
    }

    public int getMazeHeight() {
        return myGameLevel.getMazeHeight();
    }

    public Point getPlayerPosition() {
        return myPlayer.getPosition();
    }

    public Point getMinotaurPosition() {
        return myMinotaur.getPosition();
    }

    public Point getExitPosition() {
        return myGameLevel.getExitPosition();
    }

    public boolean isWallAbove(int x, int y) {
        return myGameLevel.isWallAbove(x, y);
    }

    public boolean isWallLeft(int x, int y) {
        return myGameLevel.isWallLeft(x, y);
    }

    private Boolean positionsMatch(Point position1, Point position2) {
        return (position1.x == position2.x && position1.y == position2.y);
    }

    public Boolean hasPlayerWon() {
        return positionsMatch(myPlayer.getPosition(), myGameLevel.getExitPosition());
    }

    public Boolean hasMinotaurWon() {
        return positionsMatch(myMinotaur.getPosition(), myPlayer.getPosition()); }

    public void checkWonLost() {
//        if (hasPlayerWon())
//            myController.gameWon();
//
//        if (hasMinotaurWon())
//            myController.gameLost();
    }

    private Boolean isNotHittingWallOrGoingOffMap(Point currentPosition, String direction) {
        Boolean result = true;
        if (direction == "UP" && myGameLevel.isWallAbove(currentPosition.x, currentPosition.y))
            result = false;
        else if (direction == "RIGHT" && myGameLevel.isWallRight(currentPosition.x, currentPosition.y))
            result = false;
        else if (direction == "DOWN" && myGameLevel.isWallBelow(currentPosition.x, currentPosition.y))
            result = false;
        else if (direction == "LEFT" && myGameLevel.isWallLeft(currentPosition.x, currentPosition.y))
            result = false;
        return result;
    }

    private String directonToMoveMinotaurIn() {
        String direction = "SKIP";
        Point pPos = myPlayer.getPosition();
        Point mPos = myMinotaur.getPosition();

        if (pPos.y < mPos.y && isNotHittingWallOrGoingOffMap(mPos, "UP"))
            direction = "UP";
        if (pPos.y > mPos.y && isNotHittingWallOrGoingOffMap(mPos, "DOWN"))
            direction = "DOWN";
        if (pPos.x < mPos.x && isNotHittingWallOrGoingOffMap(mPos, "LEFT"))
            direction = "LEFT";
        if (pPos.x > mPos.x && isNotHittingWallOrGoingOffMap(mPos, "RIGHT"))
            direction = "RIGHT";

        return direction;
    }

    private void moveMinotaur() {
        Point currentPosition = myMinotaur.getPosition();

        String direction = directonToMoveMinotaurIn();

        if (isNotHittingWallOrGoingOffMap(currentPosition, direction))
            if (!hasMinotaurWon())
                myMinotaur.move(direction);
    }

    private void movePlayer(String direction) {
        Point currentPosition = myPlayer.getPosition();
        if(isNotHittingWallOrGoingOffMap(currentPosition, direction))
            myPlayer.move(direction);
    }

    public void playerTurn(String moveDirection) {
        movePlayer(moveDirection);
        increaseMoveCount();
    }

    public void minotaurTurn() {
        moveMinotaur();
    }
}
//U=xxxxxxxo,oooooooo,oxoooooo,oooooxoo,xoxxxxxo;L=xoooooox,xxxoooox,xooooxxx,xoooooox,oxxooooo;T=2,1;M=0,1;E=1,4;
