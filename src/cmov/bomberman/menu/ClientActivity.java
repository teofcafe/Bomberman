package cmov.bomberman.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ClientActivity extends MultiplayerGameActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getStartupProperties();
		
	}
	
	protected void onCreate(Bundle savedInstanceState,boolean noNothing) {
		super.onCreate(savedInstanceState);
		
	}
	
	public void getStartupProperties(){
	
		Intent intent = getIntent();
		String role= intent.getStringExtra("role");
		String levelName=intent.getStringExtra("levelName");
		int timeLeft=intent.getIntExtra("timeLeft", 0);
		int numberOfPlayers=intent.getIntExtra("numberOfPlayers", 1);
		int idPlayer=intent.getIntExtra("idPlayer", 1);
		System.out.println("ROLE: " + role + " levelName: " + levelName + " timeleft: " + timeLeft + " numberOfPlayers" + numberOfPlayers +
				" idplayer: " + idPlayer);
		String gameStatus = intent.getStringExtra("gameStatus");
		
		String[] map = gameStatus.split("nl");

		char[][] currentMap = new char[map.length][map[0].length()];

		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length(); j++) 
				currentMap[i][j] = map[i].charAt(j);
		}
		
		
		this.multiplayerMode(idPlayer,levelName,role,timeLeft,numberOfPlayers, currentMap);
	}

}
