package nz.ac.wew248.simplethesmin;

import android.content.Context;

import java.io.FileOutputStream;

import nz.ac.wew248.simplethesmin.model.IObserver;
import nz.ac.wew248.simplethesmin.model.Point;
import nz.ac.wew248.simplethesmin.model.Wall;

public interface IViewModel {
//    void setContext(Context context);
    void setMazeGame();
    void attach(IObserver observer);
    Point getTheseus();
    Point getMinotaur();
    Wall[][] getLeft();
    Wall[][] getUp();
    Point getExit();
    void moveLeft();
    void moveRight();
    void moveUp();
    void moveDown();
    void pauseMove();
    int getMoveCount();
    void reset();
    void undo();
    void loadLevel(String level);
    void saveGame(FileOutputStream file);
    boolean getWin();
    boolean getLose();
}
