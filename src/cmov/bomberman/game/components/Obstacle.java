package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import cmov.bomberman.pair.Pair;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Obstacle {

	private int x, y;
	private Bitmap bitmap;
	private int width;
	private int height;

	public Obstacle(Context context, Pair coordinates) {
		this.y = (Integer) coordinates.getKey();
		this.x = (Integer) coordinates.getValue();
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.obstacle);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
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
	public void draw(Canvas canvas) {
//		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
		canvas.drawBitmap(bitmap, x, y, null);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
