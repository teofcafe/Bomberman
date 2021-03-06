package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Explosion {

	private Bitmap bitmap;
	private int x;
	private int y;
	private static final int BMP_COLUMNS = 4;
	private int currentFrame = -1; 
	private int width;
	private int height;
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
		if(x == xSource)
			if(y == ySource) 
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_center);
			else if(ySource == y + range)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_top);

			else if(ySource == y - range) 
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_bottom);
			else
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_top_bottom_fill);
		else if(y == ySource) 
			if(xSource == x + range)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_left);
			else if(xSource == x - range) 
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_right);
			else
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_left_right_fill);

		this.x = x;
		this.y = y;
		this.width = this.bitmap.getWidth() / BMP_COLUMNS;
		this.height = this.bitmap.getHeight();
	}

	public void draw(Canvas canvas) {
		int srcX = currentFrame * width;
		int srcY = 0;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bitmap, src, dst, null);
		if(currentFrame == 3) 
			alive = false;
	}

	public void update() {
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

}
