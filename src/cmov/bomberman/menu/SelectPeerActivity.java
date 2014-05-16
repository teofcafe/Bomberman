package cmov.bomberman.menu;

import java.util.ArrayList;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SelectPeerActivity extends ClientActivity {

	ArrayList<WifiP2pDevice> specialPeers = new ArrayList<WifiP2pDevice>();
	Handler waitPeers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, false);
		setContentView(R.layout.activity_search);
		

		mReceiver = new ClientWifiBroadcast(mManager, mChannel, this);

		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(getBaseContext(), "finding peers", Toast.LENGTH_SHORT).show();
				waitPeers = new Handler();
				waitPeers.postDelayed(searchForPeers, 1000);
			}
			@Override
			public void onFailure(int reasonCode) {
				Toast.makeText(getBaseContext(), " not finding peers", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	Runnable searchForPeers = new Runnable() {


		@SuppressWarnings("unchecked")
		public void run() {
			
			Toast.makeText(getBaseContext(), "int the loop", Toast.LENGTH_SHORT).show();
			

			WifiP2pDeviceList myPeers = ((ClientWifiBroadcast) mReceiver).getPeers();

			if(myPeers != null) {
				
				Toast.makeText(getBaseContext(), "adding peers", Toast.LENGTH_SHORT).show();

				specialPeers.addAll(myPeers.getDeviceList());
				WifiP2pDevice device = specialPeers.get(0);

				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = device.deviceAddress;
				
				Toast.makeText(getBaseContext(), "starting new activity", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(getBaseContext(), LoadingActivity.class);
				intent.putExtra("mode","multiplayer");
				intent.putExtra("role", "client");
				intent.putExtra("peerAddr",config.deviceAddress);
				startActivity(intent);
				
				waitPeers.removeCallbacks(searchForPeers);
				SelectPeerActivity.this.finish();

			} else waitPeers.postDelayed(this, 1000);
		}
	};

}
