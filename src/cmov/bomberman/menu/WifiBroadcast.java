package cmov.bomberman.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;
import android.widget.Toast;

public class WifiBroadcast extends BroadcastReceiver{

	private WifiP2pManager mManager;
	private Channel mChannel;
	private GameActivity mActivity;
	PeerListListener myPeerListListener;
	WifiP2pDeviceList myPeers;
	private boolean connected = false;

	public WifiBroadcast(WifiP2pManager manager, Channel channel,
			GameActivity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mActivity = activity;
	}

	public WifiP2pDeviceList getPeers() {
		return this.myPeers;
	}

	public boolean WifiChangeState(){
		return this.connected;
	}


	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				Toast.makeText(context, "Enable !!! =)",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Disable !",
						Toast.LENGTH_LONG).show();
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			// request available peers from the wifi p2p manager. This is an
			// asynchronous call and the calling activity is notified with a
			// callback on PeerListListener.onPeersAvailable()

			if (mManager != null) {
				mManager.requestPeers(mChannel, myPeerListListener);
				mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {		
						for(int i = 0; i<peers.getDeviceList().size(); i++)
							Toast.makeText(context, (peers.getDeviceList().toString()), Toast.LENGTH_SHORT).show();
						myPeers = peers;
						Toast.makeText(context, Integer.toString(peers.getDeviceList().size()), Toast.LENGTH_SHORT).show();
					}
				});

			}

		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {



			NetworkInfo netInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

			if (netInfo.isConnected()) {
				Toast.makeText(context, " encontreiiiiiiii", Toast.LENGTH_LONG).show();	
				this.connected = true;
				mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {

					@Override
					public void onConnectionInfoAvailable(WifiP2pInfo info) {
						Toast.makeText(context, " sdfd", Toast.LENGTH_LONG).show();
						Toast.makeText(context, Boolean.toString(info.isGroupOwner), Toast.LENGTH_LONG).show();
						Toast.makeText(context, Boolean.toString(info.groupFormed), Toast.LENGTH_LONG).show();
						Toast.makeText(context, String.valueOf(info.groupOwnerAddress), Toast.LENGTH_LONG).show();

						mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {

							@Override
							public void onGroupInfoAvailable(WifiP2pGroup group) {
								Toast.makeText(context, " Group info!!!!!", Toast.LENGTH_LONG).show();
								Log.d("WiFi", "Owner device address: " + String.valueOf(group.getOwner().deviceAddress));
								Log.d("WiFi", "Owner device name: " + String.valueOf(group.getOwner().deviceName));
								Log.d("WiFi", "Owner status: " + String.valueOf(group.getOwner().status));
								Log.d("WiFi", "Primary Device Type: " + String.valueOf(group.getOwner().primaryDeviceType));
								Log.d("WiFi", "secondary Device Type: " + String.valueOf(group.getOwner().secondaryDeviceType));
								Log.d("WiFi", "Nr of clients: " + String.valueOf(group.getClientList().size()));
							}
						});

					}
				});


				//requestConnectionInfo(mChannel, (ConnectionInfoListener) mActivity);
			}
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

		}

	}
}