package nz.ac.wew248.theseusandtheminotaur.model;
import android.content.Context;

public class Loader {
	private String file;
	private String data;
	private Context context;
	
	public void loadFile(String level) {
		this.data = level;
	}

//	public void setContext(Context aContext) {
//		context = aContext;
//	}
	
//	public void readFile() {
//		final String originalFileSrc = this.file;
//		StringBuilder fileContent = new StringBuilder();
//		try {
//			InputStream inputStream = context.getAssets().open(originalFileSrc);
//			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//			String line;
//			while ((line = bufferedReader.readLine()) != null) {
//				fileContent.append(line);
//			}
//			bufferedReader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		this.data = fileContent.toString();
//	}
	
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
