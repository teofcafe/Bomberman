package cmov.bomberman.menu;

import cmov.bomberman.menu.adapter.ImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class SettingsActivity extends Activity {
	int imageSelected;
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Button save, cancel;
		final EditText editText;
		SharedPreferences settings;
		final GridView gridview;
		SharedPreferences.Editor editor;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		save = (Button)findViewById(R.id.saveButton);
		cancel = (Button)findViewById(R.id.cancelButton);
		editText = (EditText) findViewById(R.id.insertName);
		gridview = (GridView) findViewById(R.id.gridview);
		
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		settings =  getSharedPreferences("UserInfo", 0);
		editText.setText((settings.getString("Username", "").toString()));
		editText.setSelection(editText.length());
		final ImageAdapter myImageAdapter= new ImageAdapter(this);
		gridview.setAdapter(myImageAdapter);
		
		settings = getSharedPreferences("UserInfo", 0);
		editor = settings.edit();
		imageSelected = (settings.getInt("SelectedAvatar", -1));
				
		myImageAdapter.chageState(imageSelected);
		myImageAdapter.notifyDataSetChanged();

		    gridview.setOnItemClickListener(new OnItemClickListener() { 
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        	imageSelected = position;
		        	myImageAdapter.chageState(position);
					myImageAdapter.notifyDataSetChanged();
		        }
		    });
		
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences settings; 
				SharedPreferences.Editor editor;
				Intent intent;
				
				intent = new Intent(getApplicationContext(), HomeActivity.class);
				settings = getSharedPreferences("UserInfo", 0);
				editor = settings.edit();
				
				editor.putString("Username",editText.getText().toString());
				editor.putInt("SelectedAvatar", imageSelected);
				editor.commit();
				
				startActivity(intent);
				SettingsActivity.this.finish();
            }
        });
		
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
            }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent;
		
		intent = new Intent(this.getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		SettingsActivity.this.finish();
	}
}