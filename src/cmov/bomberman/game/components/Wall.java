package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Wall {

	private int x, y;
	private Bitmap bitmap;
	private int width;
	private int height;

	public Wall(Context context, int x, int y) {
		this.x = x;
		this.y = y;
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
	}

	public int getX() {
		return x;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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
