package cmov.bomberman.game.components;


import java.util.logging.Level;

import cmov.bomberman.game.GameBoard;
import cmov.bomberman.game.Mapping;
import cmov.bomberman.menu.R;
import cmov.bomberman.pair.Pair;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import cmov.bomberman.game.LevelProperties;

public class Player {
	private byte id;
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
	private final static float VELOCITY = 2;
	private boolean paused;
	private boolean working;
	private int steps;
	private final static int mustWalk = 10;
	private int score = 0;
	private boolean blocked = false;


	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean getBlocked() {
		return this.blocked;
	}
	
	public byte getId(){
		return this.id;
	}
	
	public void setId(byte id){
		this.id=id;
	}

	@SuppressWarnings("rawtypes")
	public Player(Context context, int avatar,byte id, Pair coordinates){
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

		this.id=id;
		this.y = (Integer) coordinates.getKey();
		this.x = (Integer) coordinates.getValue();

		this.width = bitmap.getWidth() / BMP_COLUMNS;
		this.height = bitmap.getHeight() / BMP_ROWS;
		this.paused = false;
		this.working = false;
		this.steps = 0;
	}

	public int getX() {
		return x;
	}

	public boolean getWorking() {
		return this.working;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pair nextPosition(){
		int nextX = 0, nextY = 0;
		switch (getDirection()) {
		case 0:{
			nextX = this.getX();
			nextY=this.getY() + 21;
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
			nextY=this.getY() - 1;
			break;
		}
		}
		return new Pair(nextX, nextY);
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public int getY() {
		return y;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pair getPosition(){
		Pair position = new Pair(x,y);
		return position;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pair getMapCoordinatesPosition(){
		Pair coordinates = Mapping.screenToMap(new Pair(x,y));
		return coordinates;
	}

	@SuppressWarnings("rawtypes")
	public void setPosition(Pair coordinates){
		this.y = (Integer) coordinates.getKey();
		this.x = (Integer) coordinates.getValue();
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
		Paint paint = new Paint();  
		int srcX = currentFrame * width;
		//aqui muda a direction 
		int srcY = direction * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		if(paused)
			paint.setAlpha(100);   
		else
			paint = null;
		canvas.drawBitmap(bitmap, src, dst, paint);
	}

	public void setDirection(int animation) {
		this.direction = animation;
	}

	public int getSteps() {
		return this.steps;
	}

	public void setPosition(int value) {
		this.steps = value;
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

	public boolean canMove(){
		Pair next = nextPosition();
		return !LevelProperties.hasObjectByScreenCoordinates((Integer)next.getKey(), (Integer)next.getValue());
	}

	public void update() {

		if( (this.working || isKeyTouched())){
			currentFrame = ++currentFrame % BMP_COLUMNS; //update

			switch (getDirection()) {
			case 0:
				prepareToMoveDown();
				break;
			case 1:
				prepareToMoveLeft();
				break;
			case 2:{
				prepareToMoveRight();
				break;
			}
			case 3:
				prepareToMoveUp();
				break;
			}	
		}
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void setPaused() {
		this.paused = (!this.paused);
	}

	public void resetSteps(){
		this.steps = 0;
		this.currentFrame = 1;
	}

	public void stepsIncrement(){
		this.steps++;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void updatePosition(){
		LevelProperties.insert('1', this.getPosition());
	}


	public void prepareToMoveRight() {
		if(this.working){
			LevelProperties.delete(this.getPosition());
			moveRight();
			this.updatePosition();
		}

	}

	public void prepareToMoveLeft() {
		if(this.working){
			LevelProperties.delete(this.getPosition());
			moveLeft();
			this.updatePosition();
		}

	}

	public void prepareToMoveDown() {
		if(this.working){
			LevelProperties.delete(this.getPosition());
			moveDown();
			this.updatePosition();
		}

	}

	public void prepareToMoveUp() {
		if(this.working){
			LevelProperties.delete(this.getPosition());
			moveUp();
			this.updatePosition();
		}

	}
	public void moveDown() {
		if(this.working){
			if( ((this.steps)  + 1) > mustWalk){
				resetSteps();
				if(!isKeyTouched())
					this.working= false;
				else
					if(canMove())
						moveDown();
					else{
						this.working= false;
						this.keyTouched = false;
					}
			}
			else{
				stepsIncrement();
				y += 1 * VELOCITY;
			}
		}

	}


	public void moveLeft() {
		if(this.working){
			if(((this.steps)  + 1) > mustWalk){
				resetSteps();
				if(!isKeyTouched())
					this.working= false;
				else
					if(canMove())
						moveLeft();
					else{
						this.working= false;
						this.keyTouched = false;
					}
			}
			else {
				stepsIncrement();
				x -= 1 * VELOCITY;
			}
		}
	}

	public void moveRight() {
		if(this.working ){
			if( ((this.steps)  + 1) > mustWalk){
				resetSteps();
				if(!isKeyTouched())
					this.working= false;
				else 
					if(canMove())
						moveRight();
					else{
						this.working= false;
						this.keyTouched = false;
					}
			}
			else{
				stepsIncrement();
				x += 1 * VELOCITY;
			}
		}
	}

	public void moveUp() {
		if(this.working){

			if( ((this.steps)  + 1) > mustWalk){
				resetSteps();
				if(!isKeyTouched())
					this.working= false;
				else
					if(canMove())
						moveUp();
					else{
						this.working= false;
						this.keyTouched = false;
					}
			}
			else{
				stepsIncrement();
				y -=1 * VELOCITY;
			}
		}
	}
}

