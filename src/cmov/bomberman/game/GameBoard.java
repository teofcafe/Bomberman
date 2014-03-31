package cmov.bomberman.game;

import cmov.bomberman.menu.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GameBoard extends SurfaceView implements SurfaceHolder.Callback {

	public Bitmap bmp;
	int i = 10;
	private MainThread thread;
	

	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		getHolder().addCallback(this);
		 //setFocusable(true);
		//it's best not to create any new objects in the on draw
		//initialize them as class variables here

		bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar_1);
		thread= new MainThread(getHolder(), this);
		setFocusable(true);
		
	}
	
	public void tostador() {
		
	}
	
public void move() {
		i = i+10;
	}
	
	
	@Override
	synchronized public void onDraw(Canvas canvas) {
		//create a black canvas
		//p.setColor(Color.BLACK);
		//p.setAlpha(255);
	    //p.setStrokeWidth(1);
		//canvas.drawRect(0, 0, getWidth(), getHeight(), p);
		canvas.drawBitmap(bmp, i, i, null);
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
		// TODO Auto-generated method stub
		
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
		Toast.makeText(this.getContext()," Button Touched", Toast.LENGTH_SHORT).show();
		i = i + 10;
		if (event.getY() > getHeight() - 50) {
			thread.setRunning(false);
			((Activity)getContext()).finish();
		} else {
		}
		return true;
}

		//draw the "boxes"

}
