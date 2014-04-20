package cmov.bomberman.game;

import cmov.bomberman.pair.*;
import cmov.bomberman.game.LoadMap;

public class Mapping {


	public static Pair mapToScreen(Pair coordinates){
		 int fileWidth=LoadMap.COLUMNS;
		 int fileHeight=LoadMap.LINES;
		System.out.println("COORDENADAS DADAS:" + coordinates.getKey() +" " + coordinates.getValue());
//		System.out.println("SCREEN WIDTH: " + screenWidth);
//		System.out.println("SCREEN HEIGHT: " + screenHeight);
		System.out.println("FILEWIDTH: " + fileWidth);
		System.out.println("FILE HEIGHT: " + fileHeight);

//		int newX = (x * screenHeight)/fileHeight;
//		int newY = (y * screenWidth)/fileWidth;
//
//		int newX = ((fileHeight*100)/screenHeight)*x;
//		int newY = ((fileWidth*100)/screenWidth)*y;
		
	
//		
//		int newX = (screenHeight/fileHeight)*x;
//		int newY = (screenWidth/fileWidth)*y;


		int x = (Integer) coordinates.getKey();
		int y = (Integer) coordinates.getValue();
		int newX = (((x*2)+1)*10);
		int newY = (((y*2)+1)*10);

		Pair screen = new Pair(newX,newY);
		return screen;
	}
}
