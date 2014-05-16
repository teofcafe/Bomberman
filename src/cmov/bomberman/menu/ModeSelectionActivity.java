package cmov.bomberman.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class ModeSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode_selection);
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
		intent = new Intent(this.getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		ModeSelectionActivity.this.finish();
	}

	public void setSinglePlayer(View view) {
		Intent intent = new Intent(this.getApplicationContext(),LevelSelectionActivity.class);
		intent.putExtra("mode","singleplayer");
		startActivity(intent);
		ModeSelectionActivity.this.finish();
	}

	public void setMultiplayer(View view) {
		Intent intent = new Intent(this.getApplicationContext(), RoleSelectionActivity.class);
		startActivity(intent);
		ModeSelectionActivity.this.finish();
	}

}
