package cmov.bomberman.game;

import cmov.bomberman.pair.*;

public class Mapping {

	public static Pair mapToScreen(Pair coordinates){
		int x = (Integer) coordinates.getKey();
		int y = (Integer) coordinates.getValue();
		//Para size = 20
		
		//int newX = (((x*2)+1)*10);
		//int newY = (((y*2)+1)*10);
		
		//Para size = 30
		int newX = ((x*30)+15);
		int newY = (y*30);
		Pair screen = new Pair(newX,newY);
		return screen;
	}
}
