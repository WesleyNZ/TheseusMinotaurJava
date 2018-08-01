package nz.ac.wew248.theseusandtheminotaur.model;


import java.io.FileOutputStream;

public interface IGame extends ISubject{
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
