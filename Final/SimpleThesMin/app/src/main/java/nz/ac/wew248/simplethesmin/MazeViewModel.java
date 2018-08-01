package nz.ac.wew248.simplethesmin;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.FileOutputStream;

import nz.ac.wew248.simplethesmin.model.Direction;
import nz.ac.wew248.simplethesmin.model.Graham.GameAdapter;
import nz.ac.wew248.simplethesmin.model.MyGame;

import nz.ac.wew248.simplethesmin.model.IGame;
import nz.ac.wew248.simplethesmin.model.IObserver;
import nz.ac.wew248.simplethesmin.model.Point;
import nz.ac.wew248.simplethesmin.model.Rochelle.GameAdapter2;
import nz.ac.wew248.simplethesmin.model.Wall;

public class MazeViewModel extends AndroidViewModel implements IViewModel {
    private IGame mazeGame;
    private boolean levelLoaded;

//    public void setContext(Context context) {
//        mazeGame.setContext(context);
//    }

    public MazeViewModel(@NonNull Application application) {
        super(application);
        levelLoaded = false;
        setMazeGame();
    }

    public void setMazeGame() {
        if(mazeGame == null) {
            mazeGame = new GameAdapter2();
        }
    }

    public void attach(IObserver observer) {
        mazeGame.attach(observer);
    }

    public Point getTheseus() {
        return mazeGame.getTheseus();
    }

    public Point getMinotaur() {
        return mazeGame.getMinotaur();
    }

    public Wall[][] getLeft() {
        return mazeGame.getLeft();
    }

    public Wall[][] getUp() {
        return mazeGame.getUp();
    }

    public Point getExit() {
        return mazeGame.getExit();
    }

    public void moveLeft() {
        mazeGame.moveTheseus(Direction.LEFT);
    }

    public void moveRight() {
        mazeGame.moveTheseus(Direction.RIGHT);
    }

    public void moveUp() {
        mazeGame.moveTheseus(Direction.UP);
    }

    public void moveDown() {
        mazeGame.moveTheseus(Direction.DOWN);
    }

    public void pauseMove() {
        mazeGame.moveTheseus(Direction.PAUSE);
    }

    public int getMoveCount() {
        return mazeGame.getMoveCount();
    }

    public void reset() {
        mazeGame.reset();
    }

    public void undo() {
        mazeGame.undoMove();
    }

    public void loadLevel(String level) {
        System.out.println(level);
        if(!levelLoaded) {
            mazeGame.loadLevel(level);
            levelLoaded = true;
        }
    }

    public void saveGame(FileOutputStream file) {
        mazeGame.saveGame(file);
    }

    public boolean getWin() {
        return mazeGame.getWin();
    }
    public boolean getLose() {
        return mazeGame.getLose();
    }




}
