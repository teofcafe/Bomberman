package cmov.bomberman.game;

import cmov.bomberman.pair.*;

public class Mapping {


	public static Pair mapToScreen(Pair coordinates,int screenWidth,int screenHeight, int fileWidth, int fileHeight){
		System.out.println("COORDENADAS DADAS:" + coordinates.getKey() +" " + coordinates.getValue());
		System.out.println("SCREEN WIDTH: " + screenWidth);
		System.out.println("SCREEN HEIGHT: " + screenHeight);
		System.out.println("FILEWIDTH: " + fileWidth);
		System.out.println("FILE HEIGHT: " + fileHeight);
		int x = (Integer) coordinates.getKey();
		int y = (Integer) coordinates.getValue();
		
//		int newX = (x * screenHeight)/fileHeight;
//		int newY = (y * screenWidth)/fileWidth;

		int newX = ((fileHeight*100)/screenHeight)*x;
		int newY = ((fileWidth*100)/screenWidth)*y;
		Pair screen = new Pair(newX,newY);
		
		return screen;
	}
}
