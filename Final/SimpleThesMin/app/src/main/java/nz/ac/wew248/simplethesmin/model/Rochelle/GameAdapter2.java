package nz.ac.wew248.simplethesmin.model.Rochelle;

import java.io.FileOutputStream;
import java.util.ArrayList;

import nz.ac.wew248.simplethesmin.model.Direction;
import nz.ac.wew248.simplethesmin.model.IGame;
import nz.ac.wew248.simplethesmin.model.IObserver;
import nz.ac.wew248.simplethesmin.model.Point;
import nz.ac.wew248.simplethesmin.model.Wall;

public class GameAdapter2 extends Game implements IGame {
    public GameAdapter2() {
//        String[][] level = {{}};
        aObserver = new ArrayList<IObserver>();
//        setGameLevel(level);
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

    public Point getTheseus() {
        String theseus = getTheseusPos();
        String[] strings = theseus.split(",");
        int x = Integer.parseInt(strings[1]);
        int y = Integer.parseInt(strings[0]);
        return new Point(positionOffset(x), positionOffset(y));
    }

    private int positionOffset(int pos) {
        return (int)pos/2;
    }

    public Point getMinotaur(){
        String minotaur = getMinotaurPos();
        String[] strings = minotaur.split(",");
        int x = Integer.parseInt(strings[1]);
        int y = Integer.parseInt(strings[0]);
        return new Point(positionOffset(x), positionOffset(y));
    }

    private int getWallYSize() {
        String[][] level = getLevel();
        int countY = 0;
        for (int y = 0; y < level.length; y++) {
            if(y%2 == 0) {
                countY++;
            }
        }
        return countY;
    }

    private int getWallXSize() {
        String[][] level = getLevel();
        int countX = 0;
        for (int x = 0; x < level[0].length; x++) {
            if(x%2 == 0) {
                countX++;
            }
        }
        return countX;
    }

    public Wall[][] getLeft(){
        String[][] level = getLevel();
        Wall[][] left = new Wall[getWallYSize()][getWallXSize()];
        int X = 0;
        int Y = 0;
//        System.out.println("HERE ARE THE WALLS");
        for(int y = 1; y < level.length; y = y + 2){
//            System.out.println("Y = " + Y);
            for(int x = 0; x < level[0].length; x = x + 2){
//                System.out.println("X = " + X);
                    if(level[y][x].equals("#")){
                        left[Y][X] = Wall.WALL;
                    } else {
                        left[Y][X] = Wall.EMPTY;
                    }
                X++;
            }
            X = 0;
            Y++;
        }
        printWall(left);
        return setNullEmpty(left);
    }

    private Wall[][] setNullEmpty(Wall[][] walls) {
        for(int y = 0; y < walls.length; y++) {
            for(int x = 0; x < walls[0].length; x++) {
                if(walls[y][x] == null) {
                    walls[y][x] = Wall.EMPTY;
                }
            }
        }
        return walls;
    }

    public Wall[][] getUp(){
        String[][] level = getLevel();
        Wall[][] up = new Wall[getWallYSize()][getWallXSize()];
        int X = 0;
        int Y = 0;
        for(int y = 0; y < level.length; y = y + 2){
            for(int x = 1; x < level[0].length; x = x + 2){
                if(level[y][x].equals("#")){
                    up[Y][X] = Wall.WALL;
                } else {
                    up[Y][X] = Wall.EMPTY;
                }
                X++;
            }
            X = 0;
            Y++;
        }
        printWall(up);
        return setNullEmpty(up);
    }

    private void printWall(Wall[][] wall){
        System.out.println("PRINTING THE WALLS");
        for(int yWall = 0; yWall < wall.length; yWall++) {
            System.out.println();
            for(int xWall = 0; xWall < wall[0].length; xWall++) {
                System.out.print(wall[yWall][xWall] + " ");
            }
            System.out.println();
        }
    }

    public Point getExit(){
        String exitPos = getExitPos();
        String[] strings = exitPos.split(",");
        int x = Integer.parseInt(strings[1]);
        int y = Integer.parseInt(strings[0]);
        return new Point(positionOffset(x), positionOffset(y));
    }

    public void moveTheseus(Direction direction){
        super.moveTheseus(direction);
        inform();
    }

    public int getMoveCount(){
        return super.getMoveCount();
    }

    public void reset(){
        super.reset();
        inform();
    }

    public void undoMove(){}

    public void loadLevel(String level){
        String[][] aLevel = {{}};
        setGameLevel(aLevel);
    }

    public void saveGame(FileOutputStream file){}

    public boolean getWin(){
        return getTheseusWon();
    }

    public boolean getLose(){
        return getGameOver();
    }
}
