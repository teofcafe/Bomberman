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
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Button save, cancel;
		final EditText editText;
		SharedPreferences settings;
		GridView gridview;
		
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
		gridview.setAdapter(new ImageAdapter(this));

		    gridview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        	Toast.makeText(SettingsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
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
				editor.commit();
				startActivity(intent);
				SettingsActivity.this.finish();
            }
        });
		
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent intent;
				
				intent = new Intent(getApplicationContext(), HomeActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
}
