package cmov.bomberman.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class HomeActivity extends Activity {

	String username = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;		
		TextView homePageUsername;
		SharedPreferences.Editor editor;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		settings =  getSharedPreferences("UserInfo", 0);
		username = settings.getString("Username", "").toString();
		
		if(username.equals("")){
			editor = settings.edit();
			editor.putString("Username",this.getString(R.string.guest));
			editor.commit();
		}
			
		homePageUsername = (TextView)findViewById(R.id.usernameHomePage);
		
		homePageUsername.setText(settings.getString("Username", "").toString());	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void changeNameMenu(View v) {
		
		Intent intent = new Intent(this.getApplicationContext(), SettingsActivity.class);
		startActivity(intent);
		HomeActivity.this.finish();
	}
	
	public void quit(View v) {
		Intent intent;
		
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		HomeActivity.this.finish();
	}
	
	public void levelSelectionMenu(View v) {
		Intent intent = new Intent(this.getApplicationContext(), LevelSelectionActivity.class);
		startActivity(intent);
		HomeActivity.this.finish();
	}
}
