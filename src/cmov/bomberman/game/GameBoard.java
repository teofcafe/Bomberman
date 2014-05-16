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
	private LevelProperties levelProperties;

	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		getHolder().addCallback(this);
	}


	public LevelProperties getLevelProperties(){
		return this.levelProperties;
	}

	private void drawObstacle(Canvas canvas) {
		for(Obstacle obstacle : levelProperties.getObstacles())
			obstacle.draw(canvas);
	}

	private void drawWall(Canvas canvas) {
		for(Wall wall : levelProperties.getWalls())
			wall.draw(canvas);
	}

	private void drawRobots(Canvas canvas) {
		for(Robot robot : levelProperties.getRobots())
			robot.draw(canvas);
	}

	public void gameStartSinglePlayer(int avatar, String levelName) {	

		try {

			int resID = getResources().getIdentifier(levelName , "raw", GameActivity.packageName);
			InputStream level = getResources().openRawResource(resID);

			this.levelProperties = LoadMap.loadMap(level,getContext(),avatar);
			//TODO alterar o index para o player respectivo 
			player = this.levelProperties.getPlayerById((byte)1);
			removeAdicionalPlayers();
			
			Robot.setVELOCITY(this.levelProperties.getRobotSpeed());
			int mustWalk = 20 / this.levelProperties.getRobotSpeed();
			Robot.setMustWalk(mustWalk);
			
		} catch (IOException e) {
			System.err.println("Unable to read map");
		}

		startMainThread();	
	}

	public void gameStartMultiplayer(int idPlayer, String levelName, String role, int timeleft, int players, char[][] gameStatus) {	

			int resID = getResources().getIdentifier(levelName , "raw", GameActivity.packageName);
			InputStream level = getResources().openRawResource(resID);

			try {
				this.levelProperties = LoadMap.loadMultiplayer(level,getContext(),idPlayer,timeleft,players,gameStatus);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.levelProperties == null) System.out.println("Level Propriedades null");
			player = this.levelProperties.getPlayerById((byte)idPlayer);
			player.setScore(0);
			
			Robot.setVELOCITY(this.levelProperties.getRobotSpeed());
			int mustWalk = 20 / this.levelProperties.getRobotSpeed();
			Robot.setMustWalk(mustWalk);
			
			startMainThread();
		
	}

	public void startMainThread() {
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
		for(Robot robot : levelProperties.getRobots())
			robot.update(player.getX(), player.getY(), player.isPaused());

	}


	public Player getPlayer() {
		return player;
	}

	public void robotKilled(Robot robot, int x, int y){
		levelProperties.deleteRobot(robot,x,y);
		player.setScore(player.getScore() + levelProperties.getPointsPerRobotKilled());
	}


	private void freezeObjects(int x, int y) {
		int expRange=levelProperties.getExplosionRange();

		for(Robot r : levelProperties.getRobots()){
			Pair robotCoordinates = Mapping.screenToMap(r.getPosition());
			Log.d("robots","X="+robotCoordinates.getKey()+" y="+robotCoordinates.getValue());
			int robotX=r.getX();
			int robotY=r.getY();
			if((robotX == x || robotY==y) &&(robotX > (x-expRange)) && (robotX < (x+expRange)) && 
					(robotY > (y-expRange)) && (robotY < (y+expRange))){
				Log.d("robots","consegui bloquear em "+ robotX + " y="+robotY);
				robotKilled(r,robotX,robotY);
			}

		}
	}

	@SuppressWarnings({ "static-access", "unused" })
	public void updateBomb() {
		while(!bomb.isExploded()) {
			bomb.update();
			try {
				updateBomb.sleep((levelProperties.getExplosionTimeout())/2);
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
				else if((j > 0 && j < this.getHeight()) && (i > 0 && i < this.getWidth())) {
					explosions.add(new Explosion(getContext(), i, j, bombX, bombY, explosionRange));

				}

		bomb = null;
		bombExploded = true;


		while(explosions.get(0).isAlive()) {
			for(Explosion explosion : explosions){
				freezeObjects(bombX, bombY);
				explosion.update();
			}


			try {
				updateBomb.sleep((levelProperties.getExplosionDuration())/4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}



		for(Explosion explosion : explosions) {
			deleteObstacles(explosion.getX(),explosion.getY());
			explosion = null;
		}

		bombExploded = false;
		bombDroped = false;
		updateBomb.interrupt();
	}

	private void deleteObstacles(int x, int y) {
		levelProperties.deleteObstacle(x,y);

	}

	public void dropBomb() {
		if(!bombDroped && !player.isPaused() && player.getWorking() == false) {
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

	public void removeAdicionalPlayers() {
		for(byte i=2; i<=levelProperties.getPlayers().size();i++)
			levelProperties.deletePlayerById(i);
	}

	
}
