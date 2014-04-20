package cmov.bomberman.game.components;

import cmov.bomberman.menu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Player {

	private Bitmap bitmap;	// the actual bitmap
	private int x;			// the X coordinate
	private int y;			// the Y coordinate
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private int currentFrame = 1;
	private int width;
	private int height;
	private int direction = 0;		
	private boolean keyTouched = false;
	private final static float VELOCITY = 3;
	private boolean paused;

	public Player(Context context, int avatar, int x, int y){

		switch (avatar) {
		case 0:
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_1);
			break;
		case 1:
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_2);
			break;
		case 2:
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_3);
			break;
		case 3:
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_4);
			break;
		case 4:
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_5);
			break;
		case 5:
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_6);
			break;
		}

		this.x=x;
		this.y=y;
		this.width = bitmap.getWidth() / BMP_COLUMNS;
		this.height = bitmap.getHeight() / BMP_ROWS;
		this.paused = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setX(int x) {
		if (keyTouched) 
			this.x = x;		
	}
	public void setY(int y) {
		if(keyTouched)
			this.y = y;
	}

	public void draw(Canvas canvas) {
		int srcX = currentFrame * width;
		//aqui muda a direction 
		int srcY = direction * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bitmap, src, dst, null);
	}

	public void setDirection(int animation) {
		this.direction = animation;
	}

	public int getDirection() {
		return direction;
	}

	public void setTouched(boolean touched) {
		this.keyTouched = touched;
	}

	public boolean isKeyTouched() {
		return this.keyTouched;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void update() {
		currentFrame = ++currentFrame % BMP_COLUMNS; //update

		switch (getDirection()) {
		case 0:
			moveDown();
			break;
		case 1:
			moveLeft();
			break;
		case 2:
			moveRight();
			break;
		case 3:
			moveUp();
			break;
		}	
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void setPaused() {
		this.paused = (!this.paused);
	}

	public void moveDown() {
		y += 1 * VELOCITY;
	}

	public void moveLeft() {
		x -= 1 * VELOCITY;
	}

	public void moveRight() {
		x += 1 * VELOCITY;		
	}

	public void moveUp() {
		y -=1 * VELOCITY;		
	}
}

