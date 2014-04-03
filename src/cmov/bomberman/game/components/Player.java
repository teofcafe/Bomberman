package cmov.bomberman.game.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player {

	private Bitmap bitmap;	// the actual bitmap
	private int x;			// the X coordinate
	private int y;			// the Y coordinate
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private int currentFrame = 1;
	private int width;
	private int height;
	int direction = 2;
	
	public Player(Bitmap bitmap, int x, int y){
		this.bitmap=bitmap;
		this.x=x;
		this.y=y;
		this.width = bitmap.getWidth() / BMP_COLUMNS;
		this.height = bitmap.getHeight() / BMP_ROWS;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
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
	
	// direction = 0 up, 1 left, 2 down, 3 right,
	// frame = 3 back, 1 left, 0 front, 2 right
	public void draw(Canvas canvas) {
		currentFrame = ++currentFrame % BMP_COLUMNS; //update
		
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

}
