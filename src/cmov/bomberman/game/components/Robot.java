package cmov.bomberman.game.components;


import java.util.Random;
import cmov.bomberman.game.LevelProperties;
import cmov.bomberman.menu.R;
import cmov.bomberman.pair.Pair;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Robot {

	private int x, y;
	private Bitmap bitmap;
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private int currentFrame = 1;
	private final static float VELOCITY = 2;
	private int direction = 1;
	private int width;
	private int height;
	private final static int mustWalk = 10;
	private byte working = -1;
	private byte walked = 0;
	private boolean inRandomMove = false;
	private byte randomSteps = 0;
	private byte id; 
	private boolean blocked = false;


	@SuppressWarnings("rawtypes")
	public Robot(Context context, byte id, Pair coordinates) {
		this.y = (Integer) coordinates.getKey();
		this.x = (Integer) coordinates.getValue();
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bot);
		this.width = bitmap.getWidth() / BMP_COLUMNS;
		this.height = bitmap.getHeight() / BMP_ROWS;
		this.id = id;
	}

	public int getDirection(){
		return this.direction;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pair nextPosition(){
		int nextX = 0, nextY = 0;
		switch (getDirection()) {
		case 0:{
			nextX = this.getX();
			nextY=this.getY() - 1;
			break;
		}
		case 1:{
			nextX = this.getX() -1;
			nextY=this.getY();
			break;
		}
		case 2:{
			nextX = this.getX() + 21;
			nextY=this.getY();
			break;
		}
		case 3:{
			nextX = this.getX();
			nextY=this.getY() + 21;
			break;
		}
		}
		return new Pair(nextX, nextY);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pair getPosition(){
		Pair position = new Pair(x,y);
		return position;
	}

	public void updatePosition(){
		LevelProperties.insert('R', this.getPosition());
		LevelProperties.dumpMap();
	}


	@SuppressWarnings("rawtypes")
	public boolean canMove(){
		Pair next = nextPosition();
		return !LevelProperties.hasObjectByScreenCoordinates((Integer)next.getKey(), (Integer)next.getValue());
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

	public void redirectMoves(int working){
		if(working == -1){
			working = direction;

		}
		LevelProperties.delete(this.getPosition());
		switch (working) {
		case 0:{
			moveUp();
			break;
		}
		case 1:{
			moveLeft();
			break;
		}
		case 2:{
			moveRight();
			break;
		}
		case 3:{
			moveDown();
			break;
		}
		}
	}

	public void runRandom(){
		int j;
		working = -1;
		if(randomSteps < 4){
			Random generator = new Random(); 
			int i = generator.nextInt(3) + 1;					
			direction = i;
			if(canMove()){
				inRandomMove = true;
				randomSteps++;
				redirectMoves(working);
			}
			else
				for (j = 0; j < 4; j++){
					direction = j;
					if(canMove()){
						redirectMoves(working);
						randomSteps++;
						inRandomMove = true;
					}
					break;
				}
		}
		else {
			inRandomMove = false;
			randomSteps = 0;
			working = -1;
		}
	}


	public void checkDirection(int distX, int distY){

		/**************************************************************************************/
		/**************************************************************************************/

		//Aproxima.se do player pelo eixo x ou y
		if(Math.abs(distX) <=5 || Math.abs(distY) <=5){


			if(Math.abs(distY) <= 5 && Math.abs(distX) >= 5 && distX < 0) {
				direction = 2;
				LevelProperties.delete(this.getPosition());
				if(canMove()) {
					moveRight();
				} else runRandom();
			}

			if(Math.abs(distY) <= 5 && Math.abs(distX) >= 5 && distX > 0) {
				direction = 1;
				LevelProperties.delete(this.getPosition());
				if(canMove()) {
					moveLeft();
				} else runRandom();
			}

			if(Math.abs(distX) <= 5 && Math.abs(distY) >= 5 && distY > 0) {
				direction = 0;
				LevelProperties.delete(this.getPosition());
				if(canMove()) {
					moveUp();
				} else runRandom();
			}

			if(Math.abs(distX) <= 5 && Math.abs(distY) >= 5 && distY < 0) {
				direction = 3;
				LevelProperties.delete(this.getPosition());
				if(canMove()) {
					moveDown();
				} else runRandom();
			}
		}

		/**************************************************************************************/
		/**************************************************************************************/

		//Escolhe o eixo com menos distancia ate ao player e posiciona.se la
		else {
			if (Math.abs(distX) < Math.abs(distY)) {
				if(distX > 0) {
					direction = 1;
					LevelProperties.delete(this.getPosition());
					if(canMove()) {
						moveLeft();
					} else {
						working = -1;
						runRandom();
					}
				} else {
					direction = 2;
					LevelProperties.delete(this.getPosition());

					if(canMove()) {
						moveRight();
					} else {
						working = -1;
						runRandom();
					}
				}
			} else if(distY > 0) {
				direction = 0;
				LevelProperties.delete(this.getPosition());
				if(canMove()) {
					moveUp();
				} else {
					working = -1;
					runRandom();
				}
			} else {
				direction = 3;
				LevelProperties.delete(this.getPosition());
				if(canMove()) {
					moveDown();
				} else {
					working = -1;
					runRandom();
				}
			}
		}
		/**************************************************************************************/
		/**************************************************************************************/
	}

	public void update(int playerX, int playerY, boolean paused) {
		int distX = this.getX() - playerX;
		int distY = this.getY() - playerY;

		if(!(working < 0))
			redirectMoves(working);
		
		if(blocked);

		else{
			if(!paused){
				if(inRandomMove)
					runRandom();
				else 
					//heuristic #1
					checkDirection(distX, distY);
			}
			else
				//heuristic #2
				runRandom();
		}
	}

	private void update() {
		currentFrame = ++currentFrame % BMP_COLUMNS;
		walked++;		
	}

	public void moveUp() {

		if(walked < mustWalk){
			working = 0;
			direction = 3;
			y -= 1 * VELOCITY;
			update();
		}
		else{
			walked = 0;
			working = -1;
			this.currentFrame = 1;
			this.updatePosition();
		}

	}

	public void moveLeft() {

		if(walked < mustWalk){
			working = 1;
			x -= 1 * VELOCITY;
			update();
		}
		else{
			walked = 0;
			working = -1;
			this.currentFrame = 1;
			this.updatePosition();
		}
	}

	public void moveRight() {

		if(walked < mustWalk){
			working = 2;
			direction = 2;
			x += 1 * VELOCITY;
			update();
		}
		else {
			walked = 0;
			working = -1;
			this.currentFrame = 1;
			this.updatePosition();
		}
	}

	public void moveDown() {

		if(walked < mustWalk){
			working = 3;
			direction = 0;	
			y += 1 * VELOCITY;
			update();
		}
		else{
			walked = 0;
			working = -1;
			this.currentFrame = 1;
			this.updatePosition();
		}
	}
}
