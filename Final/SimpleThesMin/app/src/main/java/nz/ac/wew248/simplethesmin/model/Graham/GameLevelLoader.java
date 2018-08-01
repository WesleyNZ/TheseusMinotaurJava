package nz.ac.wew248.simplethesmin.model.Graham;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.content.Context;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.Hashtable;

public class GameLevelLoader implements ILoader {
    private String mainSeperator = ";";
    private Boolean fileFound = true;

    private boolean isValidLine(String newLine) {
        boolean result = true;
        String line = newLine;

        String[] shouldContain = {"U=", "L=", "M=", "T=", "E="};

        for (String stringToMatch : shouldContain) {
            if (!line.contains(stringToMatch)) {
                result = false;
                break;
            }
        }

        return result;
    }

    private String[] splitString(String toSplit, String seperator) {
        return toSplit.split(seperator);
    }

    private String washData(String data) {
        return data.replaceAll("[:]", "");
    }

    private Dictionary<String, String> extractLevelDataFromLine(String newLineOfData) {
        String[] lineOfData = splitString(newLineOfData, mainSeperator);

        Dictionary<String, String> levelData = new Hashtable<String, String>();

        for (String chunkOfData : lineOfData) {
            String dataHeader = chunkOfData.substring(0, 1);
            String dataText = chunkOfData.substring(2);

            dataText = washData(dataText);

            levelData.put(dataHeader, dataText);
        }

        return levelData;
    }

    public Dictionary<String, String> loadLineFromFile(Context context, String theFilename) {
        String fileName = theFilename;
        Dictionary<String, String> levelData = new Hashtable<String, String>();
        String fileContents = "";

        try {
            InputStream inputStream = context.getAssets().open("levels/" + fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader br = new BufferedReader(inputStreamReader);

            fileContents = br.readLine();

            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
            return null;
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            return null;
        }

        if (fileFound) {
            levelData = new Hashtable<String, String>();
            fileContents = fileContents.replaceAll("\\s+", ""); // Remove all spaces
            isValidLine(fileContents);

            levelData = extractLevelDataFromLine(fileContents);
        }

        return levelData;
    }

    public Dictionary<String, String> loadLineFile(String fileData) {
        Dictionary<String, String> levelData = new Hashtable<String, String>();
        String fileContents = fileData;
        levelData = new Hashtable<String, String>();
        fileContents = fileContents.replaceAll("\\s+", ""); // Remove all spaces
        isValidLine(fileContents);
        System.out.println("GAME LEVEL DATA STUFF THINGY");
        System.out.println(fileContents);


        levelData = extractLevelDataFromLine(fileContents);

        return levelData;
    }


}