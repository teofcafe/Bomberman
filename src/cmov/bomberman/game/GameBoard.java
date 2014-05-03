package cmov.bomberman.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import cmov.bomberman.game.components.Bomb;
import cmov.bomberman.game.components.Explosion;
import cmov.bomberman.game.components.Obstacle;
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

	private boolean bombDroped = false;
	private boolean bombExploded = false;
	private ArrayList<Explosion> explosions; 
	//layout size max
	@SuppressWarnings("unused")
	private int maxWidth, maxHeight;

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

	private void drawObstacle(Canvas canvas) {
		int maxHeight=this.getHeight();
		int maxWidth=this.getWidth();
		setMaxHeight(maxHeight);
		setMaxWidth(maxWidth);

		for(Obstacle obstacle : levelProperties.getObstacles()){
			obstacle.draw(canvas);
		}
	}

	private void drawWall(Canvas canvas) {
		int maxHeight=this.getHeight();
		int maxWidth=this.getWidth();
		setMaxHeight(maxHeight);
		setMaxWidth(maxWidth);

		for(Wall wall : levelProperties.getWalls()){
			wall.draw(canvas);
		}
	}

	private void drawRobots(Canvas canvas) {
		int maxHeight=this.getHeight();
		int maxWidth=this.getWidth();
		setMaxHeight(maxHeight);
		setMaxWidth(maxWidth);

		for(Robot robot : levelProperties.getRobots())
			robot.draw(canvas);

	}
	
	public void gameStart(int avatar, String levelName) {	
		try {
			
			int resID = getResources().getIdentifier(levelName , "raw", GameActivity.packageName);

			InputStream level = getResources().openRawResource(resID);
			this.levelProperties = LoadMap.loadMap(level,getContext(),avatar,320, 360);
			//TODO alterar o index para o player respectivo 
			player = this.levelProperties.getPlayers().get(0);

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
		drawWall(canvas);
		drawObstacle(canvas);
		drawRobots(canvas);

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
		player.update();
		//TODO tratar da actualizacao dos robots
//		for(Robot robot : levelProperties.getRobots())
//			robot.update(player.getX(), player.getY(), player.isPaused());
	}


	public Player getPlayer() {
		return player;
	}

	
	//arguments as gameCoordinates
	public void deleteObjects(int x, int y){
		char type;
		Pair mapCoordinates = Mapping.screenToMap(new Pair(x,y));
		int xvalue = (Integer) mapCoordinates.getKey();
		int yvalue = (Integer) mapCoordinates.getValue();
		type = levelProperties.delete(xvalue,yvalue);
		
		switch(type) {
		case 'R': 
			player.setScore(player.getScore() + levelProperties.getPointsPerRobotKilled());
			break;
		case 'O': 
			player.setScore(player.getScore() + levelProperties.getPointsPerOponentKilled());
			break;
		}
		
		System.out.println("Obstaculos: " + levelProperties.getObstacles().size());
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	public void updateBomb() {
		while(!bomb.isExploded()) {
			bomb.update();
			try {
				updateBomb.sleep((levelProperties.getExplosionTimeout())/4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int bombX = bomb.getX(), bombY = bomb.getY();
		int explosionRange = levelProperties.getExplosionRange();
		explosions = new ArrayList<Explosion>();

		 
		//No eixo dos X
		for(int i = bombX - explosionRange; i <= bombX + explosionRange; i += bomb.getWidth())
			//No eixo dos Y
			for(int j = bombY - explosionRange; j <= bombY + explosionRange; j += bomb.getHeight()) 
				//para fazer cruzamento
				if(j != bombY && i != bombX) 
					continue;
				else{
					explosions.add(new Explosion(getContext(), i, j, bombX, bombY, explosionRange));
					
				}

		bomb = null;
		bombExploded = true;

		while(explosions.get(0).isAlive()) {
			for(Explosion explosion : explosions) 
				explosion.update();
			try {
				updateBomb.sleep((levelProperties.getExplosionDuration())/4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(Explosion explosion : explosions) {
			deleteObjects(explosion.getX(),explosion.getY());
			explosion = null;
		}

		bombExploded = false;
		bombDroped = false;
		updateBomb.interrupt();
	}

	public void dropBomb() {
		if(!bombDroped && !player.isPaused()) {
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
