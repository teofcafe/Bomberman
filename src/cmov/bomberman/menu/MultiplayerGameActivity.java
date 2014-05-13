package cmov.bomberman.menu;

import java.net.InetAddress;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
	Handler waitConnect;
	
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
		
		waitConnect = new Handler();
		//waitConnect.postDelayed(waitForConnect, 1000);
		
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
	
	
	
	
	/*mManager.requestGroupInfo(mChannel, new GroupInfoListener() {
		@Override
		public void onGroupInfoAvailable(WifiP2pGroup group) {
			if (group != null && mManager != null && mChannel != null
					&& group.isGroupOwner()) {
				
				Toast.makeText(context, "Interface: " + group.getInterface(), Toast.LENGTH_LONG).show();
				Log.d("WiFi", "Interface: " + group.getInterface());
				Log.d("WiFi", "Network name: " + group.getNetworkName());
				Log.d("WiFi", "Pass: " + group.getPassphrase());
				Log.d("WiFi", "Owner name: " + group.getOwner().deviceName);
				Toast.makeText(context, group.describeContents(), Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(context, "Nao encontreiiiiiiii", Toast.LENGTH_LONG).show();	
			}
		}
	});*/
	

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
	
	
	Runnable waitForConnect = new Runnable() {
		public void run() {
			/*boolean connect = ((WifiBroadcast) mReceiver).WifiChangeState();
			if(connect) {
				Toast.makeText(getBaseContext(), "CONECTADOOOOOOOOOOOOOO", Toast.LENGTH_LONG).show();
				mManager.requestGroupInfo(mChannel, new GroupInfoListener() {
					@Override
					public void onGroupInfoAvailable(WifiP2pGroup group) {
							Log.d("WiFi", "Interface: " + group.getInterface());
							Log.d("WiFi", "Network name: " + group.getNetworkName());
							Log.d("WiFi", "Pass: " + group.getPassphrase());
							Log.d("WiFi", "Owner name: " + group.getOwner().deviceName);
							Toast.makeText(getBaseContext(), group.describeContents(), Toast.LENGTH_LONG).show();
					}
				});
				(waitConnect).removeCallbacks(waitForConnect);
			} else waitConnect.postDelayed(this, 1000);*/
			
			
			mManager.requestConnectionInfo(mChannel, new ConnectionInfoListener() {
				 
				@Override 
				public void onConnectionInfoAvailable(WifiP2pInfo info) {
	
					mManager.requestGroupInfo(mChannel, new GroupInfoListener() {
							 
							@Override 
							public void onGroupInfoAvailable(WifiP2pGroup group) {
								if (group == null)
								return; 
							} 
						}); 
					if (((WifiBroadcast) mReceiver).WifiChangeState() && !info.isGroupOwner) {
						// normally stopped already 
				//		stopDiscovery(); 
					} else { 
						// TODO if is master relaunch a discovery later ? 
						; 
					} 
		 
				} 
			});
			}
	};
	
	/*@Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {

        // InetAddress from WifiP2pInfo struct.
      //  InetAddress groupOwnerAddress = (info.groupOwnerAddress.getHostAddress());

        // After the group negotiation, we can determine the group owner.
        if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a server thread and accepting
            // incoming connections.
        } else if (info.groupFormed) {
            // The other device acts as the client. In this case,
            // you'll want to create a client thread that connects to the group
            // owner.
        }
    }*/
	
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
