package nz.ac.wew248.simplethesmin.model.Graham;

import java.io.FileOutputStream;
import java.util.ArrayList;

import nz.ac.wew248.simplethesmin.model.Direction;
import nz.ac.wew248.simplethesmin.model.IGame;
import nz.ac.wew248.simplethesmin.model.IObserver;
import nz.ac.wew248.simplethesmin.model.ISubject;
import nz.ac.wew248.simplethesmin.model.Wall;

public class GameAdapter extends Game implements IGame, ISubject {
    public GameAdapter() {
        aObserver = new ArrayList<IObserver>();
    }


    private ArrayList<IObserver> aObserver;

    public void attach(IObserver observer) {
        aObserver.add(observer);
    }

    public void detach(IObserver observer) {
        aObserver.remove(observer);
    }

    public void inform() {
        for(IObserver obs : aObserver) {
            obs.update();
        }
    }

    @Override
    public String getLevelFileName() {
        return "";
    }

    @Override
    public void checkWonLost() {

    }

    public nz.ac.wew248.simplethesmin.model.Point getTheseus() {
        return new nz.ac.wew248.simplethesmin.model.Point(getPlayerPosition().x, getPlayerPosition().y);
    }

    public nz.ac.wew248.simplethesmin.model.Point getMinotaur() {
        return new nz.ac.wew248.simplethesmin.model.Point(getMinotaurPosition().x, getMinotaurPosition().y);
    }

    public Wall[][] getLeft() {
        GameLevel gameLevel = getMyGameLevel();
        GameLevelTile[][] gameLevelTiles = gameLevel.getLevelGameLevelTiles();
        int x = gameLevelTiles[0].length;
        int y = gameLevelTiles.length;
        Wall[][] left = new Wall[y][x];

        for(int yy = 0; yy < gameLevelTiles.length; yy++) {
            for(int xx = 0; xx < gameLevelTiles[0].length; xx++) {
                if(gameLevelTiles[yy][xx].leftOfTile().equals(EnumWall.WALL)){
                    left[xx][yy] = Wall.WALL;
                } else {
                    left[xx][yy] = Wall.EMPTY;
                }
            }
        }
        return left;
    }

    public Wall[][] getUp() {
        GameLevel gameLevel = getMyGameLevel();
        GameLevelTile[][] gameLevelTiles = gameLevel.getLevelGameLevelTiles();
        int x = gameLevelTiles[0].length;
        int y = gameLevelTiles.length;
        Wall[][] up = new Wall[y][x];
        for(int yy = 0; yy < gameLevelTiles.length; yy++) {
            for(int xx = 0; xx < gameLevelTiles[0].length; xx++) {
                if(gameLevelTiles[yy][xx].topOfTile().equals(EnumWall.WALL)){
                    up[xx][yy] = Wall.WALL;
                } else {
                    up[xx][yy] = Wall.EMPTY;
                }
            }
        }

        return up;

    }

    public nz.ac.wew248.simplethesmin.model.Point getExit() {
        GameLevel gameLevel = getMyGameLevel();
        Point exit = gameLevel.getExitPosition();
        return new nz.ac.wew248.simplethesmin.model.Point(exit.x, exit.y);
    }

    public void moveTheseus(Direction direction) {
        if (direction.equals(Direction.UP)) {
            playerTurn("UP");
        } else if (direction.equals(Direction.DOWN)) {
            playerTurn("DOWN");
        }else if (direction.equals(Direction.LEFT)) {
            playerTurn("LEFT");
        } else if (direction.equals(Direction.RIGHT)) {
            playerTurn("RIGHT");
        }
        minotaurTurn();
        minotaurTurn();
        inform();
    }

    public int getMoveCount() {
        return super.getMoveCount();
    }

    public void reset() {
        super.reset();
        inform();
    }
    public void undoMove() {

    }
    public void loadLevel(String level) {
        run(level);
    }
    public void saveGame(FileOutputStream file) {

    }
    public boolean getWin() {
        return hasPlayerWon();
    }
    public boolean getLose() {
        return hasMinotaurWon();
    }




}
