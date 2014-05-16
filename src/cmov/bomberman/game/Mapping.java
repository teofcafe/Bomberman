package cmov.bomberman.game;

import cmov.bomberman.pair.*;

public class Mapping {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Pair mapToScreen(Pair coordinates){
		int x = (Integer) coordinates.getKey();
		int y = (Integer) coordinates.getValue();

		int newX = (((x*2))*10);
		int newY = (((y*2))*10);

		Pair screen = new Pair(newX,newY);
		return screen;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Pair screenToMap(Pair coordinates){
		int y = (Integer) coordinates.getKey();
		int x = (Integer) coordinates.getValue();

		int newX = (x/10)/2;
		int newY = (y/10)/2;

		Pair map = new Pair(newX,newY);
		return map;
	}
}
