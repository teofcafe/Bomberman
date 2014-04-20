package cmov.bomberman.game;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import cmov.bomberman.game.components.Bomb;

import cmov.bomberman.game.components.Explosion;
import cmov.bomberman.game.components.Player;
import cmov.bomberman.game.components.Wall;
import cmov.bomberman.menu.GameActivity;
import cmov.bomberman.pair.Pair;
import android.content.Context;
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
	//layout size max
	private int maxWidth, maxHeight;
	public Robot bot;

	private LevelProperties levelProperties;
	
	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		getHolder().addCallback(this);
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
	


	private void drawWall(Canvas canvas,Pair fileCoordinates) {
		int posX=0,posY=0;
		int maxHeight=this.getHeight();
		int maxWidth=this.getWidth();
		setMaxHeight(maxHeight);
		setMaxWidth(maxWidth);


		Pair coordinates = Mapping.mapToScreen(fileCoordinates, maxWidth, maxHeight,LoadMap.LINES,LoadMap.COLUMNS);
		int coordX = (Integer)coordinates.getKey();
		int coordY = (Integer) coordinates.getValue();
		System.out.println("COORD X=" + coordX );
		System.out.println("COORD Y=" + coordY);
		Wall wall = new Wall(getContext(),coordX,coordY);
		wall.draw(canvas);
		
//		int posX=0,posY=0;


//
//		Log.d("parede", "maxHeight: " + maxHeight);
//		Log.d("parede","maxWidth: " + maxWidth);
//		int i=0;
//
//
//		int amountOfWalls=(maxHeight/this.wallHeight)*2;
//		amountOfWalls+=(maxWidth/this.wallWidth)*2;
//		Log.d("parede", "amount of walls: " + amountOfWalls);
//
//		wall = new Wall[amountOfWalls];
//
//		wall[0] = new Wall(this.resizedWall,300,0);
//		wall[0].draw(canvas);
//		wall[1] = new Wall(this.resizedWall,50,50);
//		wall[1].draw(canvas);
//
//		//		for(;posX<maxWidth;posX+=this.wallWidth){
//		//			for(;posY<maxHeight;posY+=this.wallHeight){
//		//				wall[i]=new Wall(this.resizedWall,posX,0);
//		//				wall[i++].draw(canvas);
//		//				Log.d("parede","PosX: " + posX + " PosY: " + posY);
//		//			}
//		//		}
//		//		
//		//		for(;posY<maxHeight;posY+=this.wallHeight){
//		//				wall[i]=new Wall(this.resizedWall,0,posY);
//		//				wall[i++].draw(canvas);
//		//				Log.d("parede","PosX: " + posX + " PosY: " + posY);
//		//			}
//		//		}
	}

	

	public void gameStart(int avatar) {	
		bot = new Robot(getContext(), 55,55);
		player = new Player(getContext(), avatar, 50, 50);


		try {
			//TODO relacionar os niveis com o grau de dificuldade do jogo
			String levelName = "level1";
			int resID = getResources().getIdentifier(levelName , "raw", GameActivity.packageName);

			InputStream level = getResources().openRawResource(resID);
			this.levelProperties = LoadMap.loadMap(level);

		} catch (IOException e) {
			System.err.println("Unable to read map");
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
		for(int i=0;i<19;i++)
			drawWall(canvas,new Pair(0,i));
//			for(int j=0;j<13;j++)
//				drawWall(canvas,new Pair(19,j));
//		drawWall(canvas,new Pair(0,0));
//		drawWall(canvas,new Pair(0,1));
//		drawWall(canvas,new Pair(1,0));
//		drawWall(canvas,new Pair(5,0));

		if(this.bombDroped)
			if(this.bombExploded) 
				drawExplosion(canvas);
			else drawBomb(canvas);

		player.draw(canvas);
	}

	private void drawExplosion(Canvas canvas) {
		for(Explosion explosion : explosions) 
			explosion.draw(canvas);
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
		bot.update(player.getX(), player.getY(), player.isPaused());
		if(player.isKeyTouched())
			player.update();
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
		int explosionRange = levelProperties.getExplosionRange();
		explosions = new ArrayList<Explosion>();
		
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
			bomb = new Bomb(getContext(), this.player.getX(), this.player.getY());
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
