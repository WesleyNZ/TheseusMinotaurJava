package nz.ac.wew248.theseusandtheminotaur;

import java.io.FileOutputStream;

import nz.ac.wew248.theseusandtheminotaur.model.IObserver;
import nz.ac.wew248.theseusandtheminotaur.model.Point;
import nz.ac.wew248.theseusandtheminotaur.model.Wall;

public interface IViewModel {
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
    int getMoveCount();
    void reset();
    void undo();
    void loadLevel(String level);
    void saveGame(FileOutputStream file);
    boolean getWin();
    boolean getLose();
    void pauseMove();
}
