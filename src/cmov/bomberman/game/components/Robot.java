package cmov.bomberman.game.components;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Robot {

	private int x, y;
	private Bitmap bitmap;
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private int currentFrame = 1;
	private final static float VELOCITY = 2;
	private int direction = 2;
	private int width;
	private int height;

	public Robot(Bitmap bitmap, int x, int y) {
		this.x = x;
		this.y = y;
		this.bitmap = bitmap;
		this.width = bitmap.getWidth() / BMP_COLUMNS;
		this.height = bitmap.getHeight() / BMP_ROWS;
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
		int srcX = currentFrame * width;
		//aqui muda a direction 
		int srcY = direction * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bitmap, src, dst, null);
	}

	
	public void checkDirection(int distX, int distY){

		//Aproxima.se do player pelo eixo x ou y
		if(Math.abs(distX) <=5 || Math.abs(distY) <=5){

			if(Math.abs(distY) <= 5 && Math.abs(distX) >= 5 && distX <0)
				moveRight();

			if(Math.abs(distY) <= 5 && Math.abs(distX) >= 5 && distX >0)
				moveLeft();

			if(Math.abs(distX) <= 5 && Math.abs(distY) >= 5 && distY >0)
				moveUp();

			if(Math.abs(distX) <= 5 && Math.abs(distY) >= 5 && distY <0)
				moveDown();
		}

		//Escolhe o eixo com menos distancia ate ao player e posiciona.se la
		else{
			if (Math.abs(distX) < Math.abs(distY)){
				if(distX > 0)
					moveLeft();
				else
					moveRight();
			}
			else
				if(distY > 0)
					moveUp();
				else
					moveDown();
		}
	}

	public void update(int playerX, int playerY) {
		int distX = this.getX() - playerX;
		int distY = this.getY() - playerY;
		//heuristic #1
		checkDirection(distX, distY);
	}


	public void moveUp() {
		direction = 3;
		y -= 1 * VELOCITY;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void moveLeft() {
		direction = 1;
		x -= 1 * VELOCITY;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void moveRight() {
		direction = 2;
		x += 1 * VELOCITY;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void moveDown() {
		direction = 0;	
		y +=1 * VELOCITY;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

}
