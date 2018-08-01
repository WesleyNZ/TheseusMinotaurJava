package nz.ac.wew248.simplethesmin.model;
import java.io.FileOutputStream;

public class Saver {
	private FileOutputStream file;
	private String level;
	public Saver(String levelString, FileOutputStream aFile) {
		this.file = aFile;
		this.level = levelString;
	}
	
	public void saveFile() {
		FileOutputStream f = file;
		try {
			f.write(level.getBytes());
			f.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
