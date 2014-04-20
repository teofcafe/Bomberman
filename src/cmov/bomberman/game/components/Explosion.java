package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Explosion {

	private Bitmap bitmap;	// the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private static final int BMP_COLUMNS = 4;
	private int currentFrame = -1; 
	private int width;
	private int height;
	private final static float VELOCITY = 1;
	private boolean alive = true;

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean isAlive() {
		return alive;
	}

	public Explosion(Context context, int x, int y, int xSource, int ySource, int range) {
		Log.d("bomba","x: " + x + " y: " + y + " xSource: " + xSource + " ySource: " + ySource);
		if(x == xSource)
			if(y == ySource)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_center);
			else if(y == y + range)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_top);
			else if(y == y - range)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_bottom);
			else 
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_top_bottom_fill);
		else if(y == ySource)
			if(x == x + range)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_right);
			else if(x == x - range)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_left);
			else 
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_left_right_fill);

		this.x = x;
		this.y = y;
		this.width = this.bitmap.getWidth() / BMP_COLUMNS;
		this.height = this.bitmap.getHeight();
		
		
	}
	
	public void draw(Canvas canvas) {
		int srcX = currentFrame * width;
		int srcY = 0; //so ha uma linha
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bitmap, src, dst, null);
		if(currentFrame == 3) 
			alive = false;
	}

	public void update() {
		currentFrame = ++currentFrame % BMP_COLUMNS; //update
	}

}
