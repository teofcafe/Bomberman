package cmov.bomberman.menu;

import java.util.ArrayList;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class ClientActivity extends MultiplayerGameActivity {

	@SuppressWarnings("rawtypes")
	private ArrayList peersLst = new ArrayList();
	ArrayList<WifiP2pDevice> specialPeers = new ArrayList<WifiP2pDevice>();
	Handler waitPeers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mReceiver = new ClientWifiBroadcast(mManager, mChannel, this);

		waitPeers = new Handler();
		waitPeers.postDelayed(searchForPeers, 1000);

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

	Runnable searchForPeers = new Runnable() {

		@SuppressWarnings("unchecked")
		public void run() {

			WifiP2pDeviceList myPeers = ((ClientWifiBroadcast) mReceiver).getPeers();

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

				waitPeers.removeCallbacks(searchForPeers);

			} else waitPeers.postDelayed(this, 1000);
		}
	};
}
