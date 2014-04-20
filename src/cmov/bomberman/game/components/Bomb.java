package cmov.bomberman.game.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

public class Bomb {

	private Bitmap bitmap;	// the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private static final int BMP_COLUMNS = 4;
	private int currentFrame = -1; 
	private int width;
	private int height;
	private final static float VELOCITY = 1;
	private boolean exploded = false;

	public Bomb(Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.width = bitmap.getWidth() / BMP_COLUMNS;
		this.height = bitmap.getHeight();
	}

	public boolean isExploded() {
		return exploded;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void draw(Canvas canvas) {
		int srcX = currentFrame * width;
		int srcY = 0; //so ha uma linha
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bitmap, src, dst, null);
		if(currentFrame == 2) 
			exploded = true;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void update() {
		currentFrame = ++currentFrame % BMP_COLUMNS; //update
	}
}
