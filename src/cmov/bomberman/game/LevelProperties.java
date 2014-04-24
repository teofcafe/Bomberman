package cmov.bomberman.game;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import cmov.bomberman.pair.*;
import cmov.bomberman.game.components.*;

public class LevelProperties {
	private String levelName = "";
	private int gameDuration;
	private int explosionTimeout;
	private int explosionDuration;
	private int explosionRange;

	Pair playerPositions[];

	private int numberOfWalls;
	private int numberOfObstacles;
	private int numberOfRobots;
	
	private ArrayList<Player> players;
	private ArrayList<Wall> walls;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Robot> robots;
	

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

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}

	public void setRobots(ArrayList<Robot> robots) {
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
	
	public void addRobot(){
		this.numberOfRobots++;
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

	public void setGridLayout(boolean[][] gridLayout) {
		this.gridLayout = gridLayout;
	}

	//grid layout
	public char[][] gridMap;

	public boolean[][] gridLayout;


	//cells per second
	int robotSpeed;
	int pointsPerRobotKilled;
	int pointsPerOponentKilled;

	public LevelProperties(int players, int lines, int columns){
		this.gridMap = new char[lines][columns];
		this.gridLayout = new boolean[lines][columns];
		this.playerPositions =  new Pair[players];
		this.walls = new ArrayList<Wall>();
		this.obstacles = new ArrayList<Obstacle>();
		this.robots = new ArrayList<Robot>();
	}
	

	public Pair getPlayerPositions(int player) {
		return playerPositions[player];
	}


	public void setPlayerPositions(int player, Pair pair) {
		this.playerPositions[player-1] = pair;
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
		for(int i=0;i<playerPositions.length;i++){
			String player = Integer.toString(i+1);
			System.out.println("PLAYER="+player);
			String x = this.getPlayerPositions(i).getKey().toString();
			String y = this.getPlayerPositions(i).getValue().toString();
			System.out.println("X="+x+" Y="+y);

		}
	}

	public void dumpMap(){
		for(int i=0;i<gridMap.length;i++){
			for(int j=0;j<gridMap[i].length;j++)
				System.out.print(gridMap[i][j]);
			System.out.printf("\n");	
		}

	}

	public void dumpGrid(){
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
		int x = (Integer)coordinates.getKey();
		int y = (Integer)coordinates.getValue();
		
		for(Wall wall : this.getWalls()){
			if(wall.getX()==x && wall.getY()==y)
				return wall;
		}
		
		throw new RuntimeException("Wall not found");
	}
	
	public Wall getWallByGameCoordinates(int x,int y){
		Pair coordinates = new Pair(x,y);
		Pair mapCoordinates = Mapping.screenToMap(coordinates);
		
		try{
			return this.getWallByMapCoordinates(mapCoordinates);
		}catch(Exception e){
			return null;
		}
	}
	
	public Obstacle getObstacleByMapCoordinates(Pair coordinates){
		int x = (Integer)coordinates.getKey();
		int y = (Integer)coordinates.getValue();
		
		for(Obstacle obstacle : this.getObstacles()){
			if(obstacle.getX()==x && obstacle.getY()==y)
				return obstacle;
		}
		
		return null;
	}
	
	public Obstacle getObstacleByGameCoordinates(int x,int y){
		Pair coordinates = new Pair(x,y);
		Pair mapCoordinates = Mapping.screenToMap(coordinates);
		
		try{
			return this.getObstacleByMapCoordinates(mapCoordinates);
		}catch(Exception e){
			return null;
		}
	}
	
	public void delete(int x,int y){
		char objectToDelete = gridMap[x][y];
		switch(objectToDelete){
			case 'O': 
				this.getObstacles().remove(getObstacleByMapCoordinates(new Pair(x,y)));
				Log.d("posicao","remover x= " +x + " y="+y );
				break;
			
		}
		gridLayout[x][y]=false;
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
