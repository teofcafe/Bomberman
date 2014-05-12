package cmov.bomberman.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
public class LevelSelectionActivity extends Activity {
	SharedPreferences settings;	
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_selection);
		settings = getSharedPreferences("UserInfo", 0);
		editor = settings.edit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent intent = getIntent(); // gets the previously created intent
		String mode = intent.getStringExtra("mode");
		String role= intent.getStringExtra("role"); 

		if(mode.equals("singleplayer")) {
			intent = new Intent(this.getApplicationContext(), ModeSelectionActivity.class);
			startActivity(intent);
		} else {
			intent = new Intent(this.getApplicationContext(), RoleSelectionActivity.class);
			startActivity(intent);
			
		}
		
		super.onBackPressed();
		LevelSelectionActivity.this.finish();
	}

	public void setLevelBeginner(View view) {
		editor.putString("Level","level1");
		editor.commit();
		startGameMenu(view);
	}

	public void setLevelIntermediate(View view) {
		editor.putString("Level","level2");
		editor.commit();
		startGameMenu(view);
	}

	public void setLevelAdvanced(View view) {
		editor.putString("Level","level3");
		editor.commit();
		startGameMenu(view);
	}

	public void startGameMenu(View view){
		Intent intent = getIntent(); // gets the previously created intent
		String mode = intent.getStringExtra("mode");
		String role= intent.getStringExtra("role"); 
		
		if(mode.equals("singleplayer"))
			intent = new Intent(this.getApplicationContext(), GameActivity.class);
		else
			intent = new Intent(this.getApplicationContext(), MultiplayerGameActivity.class);
		
		startActivity(intent);
		LevelSelectionActivity.this.finish();
	}
}
