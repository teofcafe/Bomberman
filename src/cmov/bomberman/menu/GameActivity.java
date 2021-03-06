package cmov.bomberman.menu;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.GetChars;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cmov.bomberman.game.GameBoard;
import cmov.bomberman.game.LevelProperties;


public class GameActivity extends Activity implements OnTouchListener{
	GameBoard gameBoard;
	public static String packageName;
	Handler updateTimeHander;
	private TextView timeLeft;
	private TextView playerScore;
	private TextView numberPlayers;
	String mode="",role="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;
		TextView usernameTextView;
		String username;
		int avatar;
		String level;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);


		gameBoard = (GameBoard)findViewById(R.id.gameBoard);
		packageName = getApplicationContext().getPackageName();
		settings =  getSharedPreferences("UserInfo", 0);
		username = (settings.getString("Username", "").toString());
		usernameTextView = (TextView)findViewById(R.id.playerNameTextView);


		timeLeft = (TextView)findViewById(R.id.timeLeftTextView);
		playerScore = (TextView)findViewById(R.id.playerScoreTextView);
		numberPlayers = (TextView)findViewById(R.id.numberPlayersTextView);
		usernameTextView.setText(username);	
		//SP
		avatar = settings.getInt("SelectedAvatar", -1);
		level = settings.getString("Level", "").toString();


		Intent intent = getIntent(); // gets the previously created intent
		mode = intent.getStringExtra("mode");
		role = intent.getStringExtra("role");

		if(mode.equals("singleplayer") || role.equals("server")){
			this.singlePlayerMode(avatar,level);
			updateTimeHander = new Handler();
			updateTimeHander.post(updateDashboard);
		}



		final ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
		ImageButton leftButton = (ImageButton)findViewById(R.id.leftButton);
		ImageButton upButton = (ImageButton)findViewById(R.id.upButton);
		ImageButton downButton = (ImageButton) findViewById(R.id.downButton);

		upButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (gameBoard.getPlayer().getWorking() == false){
						gameBoard.getPlayer().setDirection(3);
						if( (!gameBoard.getPlayer().isPaused()) && (gameBoard.getPlayer().canMove())){
							gameBoard.getPlayer().setDirection(3);
							gameBoard.getPlayer().setWorking(true);
							gameBoard.getPlayer().setTouched(true);
							break;
						}}
				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);
				}  	

				return true;
			}});

		downButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (gameBoard.getPlayer().getWorking() == false){
						gameBoard.getPlayer().setDirection(0);
						if( (!gameBoard.getPlayer().isPaused()) &&  (gameBoard.getPlayer().canMove())){
							gameBoard.getPlayer().setDirection(0);
							gameBoard.getPlayer().setWorking(true);
							gameBoard.getPlayer().setTouched(true);
							break;

						}}

				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);

				}
				return true;
			}});


		leftButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (gameBoard.getPlayer().getWorking() == false){
						gameBoard.getPlayer().setDirection(1);
						if( (!gameBoard.getPlayer().isPaused()) && (gameBoard.getPlayer().canMove())){
							gameBoard.getPlayer().setWorking(true);
							gameBoard.getPlayer().setDirection(1);
							gameBoard.getPlayer().setTouched(true);
							break;
						}}


				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);

				}
				return true;
			}});

		rightButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					if (gameBoard.getPlayer().getWorking() == false) {
						gameBoard.getPlayer().setDirection(2);
						if( (!gameBoard.getPlayer().isPaused())  && (gameBoard.getPlayer().canMove())){

							gameBoard.getPlayer().setWorking(true);
							gameBoard.getPlayer().setDirection(2);
							gameBoard.getPlayer().setTouched(true);
							break;
						}
					}

				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);

				}
				return true;
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	Runnable updateDashboard = new Runnable() {

		public void run() {
			timeLeft.setText(Integer.toString((gameBoard.getLevelProperties().getGameDuration() / (1000*60)) % 60) + ":" +
					Integer.toString((gameBoard.getLevelProperties().getGameDuration() / 1000) % 60));

			playerScore.setText(Integer.toString(gameBoard.getPlayer().getScore()));

			if(gameBoard.getLevelProperties().getGameDuration() == 0) {
				Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
				gameBoard.exitGame();	
				updateTimeHander.removeCallbacks(updateDashboard);
			} else {
				gameBoard.getLevelProperties().setGameDuration(gameBoard.getLevelProperties().getGameDuration() - 1000);

				playerScore.setText(Integer.toString(gameBoard.getPlayer().getScore()));
				numberPlayers.setText(Integer.toString(gameBoard.getLevelProperties().getNumberOfPlayers()));

				updateTimeHander.postDelayed(this, 1000);
			}
		}
	};

	@Override
	public void onBackPressed() {
		gameBoard.getPlayer().setPaused();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mode.equals("singleplayer"))
			updateTimeHander.removeCallbacks(updateDashboard);
	}

	public void quitGame(View view) {
		Intent intent;
		gameBoard.exitGame();
		intent = new Intent(this.getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		GameActivity.this.finish();
	}

	public void pauseGame(View view) {
		gameBoard.getPlayer().setPaused();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	public void dropBomb(View view) {
		this.gameBoard.dropBomb();
	}

	public void singlePlayerMode(int avatar, String level){
		gameBoard.gameStartSinglePlayer(avatar, level,mode,role);
	}

	public void multiplayerMode(int avatar, String level, String role, int timeleft, int players, char[][] gameStatus){
		gameBoard.gameStartMultiplayer(avatar, level, role, timeleft, players, gameStatus);
		updateTimeHander = new Handler();
		updateTimeHander.post(updateDashboard);
	}


}
