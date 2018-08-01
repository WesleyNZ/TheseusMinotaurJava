package nz.ac.wew248.simplethesmin.model;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class MyGame implements ISubject, IGame{
// terrabad model cause i kinda just gave up...
//  please dont look at my shame
//  still have to fix stuff cause it doesnt work right proper yet
    private Wall[][] left;
    private Wall[][] up;
    private Point theseus;
    private Point minotaur;
    private Point exit;
    private int moveCount;
    private ArrayList<Point> thesMoves;
    private ArrayList<Point> minMoves;
    private boolean lose;
    private boolean win;
    private Loader loader;
    private ArrayList<IObserver> aObserver;

    public MyGame() {
        this.lose = false;
        this.win = false;
        this.thesMoves = new ArrayList<Point>();
        this.minMoves = new ArrayList<Point>();
        this.aObserver = new ArrayList<IObserver>();
        this.moveCount = 0;
        this.loader = new Loader();
    }

    public boolean getWin() {
        return win;
    }

    public boolean getLose() {
        return lose;
    }

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

    public void loadLevel(String level) {
    	this.loader.loadFile(level);
    	String[] splitItems = this.loader.splitItems();
    	this.left = loader.setWallSize(splitItems[0]);
    	this.up = loader.setWallSize(splitItems[1]);
    	this.left = this.loader.setWalls(splitItems[0], this.left);
    	this.up = this.loader.setWalls(splitItems[1], this.up);
    	this.theseus = this.loader.setPoint(splitItems[2]);
    	this.minotaur = this.loader.setPoint(splitItems[3]);
    	this.exit = this.loader.setPoint(splitItems[4]);

    		if(!this.thesMoves.isEmpty()) {
    			this.thesMoves.clear();
    			this.minMoves.clear();
    		}
			this.thesMoves.add(new Point(getTheseus().xAxis(), getTheseus().yAxis()));
			this.minMoves.add(new Point(getMinotaur().xAxis(), getMinotaur().yAxis()));
    }
    
    public void saveGame(FileOutputStream file) {
    	String levelString = Stringify.stringifyLevel(this.left, this.up, this.theseus, this.minotaur, this.exit);
    	Saver saver = new Saver(levelString, file);
    	saver.saveFile();
    }

    public void moveTheseus(Direction direction) {
        if(this.checkBoundary(this.getTheseus(), direction) && this.checkMove(this.getTheseus(), direction) && !this.lose && !this.win) {
            this.theseus.update(direction.move());
            this.checkWin();
        }

        moveCount++;
        this.moveMinotaur();
        this.moveMinotaur();
//        this.moveMinotaur();
        checkLose();

        this.thesMoves.add(moveCount, new Point(getTheseus().xAxis(), getTheseus().yAxis()));
        this.minMoves.add(moveCount, new Point(getMinotaur().xAxis(), getMinotaur().yAxis()));


        inform();
    }
    
    public void undoMove() {
        if(this.moveCount != 0) {
            this.thesMoves.remove(this.moveCount);
            this.minMoves.remove(this.moveCount);
            this.moveCount--;
        }
        this.theseus = this.thesMoves.get(this.moveCount);
        this.minotaur = this.minMoves.get(this.moveCount);
        inform();
    }
    
    public void reset() {
    	this.theseus = this.thesMoves.get(0);
		this.minotaur = this.minMoves.get(0);
		this.lose = false;
		this.win = false;
		this.moveCount = 0;
        if(!this.thesMoves.isEmpty()) {
            this.thesMoves.clear();
            this.minMoves.clear();
        }
        this.thesMoves.add(new Point(getTheseus().xAxis(), getTheseus().yAxis()));
        this.minMoves.add(new Point(getMinotaur().xAxis(), getMinotaur().yAxis()));
        inform();
    }

    public void checkMinMove(Direction direction) {
        if(this.checkBoundary(this.getMinotaur(), direction) && this.checkMove(this.getMinotaur(), direction) && !this.lose && !this.win && this.checkExit(direction)) {
        	this.minotaur.update(direction.move());
        }
    }
    
    private boolean checkExit(Direction direction) {
    	int minX = this.minotaur.xAxis() + direction.move().xAxis();
    	int minY = this.minotaur.yAxis() + direction.move().yAxis();
    	if(minX == this.exit.xAxis() && minY == this.exit.yAxis()) {
    		return false;
    	} else {
    		return true;
    	}
    }

    public void moveMinotaur() {
        int thesX = this.theseus.xAxis();
        int thesY = this.theseus.yAxis();
        int minX = this.minotaur.xAxis();
        int minY = this.minotaur.yAxis();
        if (thesX == minX) {
            if(thesY == minY) {
            	checkLose();
            } else if (minY < thesY) {
                this.checkMinMove(Direction.DOWN);
            } else if (minY > thesY){
                this.checkMinMove(Direction.UP);
            }
        } else if (thesX > minX) {
            if(!this.checkMove(this.getMinotaur(), Direction.RIGHT)) {
                if (minY < thesY) {
                    this.checkMinMove(Direction.DOWN);
                } else if (minY > thesY){
                    this.checkMinMove(Direction.UP);
                }
            } else {
                Direction move = Direction.RIGHT;
                this.minotaur.update(move.move());
            }
        } else if (thesX < minX){
            if(!this.checkMove(this.getMinotaur(), Direction.LEFT)) {
                if (minY < thesY) {
                    this.checkMinMove(Direction.DOWN);
                } else if (minY > thesY){
                    this.checkMinMove(Direction.UP);
                }
            } else {
                this.minotaur.update(Direction.LEFT.move());
            }
        }
    }

    private boolean checkMove(Point person, Direction direction) {
        if(direction == Direction.LEFT) {
            if(this.checkLeft(person.xAxis(), person.yAxis())) {
                return true;
            } else {
                return false;
            }
        } else if(direction == Direction.UP) {
            if(this.checkUp(person.xAxis(), person.yAxis())) {
                return true;
            } else {
                return false;
            }
        } else if(direction == Direction.RIGHT) {
            if(this.checkLeft(person.xAxis() + 1, person.yAxis())) {
                return true;
            } else {
                return false;
            }
        } else if(direction == Direction.DOWN) {
            if(this.checkUp(person.xAxis(), person.yAxis() + 1)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkLeft(int x, int y) {
        boolean result = false;
        if(y < this.left.length && x < this.left[0].length) {
            if(this.left[y][x] == Wall.WALL) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    private boolean checkUp(int x, int y) {
        boolean result = false;
        if(y < this.up.length && x < this.up[0].length) {
            if(this.up[y][x] == Wall.WALL) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    private boolean checkBoundary(Point person, Direction direction) {
        int width = this.left[0].length;
        int height = this.left.length;
        Point move = direction.move();
        if(person.xAxis() > width || person.xAxis() + move.xAxis() < 0 ) {
            return false;
        } else if(person.yAxis() > height || person.yAxis() + move.yAxis() < 0) {
            return false;
        } else {
            return true;
        }
    }
    
    private void checkWin() {
    	int thesX = this.theseus.xAxis();
    	int thesY = this.theseus.yAxis();
    	int exitX = this.exit.xAxis();
    	int exitY = this.exit.yAxis();
    	if(thesX == exitX && thesY == exitY) {
    		this.win = true;
    		inform();
    	}
    }

    private void checkLose() {
        int thesX = this.theseus.xAxis();
        int thesY = this.theseus.yAxis();
        int exitX = this.minotaur.xAxis();
        int exitY = this.minotaur.yAxis();
        if(thesX == exitX && thesY == exitY) {
            this.lose = true;
            inform();
        }
    }

	public Point getTheseus() {
		return this.theseus;
	}
	
	public int getMoveCount() {
		return this.moveCount;
	}

	public Point getMinotaur() {
		return this.minotaur;
	}
	
	public Point getExit() {
		return this.exit;
	}
	
	public Wall[][] getUp() {
		return this.up;
	}
	
	public void setUp(int x, int y, Wall type) {
		this.up[y][x] = type;
	}
	
	public Wall[][] getLeft() {
		return this.left;
	}
	
	public void setLeft(int x, int y, Wall type) {
		this.left[y][x] = type;
	}

}
