package cmov.bomberman.game;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import cmov.bomberman.game.components.Bomb;

import cmov.bomberman.game.components.Explosion;
import cmov.bomberman.game.components.Player;
import cmov.bomberman.game.components.Wall;
import cmov.bomberman.game.resizer.Resize;
import cmov.bomberman.menu.GameActivity;
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
import cmov.bomberman.game.components.Robot;

public class GameBoard extends SurfaceView implements SurfaceHolder.Callback {

	private MainThread thread;
	private Thread updateBomb;
	private Player player;
	private Bomb bomb;
	private Wall[] wall;
	private boolean bombDroped = false;
	private boolean bombExploded = false;
	private ArrayList<Explosion> explosions; 
	private int explosionRange = 20;
	//layout size max
	private int maxWidth, maxHeight;
	//playout resized size
	private int playerHeight, playerWidth;
	//wall resized size
	private int wallHeight, wallWidth;
	private Bitmap resizedWall;
	public Robot bot;

	private LevelProperties levelProperties;
	
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
	
	public LevelProperties getLevelProperties(){
		return this.levelProperties;
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
		bot=new Robot(BitmapFactory.decodeResource(this.getResources(), R.drawable.bot),55,55);
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

		try {

			//TODO relacionar os niveis com o grau de dificuldade do jogo
			String levelName = "level1";
			int resID = getResources().getIdentifier(levelName , "raw", GameActivity.packageName);

			InputStream level = getResources().openRawResource(resID);

			this.levelProperties = LoadMap.loadMap(level);
	

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("mapa","Unable to read file.");
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
		bot.draw(canvas);
		drawWall(canvas);

		if(this.bombDroped)
			if(this.bombExploded) 
				drawExplosion(canvas);
			else drawBomb(canvas);

		player.draw(canvas);
	}

	private void drawExplosion(Canvas canvas) {
		for(Explosion explosion : explosions) 
			explosion.draw(canvas);
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
		bot.update(player.getX(), player.getY());
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

	@SuppressWarnings({ "static-access", "unused" })
	public void updateBomb() {
		while(!bomb.isExploded()) {
			bomb.update();
			try {
				updateBomb.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int bombX = bomb.getX(), bombY = bomb.getY();
		
		explosions = new ArrayList<Explosion>();
		Log.d("bomba","mmmmm");
		
		for(int i = bombX - explosionRange; i <= bombX + explosionRange; i += explosionRange) 
			for(int j = bombY - explosionRange; j <= bombY + explosionRange; j += explosionRange) 
				if(j != bombY && i != bombX) 
					continue;
				else {
					Log.d("bomba","i: " + i + " j: " + j + " bombX: " + bombX + " bombY: " + bombY);
					
					explosions.add(new Explosion(getContext(), i, j, bombX, bombY, explosionRange));
				}
		
		bomb = null;
		bombExploded = true;

		while(explosions.get(0).isAlive()) {
			for(Explosion explosion : explosions) 
				explosion.update();
			try {
				updateBomb.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for(Explosion explosion : explosions) 
			explosion = null;

		bombExploded = false;
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

			updateBomb.start();
		}
	}
}
