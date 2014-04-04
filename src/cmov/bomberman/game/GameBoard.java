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
	}

	public void gameStart(int avatar) {		
		switch (avatar) {
		case 0:
			player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.player_1),50,50);
			break;
		case 1:
			player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.player_2),50,50);
			break;
		case 2:
			player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.player_3),50,50);
			break;
		case 3:
			player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.player_4),50,50);
			break;
		case 4:
			player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.player_5),50,50);
			break;
		case 5:
			player=new Player(BitmapFactory.decodeResource(this.getResources(), R.drawable.player_6),50,50);
			break;
		}

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

			switch (player.getDirection()) {
			case 0:
				player.moveDown();
				break;
			case 1:
				player.moveLeft();
				break;
			case 2:
				player.moveRight();
				break;
			case 3:
				player.moveUp();
				break;

			}			
		}
	}
}
