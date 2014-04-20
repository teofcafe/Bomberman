package cmov.bomberman.game;

import cmov.bomberman.pair.*;

public class Mapping {

	public static Pair mapToScreen(Pair coordinates,int screenWidth,int screenHeight, int fileWidth, int fileHeight){
		int x = (Integer) coordinates.getKey();
		int y = (Integer) coordinates.getValue();
		int newX = (((x*2)+1)*10);
		int newY = (((y*2)+1)*10);
		Pair screen = new Pair(newX,newY);
		return screen;
	}
}
