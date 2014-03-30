package cmov.bomberman.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameBoard extends View {
	
	private Paint p;

	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		//it's best not to create any new objects in the on draw
		//initialize them as class variables here
		p = new Paint();
	}
	
	@Override
	synchronized public void onDraw(Canvas canvas) {
		
		//create a black canvas
		p.setColor(Color.BLACK);
		p.setAlpha(255);
	    p.setStrokeWidth(1);
		canvas.drawRect(0, 0, getWidth(), getHeight(), p);
		
		//initialize the field

		//draw the "boxes"

	}

}
