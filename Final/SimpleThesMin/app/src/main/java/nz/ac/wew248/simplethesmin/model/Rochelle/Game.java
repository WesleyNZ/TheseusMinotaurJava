package nz.ac.wew248.simplethesmin.model.Rochelle;
import nz.ac.wew248.simplethesmin.model.Direction;
import nz.ac.wew248.simplethesmin.model.IGame;
import nz.ac.wew248.simplethesmin.model.ISubject;
import nz.ac.wew248.simplethesmin.model.MyGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    private int moveCount = 0;
    private int moveCountMinotaur = 0;
    private ArrayList<String> theseusPos = new ArrayList<String>();
    private ArrayList<String> minotaurPos = new ArrayList<String>();
    private List<Direction> theseusMoves = new ArrayList<Direction>(); // for undo / history
    private List<Direction> minotaurMoves = new ArrayList<Direction>(); // for undo / history
    private String[][] gameLevel = {{}};
    private Boolean gameOver = false;
    private Boolean theseusWon = false;

    public void setGameLevel(String[][] newLevel) {
        String[][] defaultGame = {
                {"#", "#", "#", "#", "#", "#", "#", "/", "/"},
                {"#", "-", "/", "M", "/", "-", "#", "/", "/"},
                {"#", "/", "/", "#", "#", "/", "#", "#", "/"},
                {"#", "-", "/", "-", "#", "-", "/", "E", "/"},
                {"#", "/", "/", "#", "#", "/", "#", "#", "/"},
                {"#", "-", "/", "T", "/", "-", "#", "/", "/"},
                {"#", "#", "#", "#", "#", "#", "#", "/", "/"} };
        String[][] empty = {{}};
        if(Arrays.deepEquals(newLevel, empty)) {
            gameLevel = defaultGame;
        }
        else {
            gameLevel = newLevel;
        }
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public Boolean getTheseusWon() {
        return theseusWon;
    }

    public String[][] getLevel() {
        return gameLevel;
    }

    public void moveTheseus(Direction direction) {
        String value = this.getTheseusPos(); //get T position
        String[] splitT = value.split(",");
        int yPos = Integer.parseInt(splitT[(0)]);
        int xPos = Integer.parseInt(splitT[(splitT.length - 1)]);
        int toX = 0; // destination
        int toY = 0;
        int betweenX_PosAndDest = 0; // before destination, e.g. wall or empty wall space
        int betweenY_PosAndDest = 0;

        switch(direction) {
            case UP:
                betweenX_PosAndDest = xPos;
                betweenY_PosAndDest = yPos - 1;
                toX = xPos;
                toY = yPos - 2;
                break;
            case DOWN:
                betweenX_PosAndDest = xPos;
                betweenY_PosAndDest = yPos + 1;
                toX = xPos;
                toY = yPos + 2;
                break;
            case LEFT:
                betweenX_PosAndDest = xPos - 1;
                betweenY_PosAndDest = yPos;
                toX = xPos - 2;
                toY = yPos;
                break;
            case RIGHT:
                betweenX_PosAndDest = xPos + 1;
                betweenY_PosAndDest = yPos;
                toX = xPos + 2;
                toY = yPos;
                break;
            case NONE:
                this.moveMinotaur();
                moveCount++;
                theseusMoves.add(direction);
                theseusPos.add(String.valueOf(yPos) + "," + String.valueOf(xPos));
                break;
            default:
                break;
        }

        if (toX < 1 || toY < 1) {
            // do nothing
        }
        else if (toX > gameLevel[0].length - 1 || toY > gameLevel.length - 1) {
            // do nothing
        }
        else {
            System.out.println(gameLevel[0].length); //col x
            System.out.println(gameLevel.length); //row y
            if(this.checkTheseusMove(betweenX_PosAndDest, betweenY_PosAndDest, toX, toY)) {
                moveCount++;
                theseusMoves.add(direction);
                theseusPos.add(String.valueOf(yPos) + "," + String.valueOf(xPos));

                if(!gameLevel[toY][toX].equals("E")) {
                    this.move("T", yPos, xPos, toY, toX);
                    this.moveMinotaur();
                }
                else {
                    this.move("T", yPos, xPos, toY, toX);
                    System.out.println("YAY YOU WON!");
                    gameOver = true;
                    theseusWon = true;
                }

            }
            else if(gameLevel[toY][toX].equals("M")) {
                System.out.println("OH NO YOU LOST! Why did you move onto Minotaur!?");
                this.move("M", yPos, xPos, toY, toX);
                gameOver = true;
                theseusWon = false;
            }
        }

    }

    private boolean checkTheseusMove(int betweenX, int betweenY, int toX, int toY) {
        boolean result = false;
        if(gameLevel[betweenY][betweenX].equals("/")) {
            if (toX <= gameLevel[0].length) { // so doesn't try fall off map
                if(gameLevel[toY][toX].equals("-") || gameLevel[toY][toX].equals("E")) {
                    result = true;
                }
            }
        }

        return result;
    }

    public String getTheseusPos() {
        int y = 0;
        int x = 0;
        for (int i = 0; i < gameLevel.length; i++) {
            for (int j = 0; j < gameLevel[i].length; j++) {
                if (gameLevel[i][j].equals("T")) {
                    y = i;
                    x = j;
                }
            }
        }
        return (y + "," + x);
    }

    public String getMinotaurPos() {
        int y = 0;
        int x = 0;
        for (int i = 0; i < gameLevel.length; i++) {
            for (int j = 0; j < gameLevel[i].length; j++) {
                if (gameLevel[i][j].equals("M")) {
                    y = i;
                    x = j;
                }
            }
        }
        return (y + "," + x);
    }

    public String getExitPos() {
        int y = 0;
        int x = 0;
        for (int i = 0; i < gameLevel.length; i++) {
            for (int j = 0; j < gameLevel[i].length; j++) {
                if (gameLevel[i][j].equals("E")) {
                    y = i;
                    x = j;
                }
            }
        }
        return (y + "," + x);
    }

    public void moveMinotaur() {
        int count = 0;
        int yPosM;
        int xPosM;

        String minotaur = this.getMinotaurPos();
        String[] splitM = minotaur.split(",");
        yPosM = Integer.parseInt(splitM[(0)]);
        xPosM = Integer.parseInt(splitM[(splitM.length - 1)]);

        minotaurPos.add(String.valueOf(yPosM) + "," + String.valueOf(xPosM));

        while(count < 2) { //minotaur can move twice
            String theseus = this.getTheseusPos(); //get T position
            minotaur = this.getMinotaurPos();
            String[] splitT = theseus.split(",");
            int yPosT = Integer.parseInt(splitT[(0)]);
            int xPosT = Integer.parseInt(splitT[(splitT.length - 1)]);
            splitM = minotaur.split(",");
            yPosM = Integer.parseInt(splitM[(0)]);
            xPosM = Integer.parseInt(splitM[(splitM.length - 1)]);
            int toX = 0; // destination
            int toY = 0;
            int betweenX_PosAndDest = 0; // before destination, e.g. wall or empty wall space
            int betweenY_PosAndDest = 0;
            Direction direction = null; // to know what way to go

            if(xPosT < xPosM) {
                direction = Direction.LEFT;
            }
            else if(xPosT > xPosM) {
                direction = Direction.RIGHT;
            }
            else if(yPosT < yPosM) {
                direction = Direction.UP;
            }
            else if(yPosT > yPosM) {
                direction = Direction.DOWN;
            }
            else {
                direction = Direction.NONE;
            }

            switch(direction) {
                case UP:
                    betweenX_PosAndDest = xPosM;
                    betweenY_PosAndDest = yPosM - 1;
                    toX = xPosM;
                    toY = yPosM  - 2;
                    break;
                case DOWN:
                    betweenX_PosAndDest = xPosM;
                    betweenY_PosAndDest = yPosM + 1;
                    toX = xPosM;
                    toY = yPosM + 2;
                    break;
                case LEFT:
                    betweenX_PosAndDest = xPosM - 1;
                    betweenY_PosAndDest = yPosM;
                    toX = xPosM - 2;
                    toY = yPosM;
                    break;
                case RIGHT:
                    betweenX_PosAndDest = xPosM + 1;
                    betweenY_PosAndDest = yPosM;
                    toX = xPosM + 2;
                    toY = yPosM;
                    break;
                case NONE:
                    break;
                default:
                    break;
            }

            if(this.checkMinotaurMove(betweenX_PosAndDest, betweenY_PosAndDest, toX, toY)) {
                this.move("M", yPosM, xPosM, toY, toX);
                minotaurMoves.add(direction);
            }
            else {
                if(yPosT < yPosM) {
                    direction = Direction.UP;
                }
                else if(yPosT > yPosM) {
                    direction = Direction.DOWN;
                }
                else if(xPosT < xPosM) {
                    direction = Direction.LEFT;
                }
                else if(xPosT > xPosM) {
                    direction = Direction.RIGHT;
                }

                switch(direction) {
                    case UP:
                        betweenX_PosAndDest = xPosM;
                        betweenY_PosAndDest = yPosM - 1;
                        toX = xPosM;
                        toY = yPosM  - 2;
                        break;
                    case DOWN:
                        betweenX_PosAndDest = xPosM;
                        betweenY_PosAndDest = yPosM + 1;
                        toX = xPosM;
                        toY = yPosM + 2;
                        break;
                    case LEFT:
                        betweenX_PosAndDest = xPosM - 1;
                        betweenY_PosAndDest = yPosM;
                        toX = xPosM - 2;
                        toY = yPosM;
                        break;
                    case RIGHT:
                        betweenX_PosAndDest = xPosM + 1;
                        betweenY_PosAndDest = yPosM;
                        toX = xPosM + 2;
                        toY = yPosM;
                        break;
                    default:
                        break;
                }

                if(this.checkMinotaurMove(betweenX_PosAndDest, betweenY_PosAndDest, toX, toY)) {
                    this.move("M", yPosM, xPosM, toY, toX);
                    minotaurMoves.add(direction);
                }
            }
            count++;
        }
    }

    private void move(String player, int yPos, int xPos, int toY, int toX) {
        if(player.equals("T")) {
            gameLevel[yPos][xPos] = "-";
            gameLevel[toY][toX] = "T";
        }
        else if(player.equals("M")) {
            gameLevel[yPos][xPos] = "-";
            gameLevel[toY][toX] = "M";
        }
    }

    private boolean checkMinotaurMove(int betweenX, int betweenY, int toX, int toY) {
        boolean result = false;
        if(gameLevel[betweenY][betweenX].equals("/")) {
            if(gameLevel[toY][toX].equals("-")) {
                result = true;
            }
            else if(gameLevel[toY][toX].equals("T")) {
                System.out.println("OH NO YOU LOST! Minotaur caught you!");
                gameOver = true;
                theseusWon = false;
                result = true;
            }
        }

        return result;
    }

    public void undo() {
        String value = this.getTheseusPos(); //get T position
        int yPos = Integer.parseInt(value.substring(0, value.length() / 2)); // get y pos
        int xPos = Integer.parseInt(value.substring(value.length() - 1)); // get x pos
        int toX = 0; // destination
        int toY = 0;

        if (theseusPos.size() < 1) {

        }
        else {
            String m = theseusPos.get(theseusPos.size() - 1);
            String[] parts = m.split(",");
            toY = Integer.parseInt(parts[0]);
            toX = Integer.parseInt(parts[1]);

            this.move("T", yPos, xPos, toY, toX);
            System.out.println(theseusPos);
            moveCount--;

            this.undoMinotaur();

            theseusPos.remove(theseusPos.size() - 1);
        }
    }

    private void undoMinotaur() {
        int count = 0;
        int toX = 0; // destination
        int toY = 0;
        int xPosM = 0;
        int yPosM = 0;

        String minotaur = this.getMinotaurPos();
        xPosM = Integer.parseInt(minotaur.substring(minotaur.length() - 1));
        yPosM = Integer.parseInt(minotaur.substring(0, minotaur.length() / 2));

        if (minotaurPos.size() < 1) {

        }
        else {
            String m = minotaurPos.get(minotaurPos.size() - 1);
            String[] parts = m.split(",");
            toY = Integer.parseInt(parts[0]);
            toX = Integer.parseInt(parts[1]);

            this.move("M", yPosM, xPosM, toY, toX);
            minotaurPos.remove(minotaurPos.size() - 1);
        }
    }

    public void reset() {
//		String[][] empty = {{}};
//		this.setGameLevel(file);
        this.moveCount = 0;
        theseusPos = new ArrayList<String>();
        minotaurPos = new ArrayList<String>();
    }

    public int getMoveCount() {
        return moveCount;
    }

    public String getMoveHistory() {
        String moveHistory = "";
        int length = theseusMoves.size() - 1;

        for(int i = length; i >= 0; i--) {
            moveHistory += theseusMoves.get(i).toString() + " ";
        }

        return moveHistory;
    }
}
