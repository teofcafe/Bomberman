package cmov.bomberman.game.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Wall {

	private int x, y;
	private Bitmap bitmap;

	public Wall(Bitmap bitmap, int x, int y) {
		this.x = x;
		this.y = y;
		this.bitmap = bitmap;
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
}
