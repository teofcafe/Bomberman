package cmov.bomberman.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class RoleSelectionActivity extends Activity {
	SharedPreferences settings;	
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role_selection);
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
		Intent intent;
		intent = new Intent(this.getApplicationContext(), ModeSelectionActivity.class);
		startActivity(intent);
		RoleSelectionActivity.this.finish();
	}
	
	public void startServer(View view) {
		Intent intent = new Intent(this.getApplicationContext(),LevelSelectionActivity.class);
		intent.putExtra("mode","multiplayer");
		intent.putExtra("role","server");
		startActivity(intent);
		RoleSelectionActivity.this.finish();
	}
	
	public void startClient(View view) {
		Intent intent = new Intent(this.getApplicationContext(),ClientActivity.class);
		intent.putExtra("mode","multiplayer");
		intent.putExtra("role","client");
		
		
		
		
		
		editor.putString("Level","level1");
		editor.commit();
		startActivity(intent);
		RoleSelectionActivity.this.finish();
	}
}
