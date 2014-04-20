package cmov.bomberman.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cmov.bomberman.game.GameBoard;

public class GameActivity extends Activity implements OnTouchListener{
	GameBoard gameBoard;
	
	public static String packageName;
	Handler timeHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;
		TextView usernameTextView;
		String username;
		int avatar;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		gameBoard = (GameBoard)findViewById(R.id.gameBoard);
		packageName = getApplicationContext().getPackageName();
		settings =  getSharedPreferences("UserInfo", 0);
		username = (settings.getString("Username", "").toString());
		usernameTextView = (TextView)findViewById(R.id.playerNameTextView);

		usernameTextView.setText(username);	
		
		avatar = (settings.getInt("SelectedAvatar", -1));
		
		gameBoard.gameStart(avatar);
		timeHandler = new Handler();
		timeHandler.postDelayed(timeControler, gameBoard.getLevelProperties().getGameDuration());
		
		final ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
		ImageButton leftButton = (ImageButton)findViewById(R.id.leftButton);
		ImageButton upButton = (ImageButton)findViewById(R.id.upButton);
		ImageButton downButton = (ImageButton) findViewById(R.id.downButton);
		
		upButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!gameBoard.getPlayer().isPaused()){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					gameBoard.getPlayer().setDirection(3);
					gameBoard.getPlayer().setTouched(true);
					break;
				}

				case MotionEvent.ACTION_UP:{
					gameBoard.getPlayer().setTouched(false);
					gameBoard.getPlayer().setCurrentFrame(1);
				}  	
				}}
				return true;
			}});
		
		downButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!gameBoard.getPlayer().isPaused()){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					gameBoard.getPlayer().setDirection(0);
					gameBoard.getPlayer().setTouched(true);
					break;
				}

				case MotionEvent.ACTION_UP:{
					gameBoard.getPlayer().setTouched(false);
					gameBoard.getPlayer().setCurrentFrame(1);
				}  	
				}}
				return true;
			}});


		leftButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!gameBoard.getPlayer().isPaused()){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					gameBoard.getPlayer().setDirection(1);
					gameBoard.getPlayer().setTouched(true);
					break;
				}

				case MotionEvent.ACTION_UP:{
					gameBoard.getPlayer().setTouched(false);
					gameBoard.getPlayer().setCurrentFrame(1);
				}  	
				}}
				return true;
			}});

		rightButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!gameBoard.getPlayer().isPaused()){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					gameBoard.getPlayer().setDirection(2);
					gameBoard.getPlayer().setTouched(true);
					break;
				}

				case MotionEvent.ACTION_UP:{
					gameBoard.getPlayer().setTouched(false);
					gameBoard.getPlayer().setCurrentFrame(1);
				}  	
				}}
				return true;
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	private final Runnable timeControler = new Runnable(){
	    public void run(){
            	Toast.makeText(getApplicationContext(), "Tempo acabou...", Toast.LENGTH_SHORT).show();
            	gameBoard.exitGame();
	    }
	};

	@Override
	public void onBackPressed() {
		Intent intent;
		gameBoard.exitGame();
		intent = new Intent(this.getApplicationContext(), LevelSelectionActivity.class);
		startActivity(intent);
		GameActivity.this.finish();
	}
	
	@Override
	protected void onDestroy() {
	      super.onDestroy();
	      timeHandler.removeCallbacks(timeControler);
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

}
