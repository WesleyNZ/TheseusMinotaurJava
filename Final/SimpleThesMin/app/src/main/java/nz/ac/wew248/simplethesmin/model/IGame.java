package nz.ac.wew248.simplethesmin.model;


import android.content.Context;

import java.io.FileOutputStream;

public interface IGame extends ISubject{
//    void setContext(Context context);
    Point getTheseus();
    Point getMinotaur();
    Wall[][] getLeft();
    Wall[][] getUp();
    Point getExit();
    void moveTheseus(Direction direction);
    int getMoveCount();
    void reset();
    void undoMove();
    void loadLevel(String level);
    void saveGame(FileOutputStream file);
    boolean getWin();
    boolean getLose();
}
