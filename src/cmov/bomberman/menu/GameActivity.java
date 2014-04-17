package cmov.bomberman.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cmov.bomberman.game.GameBoard;

public class GameActivity extends Activity implements OnTouchListener{
	GameBoard gameBoard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;
		TextView usernameTextView;
		String username;
		int avatar;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		gameBoard = (GameBoard)findViewById(R.id.gameBoard);
		
		settings =  getSharedPreferences("UserInfo", 0);
		username = (settings.getString("Username", "").toString());
		usernameTextView = (TextView)findViewById(R.id.playerNameTextView);

		usernameTextView.setText(username);	
		
		avatar = (settings.getInt("SelectedAvatar", -1));
		
		gameBoard.gameStart(avatar);

		final ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
		ImageButton leftButton = (ImageButton)findViewById(R.id.leftButton);
		ImageButton upButton = (ImageButton)findViewById(R.id.upButton);
		ImageButton downButton = (ImageButton) findViewById(R.id.downButton);
		
		upButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
				}
				return true;
			}});
		
		downButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
				}
				return true;
			}});


		leftButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
				}
				return true;
			}});

		rightButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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

	@Override
	public void onBackPressed() {
		Intent intent;
		gameBoard.exitGame();
		intent = new Intent(this.getApplicationContext(), LevelSelectionActivity.class);
		startActivity(intent);
		GameActivity.this.finish();
	}

	public void quitGame(View view) {
		Intent intent;
		gameBoard.exitGame();
		intent = new Intent(this.getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		GameActivity.this.finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	public void dropBomb(View view) {
		this.gameBoard.dropBomb();
	}

}
