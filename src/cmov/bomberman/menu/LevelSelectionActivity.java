package cmov.bomberman.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
public class LevelSelectionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_selection);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void startGameMenu(View view){
		Intent intent = new Intent(this.getApplicationContext(),GameActivity.class);
		startActivity(intent);
		LevelSelectionActivity.this.finish();
	}
	
	@Override
	public void onBackPressed() {
		Intent intent;
		
		intent = new Intent(this.getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		LevelSelectionActivity.this.finish();
	}
}
