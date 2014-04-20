package cmov.bomberman.game;

import cmov.bomberman.pair.*;

public class Mapping {

	public static Pair mapToScreen(Pair coordinates,int screenWidth,int screenHeight, int fileWidth, int fileHeight){
		int x = (Integer) coordinates.getKey();
		int y = (Integer) coordinates.getValue();
		//Para size = 20
		
		//int newX = (((x*2)+1)*10);
		//int newY = (((y*2)+1)*10);
		
		//Para size = 30
		int newX = ((x*30)+25);
		int newY = ((y*30)+25);
		Pair screen = new Pair(newX,newY);
		return screen;
	}
}
