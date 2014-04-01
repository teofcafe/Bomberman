package cmov.bomberman.menu;

import cmov.bomberman.game.GameBoard;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;
		TextView usernameTextView;
		String username;
		/*final GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);*/

		//GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
		//Bitmap bitmap = Bitmap.createBitmap(gameBoard.bmp.getWidth(),gameBoard.bmp.getHeight(), Bitmap.Config.RGB_565);
		//final Canvas canvas = new Canvas(bitmap);
		/*Bitmap result =Bitmap.createBitmap(bitmap,imgView.getLeft()+10, imgView.getTop()+50, imgView.getWidth()-20, imgView.getHeight()-100);
		bitmap.recycle();*/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		final GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
		gameBoard.tostador();

		Bitmap bitmap = Bitmap.createBitmap(gameBoard.bmp.getWidth(),gameBoard.bmp.getHeight(), Bitmap.Config.RGB_565);
		final Canvas canvas = new Canvas(bitmap);

		settings =  getSharedPreferences("UserInfo", 0);
		username = (settings.getString("Username", "").toString());
		usernameTextView = (TextView)findViewById(R.id.playerNameTextView);

		usernameTextView.setText(username);	

		ImageButton clickButton = (ImageButton) findViewById(R.id.rightButton);
		clickButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(),"clicaram.me", 
						Toast.LENGTH_SHORT).show();
				gameBoard.move();
				gameBoard.draw(canvas);
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

		intent = new Intent(this.getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		GameActivity.this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

}
