package cmov.bomberman.game;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import android.content.Context;
import android.media.JetPlayer.OnJetEventListener;
import android.util.Log;
import cmov.bomberman.pair.*;
import cmov.bomberman.game.components.*;

public class LevelProperties {
	private String levelName = "";
	private int gameDuration;
	private int explosionTimeout;
	private int explosionDuration;
	private int explosionRange;


	private int numberOfWalls;
	private int numberOfObstacles;
	private int numberOfRobots;
	
	private ArrayList<Player> players;
	private ArrayList<Wall> walls;
	private ArrayBlockingQueue<Obstacle> obstacles;
	private ArrayBlockingQueue<Robot> robots;

	

	//grid layout
	public static char[][] gridMap;

	public static boolean[][] gridLayout;


	//cells per second
	int robotSpeed;
	int pointsPerRobotKilled;
	int pointsPerOponentKilled;

	public LevelProperties(int players, int lines, int columns){
		this.gridMap = new char[lines][columns];
		this.gridLayout = new boolean[lines][columns];
		this.walls = new ArrayList<Wall>();
		this.players = new ArrayList<Player>();
		this.obstacles = new ArrayBlockingQueue(1000);
		this.robots = new ArrayBlockingQueue(4);
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public void setWalls(ArrayList<Wall> walls) {
		this.walls = walls;
	}

	public ArrayBlockingQueue<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayBlockingQueue<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public ArrayBlockingQueue<Robot> getRobots() {
		return robots;
	}

	public void setRobots(ArrayBlockingQueue<Robot> robots) {
		this.robots = robots;
	}

	public int getNumberOfwalls() {
		return numberOfWalls;
	}
	
	public void addWall(Context context,Pair coordinates){
		this.walls.add(new Wall(context, Mapping.mapToScreen(coordinates)));
	}
	
	public void addObstacle(Context context,Pair coordinates){
		this.obstacles.add(new Obstacle(context, Mapping.mapToScreen(coordinates)));
	}
	
	public void addPlayer(Context context,int avatar, Pair coordinates){
		this.players.add(new Player(context,avatar, Mapping.mapToScreen(coordinates)));	
	}
	
	public void addRobot(Context context, Pair coordinates){
		this.robots.add(new Robot(context,(byte) robots.size(), Mapping.mapToScreen(coordinates)));
	}

	public void setNumberOfwalls(int numberOfwalls) {
		this.numberOfWalls = numberOfwalls;
	}

	public int getNumberOfObstacles() {
		return numberOfObstacles;
	}

	public void setNumberOfObstacles(int numberOfObstacles) {
		this.numberOfObstacles = numberOfObstacles;
	}

	public int getNumberOfRobots() {
		return numberOfRobots;
	}

	public void setNumberOfRobots(int numberOfRobots) {
		this.numberOfRobots = numberOfRobots;
	}


	public char[][] getGridMap() {
		return gridMap;
	}

	public void setGridMap(char[][] gridMap) {
		this.gridMap = gridMap;
	}

	public boolean[][] getGridLayout() {
		return gridLayout;
	}
	
	// Screen coordinates
	public static boolean hasObjectByScreenCoordinates(int x, int y){
	 	Pair mapCoordinates = Mapping.screenToMap(new Pair(x,y));
		int xvalue = (Integer) mapCoordinates.getKey();
		int yvalue = (Integer) mapCoordinates.getValue();
		Log.d("gridlayout","XValue="+xvalue +" yvalue="+yvalue + " cH="+gridMap[xvalue][yvalue] + " Ocupado="+gridLayout[xvalue][yvalue]);
		return LevelProperties.gridLayout[xvalue][yvalue];
	}
	
	// Screen coordinates
	public static boolean hasObjectByMapCoordinates(int x, int y){
		Log.d("gridlayout","XValue="+x +" yvalue="+y + " cH="+gridMap[x][y] + " Ocupado="+gridLayout[x][y]);
		return LevelProperties.gridLayout[x][y];
	}
	

	public void setGridLayout(boolean[][] gridLayout) {
		this.gridLayout = gridLayout;
	}



	public Pair getPlayerPositions(int player) {
		return this.getPlayers().get(player-1).getPosition();
	}

	
//arguments as game coordinates
	public void setPlayerPositions(int player, Pair pair) {
		this.getPlayers().get(player-1).setPosition(pair);
	}
	
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public int getGameDuration() {
		return gameDuration;
	}
	public void setGameDuration(int gameDuration) {
		this.gameDuration = gameDuration;
	}
	public int getExplosionTimeout() {
		return explosionTimeout;
	}
	public void setExplosionTimeout(int explosionTimeout) {
		this.explosionTimeout = explosionTimeout;
	}
	public int getExplosionDuration() {
		return explosionDuration;
	}
	public void setExplosionDuration(int explosionDuration) {
		this.explosionDuration = explosionDuration;
	}
	public int getExplosionRange() {
		return explosionRange;
	}
	public void setExplosionRange(int explosionRange) {
		this.explosionRange = explosionRange;
	}
	public int getRobotSpeed() {
		return robotSpeed;
	}
	public void setRobotSpeed(int robotSpeed) {
		this.robotSpeed = robotSpeed;
	}
	public int getPointsPerRobotKilled() {
		return pointsPerRobotKilled;
	}
	public void setPointsPerRobotKilled(int pointsPerRobotKilled) {
		this.pointsPerRobotKilled = pointsPerRobotKilled;
	}
	public int getPointsPerOponentKilled() {
		return pointsPerOponentKilled;
	}
	public void setPointsPerOponentKilled(int pointsPerOponentKilled) {
		this.pointsPerOponentKilled = pointsPerOponentKilled;
	}

	public void setGridMap(int x,int y, char value){
		this.gridMap[x][y]=value;
	}


	public void dumpPlayerPositions(){
		
		for(Player player : this.getPlayers()){
			//TODO adicionar nome aos players
//			String player =
			Pair position = player.getPosition();
			String x = position.getKey().toString();
			String y = position.getValue().toString();
			System.out.println("X="+x+" Y="+y);
		}
	}

	public static void dumpMap(){
		for(int i=0;i<gridMap.length;i++){
			for(int j=0;j<gridMap[i].length;j++)
				System.out.print(gridMap[i][j]);
			System.out.printf("\n");	
		}

	}

	public static void dumpGrid(){
		for(int i=0;i<gridLayout.length;i++){
			for(int j=0;j<gridLayout[i].length;j++)
				System.out.print(gridLayout[i][j] ? '1' : '0');
			System.out.printf("\n");	
		}
	}

	public boolean isCellEmpty(int x, int y){
		return !this.gridLayout[x][y];
	}

	public void deleteObject(int x, int y){
		this.setGridMap(x, y, '-');
		this.gridLayout[x][y]=false;
	}

	public void addObject(int x, int y,char value){
		this.setGridMap(x,y,value);
		this.gridLayout[x][y]=true;
	}

	public Wall getWallByMapCoordinates(Pair coordinates){
		
		Pair screenCoordinates = Mapping.mapToScreen(coordinates);
		int xvalue = (Integer)screenCoordinates.getValue();
		int yvalue = (Integer)screenCoordinates.getKey();
		
		for(Wall wall : this.getWalls()){
			if(wall.getX()==xvalue && wall.getY()==yvalue)
				return wall;
		}
		
		throw new RuntimeException("Wall not found");
	}
	
	public Wall getWallByGameCoordinates(int x,int y){
		
		for(Wall wall : this.getWalls()){
			if(wall.getX()==x && wall.getY()==y)
				return wall;
		}
		return null;
	}
	
	public Obstacle getObstacleByMapCoordinates(int x, int y){
		return this.getObstacleByMapCoordinates(new Pair(x,y));
	}
	
	public Obstacle getObstacleByMapCoordinates(Pair coordinates){
		
		Pair screenCoordinates = Mapping.mapToScreen(coordinates);
		int xvalue = (Integer)screenCoordinates.getValue();
		int yvalue = (Integer)screenCoordinates.getKey();
		
		for(Obstacle obstacle : this.getObstacles()){
			if(obstacle.getX()==xvalue && obstacle.getY()==yvalue)
				return obstacle;
		}
		
		return null;
	}
	
	public Robot getRobotByMapCoordinates(int x, int y){
		return this.getRobotByMapCoordinates(new Pair(x,y));
	}
	
	public Robot getRobotByMapCoordinates(Pair coordinates){
		
		Pair screenCoordinates = Mapping.mapToScreen(coordinates);
		int xvalue = (Integer)screenCoordinates.getValue();
		int yvalue = (Integer)screenCoordinates.getKey();
		
		for(Robot robot : this.getRobots()){
			if(robot.getX()==xvalue && robot.getY()==yvalue)
				return robot;
		}
		
		return null;
	}
	
	public Obstacle getObstacleByGameCoordinates(int x,int y){
		for(Obstacle obstacle : this.getObstacles()){
			if(obstacle.getX()==x && obstacle.getY()==y)
				return obstacle;
		}
		return null;
	}
	

	//Game Coordinates
	//Method to be used by Players and Robots
	public static void insert(char object,Pair coordinates){
		Pair newCoordinates = Mapping.screenToMap(coordinates);
		int x = (Integer)newCoordinates.getKey();
		int y = (Integer)newCoordinates.getValue();
		gridMap[x][y]=object;
		gridLayout[x][y]=true;
	}
	
	//Method to be used by Players and Robots
	public static void delete(Pair coordinates){
		Pair newCoordinates = Mapping.screenToMap(coordinates);
		int x = (Integer)newCoordinates.getKey();
		int y = (Integer)newCoordinates.getValue();
		Log.d("posicaoplayer", "estou a eliminar X="+x+" Y="+y+" CH="+gridMap[x][y]);
		gridLayout[x][y]=false;
		gridMap[x][y]='-';
	}
	
	//Map Coordinates
	public void delete(int x,int y) {

		char objectToDelete = gridMap[x][y];
		switch(objectToDelete){
			case 'O': 
				boolean removi=this.getObstacles().remove(getObstacleByMapCoordinates(x, y));
				for(Obstacle obstacle : this.getObstacles()){
					Log.d("obstaculo","Obstaculo: X="+obstacle.getX() +" Y="+obstacle.getY());
				}
				Log.d("posicao","remover x= " +x + " y="+y +" removi " + removi);
				gridLayout[x][y]=false;
				gridMap[x][y]='-';
				break;
			case '1':
				gridLayout[x][y]=false;
				gridMap[x][y]='-';
				Log.d("posicaoplayer","removi o player na posicao "+x+" "+y+" com o conteudo "+objectToDelete);
				break;

			case 'R':
				boolean removed = this.getRobots().remove(getRobotByMapCoordinates(x, y));
				for(Robot robot : this.getRobots())
					Log.d("robot","ROBOT: X="+robot.getX() +" Y="+robot.getY());
				Log.d("posicao","remover x= " +x + " y="+y +" removi " + removed);
				gridLayout[x][y] = false;
				break;	

		}
	}
	
	public String toString(){
		String properties = "LEVEL NAME="+this.getLevelName() + "\n" + "GAME DURATION="+this.getGameDuration() +
				"\n" + "EXPLOSTION TIMEOUT="+this.getExplosionTimeout()+"\n"+"EXPLOSION RANGE="+this.getExplosionRange()+
				"\n" + "ROBOT SPEED="+this.getRobotSpeed()+"\n"+"POINTS PER ROBOT KILLED"+this.getPointsPerRobotKilled()+
				"\n" + "POINTS PER OPPONNENT KILLED="+this.getPointsPerOponentKilled();

		this.dumpPlayerPositions();
		this.dumpMap();
		this.dumpGrid();
		return properties;

	}

}
