package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import cmov.bomberman.pair.Pair;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Obstacle {

	private int x, y;
	private Bitmap bitmap;

	public Obstacle(Context context, Pair coordinates) {
//		this.x =x coordinates.get;
//		this.y = y;
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.obstacle);
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

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
