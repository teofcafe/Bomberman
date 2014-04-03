package cmov.bomberman.game;

import cmov.bomberman.game.components.Player;
import cmov.bomberman.game.components.Wall;
import cmov.bomberman.menu.R;
import cmov.bomberman.resize.Resize;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameBoard extends SurfaceView implements SurfaceHolder.Callback {


	private MainThread thread;
	private Player player;
	private Wall wall;
	
	
	//tamanhos maximos por definir
	public int maxWidth,maxHeight;
	



	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		
		getHolder().addCallback(this);
		//setFocusable(true);
		//it's best not to create any new objects in the on draw
		//initialize them as class variables here

		
		Bitmap bmpPlayer = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar_1);
		Bitmap bmpWall = BitmapFactory.decodeResource(this.getResources(), R.drawable.wall);
		Bitmap bmpObstacle=BitmapFactory.decodeResource(this.getResources(), R.drawable.obstacle);
		
		player=new Player(Resize.getResizedBitmap(bmpPlayer, bmpPlayer.getHeight()*2/3, bmpPlayer.getWidth()*2/3),19,21);
		wall=new Wall(Resize.getResizedBitmap(bmpWall,bmpWall.getHeight()*2/3,bmpWall.getWidth()*2/3),30,30);

		//		LinearLayout layout = (LinearLayout)findViewById(R.id.gameBoardLayout);
		//		this.maxHeight = layout.getMeasuredHeight();
		//		this.maxWidth = layout.getMeasuredWidth();

		thread= new MainThread(getHolder(), this);
		setFocusable(true);

	}


	public void moveRight() {
		int newPosition=player.getX()+3;

		Log.d("posicao","Max:" + getHeight() + " Proximo:" + newPosition);
		if(canMoveX(newPosition)) {

			player.setX(newPosition);
			player.setDirection(2);
		}
	}

	public void moveLeft(){
		int newPosition=player.getX()-3;

		Log.d("posicao","Max:" + getHeight() + " Proximo:" + newPosition);
		if(canMoveX(newPosition)) {

			player.setX(newPosition);
			player.setDirection(1);
		}
	}

	public void moveUp(){
		int newPosition=player.getY()-3;


		Log.d("posicao","Max:" + getWidth() + " Proximo:" + newPosition);
		if(canMoveY(newPosition)) {

			player.setY(newPosition);
			player.setDirection(3);
		}
	}

	public void moveDown(){
		int newPosition=player.getY()+3;

		Log.d("posicao","Max:" + getWidth() + " Proximo:" + newPosition);
		if(canMoveY(newPosition)) {
			player.setY(newPosition);
			player.setDirection(0);
		}
	}

	public boolean canMoveX(int x){
		int playerWidth=player.getBitmap().getWidth();
		if(x + playerWidth>maxWidth || x<playerWidth)
			return false;
		return true;
	}

	public boolean canMoveY(int y){
		int playerHeight= player.getBitmap().getHeight();
		if(y+playerHeight>maxHeight || y<playerHeight)
			return false;
		return true;
	}

	public void exitGame() {
		thread.setRunning(false);
	}


	@Override
	synchronized public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		player.draw(canvas);
		wall.draw(canvas);
	}
	//initialize the field

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while(retry){
			try{
				thread.join();
				retry = false;
			} catch (InterruptedException e){

			}
		}

	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
		//		Toast.makeText(this.getContext()," Button Touched", Toast.LENGTH_SHORT).show();
		//		i = i + 10;
		//		if (event.getY() > getHeight() - 50) {
		//			thread.setRunning(false);
		//			((Activity)getContext()).finish();
		//		} else {
		//		}
		//		return true;
	}



}
