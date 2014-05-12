package cmov.bomberman.menu;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cmov.bomberman.game.GameBoard;
import cmov.bomberman.game.LevelProperties;

import cmov.bomberman.game.Mapping;
import cmov.bomberman.game.components.Robot;
import cmov.bomberman.pair.Pair;


public class GameActivity extends Activity implements OnTouchListener{
	GameBoard gameBoard;
	public static String packageName;
	Handler updateTimeHander;
	private TextView timeLeft;
	private TextView playerScore;
	private TextView numberPlayers;
	
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	IntentFilter mIntentFilter;
	 private List peersLst = new ArrayList();
	 ArrayList<WifiP2pDevice> specialPeers = new ArrayList<WifiP2pDevice>(); 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings;
		TextView usernameTextView;

		String username;
		int avatar;
		String level;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		gameBoard = (GameBoard)findViewById(R.id.gameBoard);
		packageName = getApplicationContext().getPackageName();
		settings =  getSharedPreferences("UserInfo", 0);
		username = (settings.getString("Username", "").toString());
		usernameTextView = (TextView)findViewById(R.id.playerNameTextView);
		timeLeft = (TextView)findViewById(R.id.timeLeftTextView);
		playerScore = (TextView)findViewById(R.id.playerScoreTextView);
		numberPlayers = (TextView)findViewById(R.id.numberPlayersTextView);
		usernameTextView.setText(username);	

		avatar = settings.getInt("SelectedAvatar", -1);
		level = settings.getString("Level", "").toString();

		gameBoard.gameStart(avatar, level);

		updateTimeHander = new Handler();
		updateTimeHander.post(updateDashboard);

		final ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
		ImageButton leftButton = (ImageButton)findViewById(R.id.leftButton);
		ImageButton upButton = (ImageButton)findViewById(R.id.upButton);
		ImageButton downButton = (ImageButton) findViewById(R.id.downButton);
		
	    mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    mChannel = mManager.initialize(this, getMainLooper(), null);
	    mReceiver = new WifiBroadcast(mManager, mChannel, this);
	    
	    mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	   

		upButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (gameBoard.getPlayer().getWorking() == false){
						gameBoard.getPlayer().setDirection(3);
					if( (!gameBoard.getPlayer().isPaused()) && (gameBoard.getPlayer().canMove())){
						gameBoard.getPlayer().setDirection(3);
						gameBoard.getPlayer().setWorking(true);
						gameBoard.getPlayer().setTouched(true);
						break;
					}}
				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);
				}  	

				return true;
			}});
		
		mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
              public void onSuccess() {
                  Log.d("CNN", " remove group success");
              }
              public void onFailure(int reason) {
                  Log.d("CNN", " remove group fail " + reason);
              }
          });
		
		mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            public void onSuccess() {
                Log.d("CNN", " create group success");
                Toast.makeText(getBaseContext(), "sucesso no create", Toast.LENGTH_SHORT).show();
            }
            public void onFailure(int reason) {
                Log.d("CNN", " create group fail " + reason);
                Toast.makeText(getBaseContext(), "fail no create", Toast.LENGTH_SHORT).show();
            }
        });
		
		
		
		
		
		
		
		
		
		/* mManager.requestGroupInfo(mChannel, new GroupInfoListener() {
	            @Override
	            public void onGroupInfoAvailable(WifiP2pGroup group) {
	                if (group != null && mManager != null && mChannel != null
	                        && group.isGroupOwner()) {
	                	
	                	Toast.makeText(getBaseContext(), group.describeContents(), Toast.LENGTH_LONG).show();
	                }
	            }
	        });*/
		 
		 
		 
		 
		 
		 
		 
		 
		
		
	/*	mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
		    @Override
		    public void onSuccess() {
		    	Toast.makeText(getBaseContext(), "finding peers", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onFailure(int reasonCode) {
		    	Toast.makeText(getBaseContext(), " not  ttt t  tt t tfinding peers", Toast.LENGTH_SHORT).show();
		    }
		});*/
		
		

		downButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (gameBoard.getPlayer().getWorking() == false){
						gameBoard.getPlayer().setDirection(0);
					if( (!gameBoard.getPlayer().isPaused()) &&  (gameBoard.getPlayer().canMove())){
						gameBoard.getPlayer().setDirection(0);
						gameBoard.getPlayer().setWorking(true);
						gameBoard.getPlayer().setTouched(true);
						break;

					}}

				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);

				}
				return true;
			}});


		leftButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (gameBoard.getPlayer().getWorking() == false){
						gameBoard.getPlayer().setDirection(1);
					if( (!gameBoard.getPlayer().isPaused()) && (gameBoard.getPlayer().canMove())){
						gameBoard.getPlayer().setWorking(true);
						gameBoard.getPlayer().setDirection(1);
						gameBoard.getPlayer().setTouched(true);
						break;
					}}


				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);

				}
				return true;
			}});

		rightButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					if (gameBoard.getPlayer().getWorking() == false) {
						gameBoard.getPlayer().setDirection(2);
						if( (!gameBoard.getPlayer().isPaused())  && (gameBoard.getPlayer().canMove())){

							gameBoard.getPlayer().setWorking(true);
							gameBoard.getPlayer().setDirection(2);
							gameBoard.getPlayer().setTouched(true);
							break;
						}
					}

				case MotionEvent.ACTION_UP:
					gameBoard.getPlayer().setTouched(false);

				}
				return true;
			}});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	Runnable updateDashboard = new Runnable() {

		public void run() {
			timeLeft.setText(Integer.toString((gameBoard.getLevelProperties().getGameDuration() / (1000*60)) % 60) + ":" +
					Integer.toString((gameBoard.getLevelProperties().getGameDuration() / 1000) % 60));

			playerScore.setText(Integer.toString(gameBoard.getPlayer().getScore()));
			
			if(gameBoard.getLevelProperties().getGameDuration() == 0) {
				Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
				gameBoard.exitGame();	
				updateTimeHander.removeCallbacks(updateDashboard);
			} else {
				gameBoard.getLevelProperties().setGameDuration(gameBoard.getLevelProperties().getGameDuration() - 1000);

				playerScore.setText(Integer.toString(gameBoard.getPlayer().getScore()));
				numberPlayers.setText(Integer.toString(1)); //TODO actualizar quando for MP

				updateTimeHander.postDelayed(this, 1000);
			}
		}
	};
	
	

	@Override
	public void onBackPressed() {
		gameBoard.getPlayer().setPaused();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		updateTimeHander.removeCallbacks(updateDashboard);
	}

	public void quitGame(View view) {
		Intent intent;
		gameBoard.exitGame();
		intent = new Intent(this.getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		GameActivity.this.finish();
	}

	public void pauseGame(View view) {
		gameBoard.getPlayer().setPaused();
	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	public void dropBomb(View view) {
		//this.gameBoard.dropBomb();
		WifiP2pDeviceList myPeers = ((WifiBroadcast) mReceiver).getPeers();
		peersLst.addAll(myPeers.getDeviceList());
		Log.d("CN",peersLst.get(0).toString() );
		specialPeers.addAll(myPeers.getDeviceList());
		WifiP2pDevice device = specialPeers.get(0);
		//Log.d("CN",specialPeers.get(0).toString() );
		
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		/*mManager.connect(mChannel, config, new ActionListener() {

		    @Override
		    public void onSuccess() {
		    	Log.d("CN","CONECTADOOOOO" );
		    }

		    @Override
		    public void onFailure(int reason) {
		    	Log.d("CN","fail" );
		    }
		});*/
		
    }
	
	
	/* register the broadcast receiver with the intent values to be matched */
	@Override
	protected void onResume() {
	    super.onResume();
	    registerReceiver(mReceiver, mIntentFilter);
	}
	/* unregister the broadcast receiver */
	@Override
	protected void onPause() {
	    super.onPause();
	    unregisterReceiver(mReceiver);
	}
}
