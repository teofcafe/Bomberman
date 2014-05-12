package cmov.bomberman.menu;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.widget.Toast;

public class MultiplayerGameActivity extends GameActivity {
	
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	IntentFilter mIntentFilter;
	@SuppressWarnings("rawtypes")
	private ArrayList peersLst = new ArrayList();
	ArrayList<WifiP2pDevice> specialPeers = new ArrayList<WifiP2pDevice>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		mReceiver = new WifiBroadcast(mManager, mChannel, this);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(getBaseContext(), "finding peers", Toast.LENGTH_SHORT).show();
			}
				@Override
				public void onFailure(int reasonCode) {
					Toast.makeText(getBaseContext(), " not finding peers", Toast.LENGTH_SHORT).show();
				}
			});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, mIntentFilter);
	}
		
}
