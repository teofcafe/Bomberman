package cmov.bomberman.game;

import cmov.bomberman.game.components.Player;
import cmov.bomberman.menu.R;
import android.content.Context;
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
	
	//tamanhos maximos por definir
	int maxWidth,maxHeight;
	

	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		getHolder().addCallback(this);
		//setFocusable(true);
		//it's best not to create any new objects in the on draw
		//initialize them as class variables here
//		LinearLayout layout = (LinearLayout)findViewById(R.id.gameBoardLayout);
//		this.maxHeight = layout.getMeasuredHeight();
//		this.maxWidth = layout.getMeasuredWidth();
		player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar_1),10,10);
		thread= new MainThread(getHolder(), this);
		setFocusable(true);

	}


	public void moveRight() {
		int newPosition=player.getX()+3;
		Log.d("posicao","Max:" + getHeight() + " Proximo:" + newPosition);
		if(canMoveX(newPosition))
			player.setX(newPosition);
	}

	public void moveLeft(){
		int newPosition=player.getX()-3;
		Log.d("posicao","Max:" + getHeight() + " Proximo:" + newPosition);
		if(canMoveX(newPosition))
			player.setX(newPosition);
	}

	public void moveUp(){
		int newPosition=player.getY()-3;
		Log.d("posicao","Max:" + getWidth() + " Proximo:" + newPosition);
		if(canMoveY(newPosition))
			player.setY(newPosition);
	}

	public void moveDown(){
		int newPosition=player.getY()+3;
		Log.d("posicao","Max:" + getWidth() + " Proximo:" + newPosition);
		if(canMoveY(newPosition))
			player.setY(newPosition);
	}

	public boolean canMoveX(int x){
		if(x>getWidth() || x<13)
			return false;
		return true;
	}

	public boolean canMoveY(int y){
		if(y>getHeight() || y<0)
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
