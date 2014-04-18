package cmov.bomberman.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cmov.bomberman.menu.R;

import android.util.Log;

public class LoadMap {
	
	protected final static int LINES=13;
	protected final static int COLUMNS=19;
	private final static int PLAYERS=3; 
	
    //level properties
    String levelName = "";
    int gameDuration;
    int explosionTimeout;
    int explosionDuration;
    int explosionRange;
    
    //cells per second
    int robotSpeed;
    int pointsPerRobotKilled;
    int pointsPerOponentKilled;
    
    
    int numberOfwalls;
    int numberOfObstacles;
    int numberOfRobots;
    
    //max 3 players
    int[] playersPositions = new int[PLAYERS];
    
    //grid layout
    public static char[][] gridLayout = new char[LINES][COLUMNS];
	
	
	 protected static void loadMap(InputStream filename) throws IOException {
	        ArrayList lines = new ArrayList();
	        int width = 0;
	        int height = 0;
	        
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(filename));
	        while (true) {
	            String line = reader.readLine();
	            // no more lines to read
	            if (line == null) {
	                reader.close();
	                break;
	            }

	            if (!line.startsWith("!")) {
	                lines.add(line);
	                width = Math.max(width, line.length());

	            }
	            else {
	            	//read parameteres
	            }
	        }
	        height = lines.size();
	        
	        for (int i = 0; i < height; i++) {
	            String line = (String) lines.get(i);
	            for (int j = 0; j < width; j++) {

	                if (j < line.length()) {
	                    char ch = line.charAt(j);
	                    gridLayout[i][j]=ch;
	                }

	            }
	        }

	    }
	 
	
	 
	
}
