package cmov.bomberman.menu;

import cmov.bomberman.game.GameBoard;
import cmov.bomberman.game.components.Player;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnClickListener{

	GameBoard gameBoard = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;
		TextView usernameTextView;
		String username;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		gameBoard = (GameBoard)findViewById(R.id.gameBoard);



		settings =  getSharedPreferences("UserInfo", 0);
		username = (settings.getString("Username", "").toString());
		usernameTextView = (TextView)findViewById(R.id.playerNameTextView);

		usernameTextView.setText(username);	

		ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
		ImageButton leftButton = (ImageButton)findViewById(R.id.leftButton);
		ImageButton upButton = (ImageButton)findViewById(R.id.upButton);
		ImageButton downButton = (ImageButton) findViewById(R.id.downButton);
		
		
		leftButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				gameBoard.moveLeft();			

			}
		});


		rightButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				gameBoard.moveRight();			

			}
		});
		
		upButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				gameBoard.moveUp();			

			}
		});
		
		downButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				gameBoard.moveDown();			

			}
		});



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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
