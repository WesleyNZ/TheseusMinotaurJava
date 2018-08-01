package nz.ac.wew248.simplethesmin.model;
import android.content.Context;

public class Loader {
	private String file;
	private String data;
	private Context context;
	
	public void loadFile(String level) {
		this.data = level;
	}
	
	public String[] splitItems() {
		System.out.println(this.data);
		String[] items = this.data.split(";");
		return items;
	}
	
	public Wall[][] setWallSize(String item) {
		String[] itemSplit = item.split(",");
		Wall[][] theWall = new Wall[itemSplit.length][itemSplit[0].length()];
		return theWall;
	}
	
	public Wall[][] setWalls(String wallString, Wall[][] theWalls) {
		String[] itemSplit = wallString.split(",");
		for(int y = 0; y < itemSplit.length; y++) {
			for(int x = 0; x < itemSplit[y].length(); x++) {
				if(itemSplit[y].charAt(x) == 'x') {
					theWalls[y][x] = Wall.WALL;
				} else if(itemSplit[y].charAt(x) == 'o') {
					theWalls[y][x] = Wall.EMPTY;
				}
			}
		}
		return theWalls;
	}
	
	public Point setPoint(String item) {
		String[] itemSplit = item.split(",");
		Point thePoint = new Point(Integer.parseInt(itemSplit[0]), Integer.parseInt(itemSplit[1]));
		return thePoint;
	}
	
	public String getData() {
		return this.data;
	}
	
	public String getFile() {
		return this.file;
	}
}
