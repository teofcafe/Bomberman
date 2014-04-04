package cmov.bomberman.game;

import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;


public class MainThread extends Thread {



	private static final String TAG = MainThread.class.getSimpleName();

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface
	private GameBoard gamePanel;



	// flag to hold game state 
	private boolean running;
	public void setRunning(boolean running) {
		this.running = running;
	}



	public MainThread(SurfaceHolder surfaceHolder, GameBoard gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;	
	}

	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (running) {
			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state 
					// render state to the screen
					// draws the canvas on the panel
					this.gamePanel.update();
					this.gamePanel.onDraw(canvas);	
					try {
						TimeUnit.MILLISECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} finally {
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}
}