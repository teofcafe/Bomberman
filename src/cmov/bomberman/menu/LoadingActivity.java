package cmov.bomberman.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class LoadingActivity extends ClientActivity {
	
	
	ArrayList<WifiP2pDevice> specialPeers = new ArrayList<WifiP2pDevice>();
	Handler load;
	String peerAddr;
	String token = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		mReceiver = new ClientWifiBroadcast(mManager, mChannel, this);
		Intent intent = getIntent(); // gets the previously created intent
		peerAddr = intent.getStringExtra("peerAddr");
		
		Toast.makeText(getBaseContext(), "Peer: " + peerAddr, Toast.LENGTH_SHORT).show();
		

		load = new Handler();
		load.postDelayed(loader, 1000);
	}
	
	Runnable loader = new Runnable() {

		@SuppressWarnings("unchecked")
		public void run() {

				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = peerAddr;

				Toast.makeText(getBaseContext(), "trying to connect", Toast.LENGTH_SHORT).show();

				mManager.connect(mChannel, config, new ActionListener() {

					@Override
					public void onSuccess() {
						Toast.makeText(getBaseContext(), "connected", Toast.LENGTH_SHORT).show();
						
//						Intent intent = new Intent(getBaseContext(), ClientActivity.class);
//						startActivity(intent);
						
					}

					@Override
					public void onFailure(int reason) {
						Toast.makeText(getBaseContext(), " fail", Toast.LENGTH_SHORT).show();
					}
				});

				load.removeCallbacks(loader);
		}
	};
	
	public void startGame() {
		Intent intent = new Intent(getBaseContext(), ClientActivity.class);
		startActivity(intent);
		LoadingActivity.this.finish();
	}

}
