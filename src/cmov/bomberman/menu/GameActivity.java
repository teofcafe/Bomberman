package cmov.bomberman.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;
		TextView usernameTextView;
		String username;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		settings =  getSharedPreferences("UserInfo", 0);
		username = (settings.getString("Username", "").toString());
		usernameTextView = (TextView)findViewById(R.id.playerNameTextView);
		
		usernameTextView.setText(username);	

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

}
