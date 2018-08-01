package nz.ac.wew248.theseusandtheminotaur;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.io.FileOutputStream;

import nz.ac.wew248.theseusandtheminotaur.model.Direction;
import nz.ac.wew248.theseusandtheminotaur.model.Game;
import nz.ac.wew248.theseusandtheminotaur.model.IGame;
import nz.ac.wew248.theseusandtheminotaur.model.IObserver;
import nz.ac.wew248.theseusandtheminotaur.model.Point;
import nz.ac.wew248.theseusandtheminotaur.model.Wall;

public class MazeViewModel extends AndroidViewModel implements IViewModel {
//    game object is stored using interface
    private IGame mazeGame;
//    check level loaded on view change, vertical to horozontal
    private boolean levelLoaded;

    public MazeViewModel(@NonNull Application application) {
        super(application);
        levelLoaded = false;
        setMazeGame();
    }

    public void setMazeGame() {
//        Set so it doessnt overwrite the game object
        if(mazeGame == null) {
            mazeGame = new Game();
        }
    }

    public void attach(IObserver observer) {
//        WHAT? OBSERVER PATTERN OMG!!
        mazeGame.attach(observer);
    }

    public void pauseMove() {
        mazeGame.moveTheseus(Direction.PAUSE);
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
//        so the level won't be overwritten if it is already set
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
