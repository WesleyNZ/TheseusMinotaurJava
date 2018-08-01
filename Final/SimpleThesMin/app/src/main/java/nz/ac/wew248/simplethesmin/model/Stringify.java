package nz.ac.wew248.simplethesmin.model;

public class Stringify {

	public static String stringifyWall(Wall[][] wall) {
		String theWall = null;
		theWall = "";
		for(int y = 0; y < wall.length; y++) {
			for(int x = 0; x < wall[0].length; x++) {
				if(wall[y][x] == Wall.WALL) {
					theWall += "x";
				} else if(wall[y][x] == Wall.EMPTY) {
					theWall += "o";
				}
			}
			if(y < wall.length - 1) {
				theWall += ",";
			}
		}
		return theWall;
	}
	
	public static String stringifyPoint(Point point) {
		return point.xAxis() + "," + point.yAxis();
	}
	
	public static String join(String...strings) {
		String result = "";
		for(int i = 0; i < strings.length; i++) {
			result += strings[i];
			if(i < (strings.length - 1)) {
				result += ";";
			}
		}
		return result;
	}
	
	public static String stringifyLevel(Wall[][] left, Wall[][] up, Point thes, Point min, Point exit) {
		String result = "";
		String theLeft = Stringify.stringifyWall(left);
		String theUp = Stringify.stringifyWall(up);
		String theThes = Stringify.stringifyPoint(thes);
		String theMin = Stringify.stringifyPoint(min);
		String theExit = Stringify.stringifyPoint(exit);
		result = Stringify.join(theLeft, theUp, theThes, theMin, theExit);
		return result;
	}
	
	
	
}
