package cmov.bomberman.game;

import cmov.bomberman.pair.*;

public class Mapping {

	public static Pair mapToScreen(Pair coordinates){
		int x = (Integer) coordinates.getKey();
		int y = (Integer) coordinates.getValue();
		System.out.println("Coordenada Original: " + x + " , " + y);
		//Para size = 20
		
		int newX = (((x*2))*10);
		int newY = (((y*2))*10);
//		
		//Para size = 30

//		int newX = ((x*30)+25);
//		int newY = ((y*30)+25);
		System.out.println("Coordenada Original: " + newX + " , " + newY);

//		int newX = ((x*30)+15);
//		int newY = (y*30);

		Pair screen = new Pair(newX,newY);
		return screen;
	}
}
