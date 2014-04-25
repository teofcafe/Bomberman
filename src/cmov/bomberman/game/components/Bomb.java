package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

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

	public Bomb(Context context, int x, int y) {
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
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

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void draw(Canvas canvas) {
		int srcX = currentFrame * width;
		int srcY = 0; //so ha uma linha
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bitmap, src, dst, null);
		if(currentFrame == 3) 
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
