package cmov.bomberman.game;


import cmov.bomberman.game.components.Bomb;
import cmov.bomberman.game.components.Player;
import cmov.bomberman.game.components.Wall;
import cmov.bomberman.game.resizer.Resize;
import cmov.bomberman.menu.R;
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
	private Thread updateBomb;
	private Player player;
	private Bomb bomb;
	private Wall[] wall;
	private boolean bombDroped = false;
	//layout size max
	private int maxWidth, maxHeight;
	//playout resized size
	private int playerHeight, playerWidth;
	//wall resized size
	private int wallHeight, wallWidth;
	private Bitmap resizedWall;

	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		getHolder().addCallback(this);

		Bitmap bmpWall = BitmapFactory.decodeResource(this.getResources(), R.drawable.wall);

		this.wallHeight = bmpWall.getHeight()*2/3;
		this.wallWidth = bmpWall.getWidth()*2/3;

		this.resizedWall=Resize.getResizedBitmap(bmpWall,this.wallHeight,this.wallWidth);

	}

	public void setMaxWidth(int maxWidth){
		this.maxWidth=maxWidth;
	}

	public void setMaxHeight(int maxHeight){
		this.maxHeight=maxHeight;
	}

	private void drawWall(Canvas canvas) {
		int posX=0,posY=0;
		int maxHeight=this.getHeight();
		int maxWidth=this.getWidth();
		setMaxHeight(maxHeight);
		setMaxWidth(maxWidth);


		Log.d("parede", "maxHeight: " + maxHeight);
		Log.d("parede","maxWidth: " + maxWidth);
		int i=0;


		int amountOfWalls=(maxHeight/this.wallHeight)*2;
		amountOfWalls+=(maxWidth/this.wallWidth)*2;
		Log.d("parede", "amount of walls: " + amountOfWalls);

		wall = new Wall[amountOfWalls];

		wall[0] = new Wall(this.resizedWall,300,0);
		wall[0].draw(canvas);
		wall[1] = new Wall(this.resizedWall,50,50);
		wall[1].draw(canvas);

		//		for(;posX<maxWidth;posX+=this.wallWidth){
		//			for(;posY<maxHeight;posY+=this.wallHeight){
		//				wall[i]=new Wall(this.resizedWall,posX,0);
		//				wall[i++].draw(canvas);
		//				Log.d("parede","PosX: " + posX + " PosY: " + posY);
		//			}
		//		}
		//		
		//		for(;posY<maxHeight;posY+=this.wallHeight){
		//				wall[i]=new Wall(this.resizedWall,0,posY);
		//				wall[i++].draw(canvas);
		//				Log.d("parede","PosX: " + posX + " PosY: " + posY);
		//			}
		//		}
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
		drawWall(canvas);

		if(this.bombDroped)
			drawBomb(canvas);	



		player.draw(canvas);
	}

	private void drawBomb(Canvas canvas) {
		this.bomb.draw(canvas);	
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

	public Player getPlayer() {
		return player;
	}

	@SuppressWarnings("static-access")
	public void updateBomb() {
		while(!bomb.isExploded()) {
			bomb.update();
			try {
				updateBomb.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		bomb = null;
		bombDroped = false;
		
		updateBomb.interrupt();
	}

	public void dropBomb() {
		if(!bombDroped) {
			bomb = new Bomb(BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb), this.player.getX(), this.player.getY());
			this.bombDroped = true;

			updateBomb = new Thread() {
				public void run() {
					updateBomb();
				}
			};
		}

		updateBomb.start();
	}
}
