package cmov.bomberman.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import cmov.bomberman.pair.*;
import android.content.Context;
import android.util.Log;

public class LoadMap {
	
	protected final static int LINES=18;
	protected final static int COLUMNS=16;
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
    public static char[][] gridMap = new char[LINES][COLUMNS];
    
    public static boolean[][] gridLayout = new boolean[LINES][COLUMNS];
    
	
	 protected static LevelProperties loadMap(InputStream filename, Context context,int avatar, int maxWidth, int maxHeight) throws IOException {
		 LevelProperties levelProperties = new LevelProperties(PLAYERS,LINES,COLUMNS);
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
	            
	            if(line.startsWith("LN")){
	            	String[] split = line.split("=");
	            	levelProperties.setLevelName(split[1]);
	            }
	            else if(line.startsWith("GD")){
	            	String[] split = line.split("=");
	            	levelProperties.setGameDuration(Integer.valueOf(split[1]));
	            }
	            else if(line.startsWith("ET")){
	               	String[] split = line.split("=");
	            	levelProperties.setExplosionTimeout(Integer.valueOf(split[1]));
	            }
	            else if(line.startsWith("ED")){
	               	String[] split = line.split("=");
	            	levelProperties.setExplosionDuration(Integer.valueOf(split[1]));
	            }
	            else if(line.startsWith("ER")){
	               	String[] split = line.split("=");
	            	levelProperties.setExplosionRange(Integer.valueOf(split[1]));
	            }
	            else if(line.startsWith("RS")){
	               	String[] split = line.split("=");
	            	levelProperties.setRobotSpeed(Integer.valueOf(split[1]));
	            }
	            else if(line.startsWith("PR")){
	               	String[] split = line.split("=");
	            	levelProperties.setPointsPerRobotKilled(Integer.valueOf(split[1]));
	            }
	            else if(line.startsWith("PO")){
	               	String[] split = line.split("=");
	            	levelProperties.setPointsPerOponentKilled(Integer.valueOf(split[1]));
	            }
	            

	            else if (!line.startsWith("!")) {
	                lines.add(line);
	                width = Math.max(width, line.length());
	            }
	           
	        }
	        height = lines.size();
	        
	        for (int i = 0; i < height; i++) {
	            String line = (String) lines.get(i);
	            for (int j = 0; j < width; j++) {
	                if (j < line.length()) {
	                    char ch = line.charAt(j);
	                    gridMap[i][j]=ch;
	                    Log.d("mapa","X="+i+" Y="+j+" CH="+ch);
	                    if(!(ch == '-'))
	                    	gridLayout[i][j]=true;
	                    //player verification
	                    if((ch > 47 )&& (ch < 58)){
	                    	if(ch == '1')
	                    		levelProperties.addPlayer(context, avatar, new Pair(i,j));
	                    }
	                    if(ch == 'W') {
	                    	levelProperties.addWall(context,new Pair(i,j));
	                    }
	                    else if (ch== 'O') levelProperties.addObstacle(context,new Pair(i,j));
//	                    else if (ch == 'R') levelProperties.addRobot();
	                }

	            }
	        }
	        
	        levelProperties.setGridLayout(gridLayout);
	        levelProperties.setGridMap(gridMap);
	        
	        return levelProperties;

	    }
	
}
