package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Wall {

	private int x, y;
	private Canvas wall;

	public Wall(Context context, int x, int y) {
		this.x = x * 40;
		this.y = y * 40;
		
		Bitmap bitmapWall = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
		this.wall = new Canvas(bitmapWall);

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Canvas getWall() {
		return wall;
	}

	public void setWall(Canvas wall) {
		this.wall = wall;
	}
}
