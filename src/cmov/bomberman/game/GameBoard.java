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

	public MainThread thread;
	public Player player;

	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		getHolder().addCallback(this);
		player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.player_1),50,50);
		thread= new MainThread(getHolder(), this);
		setFocusable(true);
	}

	public void exitGame() {
		thread.setRunning(false);
	}

	public void startGame() {
		thread.setRunning(true);
	}

	@Override
	synchronized public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		player.draw(canvas);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
	}

	public void update() {
		if(player.getTouched()) {
			player.update();
			if(player.getDirection() == 0)
				player.moveDown();
			if(player.getDirection() == 1)
				player.moveLeft();
			if(player.getDirection() == 2)
				player.moveRight();
			if(player.getDirection() == 3)
				player.moveUp();
		}
	}
}
