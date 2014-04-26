package cmov.bomberman.game.components;


import cmov.bomberman.menu.R;
import cmov.bomberman.pair.Pair;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
	private int direction = 0;		
	private boolean keyTouched = false;
	private final static float VELOCITY = 2;
	private boolean paused;
	private boolean working;
	private int steps;
	private final static int mustWalk = 10;
	private int score = 0;


	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Player(Context context, int avatar, Pair coordinates){
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

	public Pair nextPosition(){
		int nextX = 0, nextY = 0;
		switch (getDirection()) {
		case 0:{
			nextX = this.getX();
			nextY=this.getY() + 1;
			break;
		}
		case 1:{
			nextX = this.getX() -1;
			nextY=this.getY();
			break;
		}
		case 2:{
			nextX = this.getX() + 1;
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

	public Pair getPosition(){
		Pair position = new Pair(x,y);
		return position;
	}

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
	
	public boolean CanMove(Pair pair){
		return true;
		
	}

	public void update() {

		if( (this.working || isKeyTouched()) && CanMove(nextPosition()) ){
			currentFrame = ++currentFrame % BMP_COLUMNS; //update

			switch (getDirection()) {
			case 0:
				moveDown();
				break;
			case 1:
				moveLeft();
				break;
			case 2:{
				moveRight();
				break;
			}
			case 3:
				moveUp();
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

	public void moveDown() {
		if(this.working ){
			if( ((this.steps)  + 1) > mustWalk){
				resetSteps();
				if(!isKeyTouched())
					this.working= false;
				else
					moveDown();
			}
			else{
				this.steps++;
				currentFrame = ++currentFrame % BMP_COLUMNS;
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
					moveLeft();
			}
			else{
				this.steps++;
				currentFrame = ++currentFrame % BMP_COLUMNS;
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
					moveRight();
			}
			else{
				this.steps++;
				currentFrame = ++currentFrame % BMP_COLUMNS;
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
					moveUp();
			}
			else{
				this.steps++;
				currentFrame = ++currentFrame % BMP_COLUMNS;
				y -=1 * VELOCITY;
			}
		}			
	}
}

