package cmov.bomberman.menu;

import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
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

public class MultiplayerGameActivity extends GameActivity {

	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	IntentFilter mIntentFilter;
	@SuppressWarnings("rawtypes")
	private ArrayList peersLst = new ArrayList();
	ArrayList<WifiP2pDevice> specialPeers = new ArrayList<WifiP2pDevice>();
	Handler waitPeers;
	
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

		waitPeers = new Handler();
		waitPeers.postDelayed(waitForPeers, 1000);
		
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

	Runnable waitForPeers = new Runnable() {

		public void run() {
			
			WifiP2pDeviceList myPeers = ((WifiBroadcast) mReceiver).getPeers();
			
			if(myPeers != null) {
				
				peersLst.addAll(myPeers.getDeviceList());
				
				specialPeers.addAll(myPeers.getDeviceList());
				WifiP2pDevice device = specialPeers.get(0);
				
				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = device.deviceAddress;
				
				mManager.connect(mChannel, config, new ActionListener() {

		
				    @Override
				    public void onSuccess() {
				    	Toast.makeText(getBaseContext(), " connected", Toast.LENGTH_SHORT).show();
				    }
		
				    @Override
				    public void onFailure(int reason) {
				    	Toast.makeText(getBaseContext(), " fail", Toast.LENGTH_SHORT).show();
				    }
				});
				
				waitPeers.removeCallbacks(waitForPeers);
			
			} else waitPeers.postDelayed(this, 1000);
			}
	};
	
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
