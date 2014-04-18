package cmov.bomberman.game.components;

import android.graphics.Bitmap;
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
	private boolean touched = false;
	private final static float VELOCITY = 3;

	public Player(Bitmap bitmap, int x, int y){
		this.bitmap=bitmap;
		this.x=x;
		this.y=y;
		this.width = bitmap.getWidth() / BMP_COLUMNS;
		this.height = bitmap.getHeight() / BMP_ROWS;
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
		if (touched) 
			this.x = x;		
	}
	public void setY(int y) {
		if(touched)
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
		this.touched = touched;
	}
	
	public boolean getTouched() {
		return this.touched;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void update() {
		currentFrame = ++currentFrame % BMP_COLUMNS; //update
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

