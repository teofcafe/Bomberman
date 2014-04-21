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
		if(x == xSource) {
			if(y == ySource) {
				Log.d("bomba","1");
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_center);
			}
			else if(ySource == y + range) {
				Log.d("bomba","2");
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_top);
			}
			else if(ySource == y - range) {
				Log.d("bomba","3");
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_bottom);
			}
			else {
				Log.d("bomba","4");
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_top_bottom_fill);
			}
		}
		
		else if(y == ySource) {
			if(xSource == x + range) {
				Log.d("bomba","5");
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_left);
			}
			else if(xSource == x - range) {
				Log.d("bomba","6");
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_right);
			}
			else  {
				Log.d("bomba","7 -> x - range: " + (x - range) + " | x + range: " + (x + range));
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_left_right_fill);
			}
		}
		
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
