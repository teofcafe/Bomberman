package cmov.bomberman.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectPeerActivity extends ClientActivity {

	ArrayList<WifiP2pDevice> specialPeers = new ArrayList<WifiP2pDevice>();
	Handler waitPeers;
	ListView listView;
	List<String> peers = new ArrayList<String>();
	ArrayAdapter<String> arrayAdapter;
	private boolean selected = false;
	private int itemSelected = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		listView = (ListView) findViewById(R.id.ListaPeers);
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, peers);
		
		mReceiver = new ClientWifiBroadcast(mManager, mChannel, this);

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
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selected = true;
				itemSelected = position;
				Toast.makeText(SelectPeerActivity.this, "Selected = true", Toast.LENGTH_LONG).show();
			}

	    });
		
		new Thread(searchForPeers).start();
		
//		waitPeers = new Handler();
//		waitPeers.postDelayed(searchForPeers, 1000);
	}
	
	Runnable searchForPeers = new Runnable() {


		@SuppressWarnings("unchecked")
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			SelectPeerActivity.this.runOnUiThread(new Runnable() {
			    public void run() {
			    	Toast.makeText(SelectPeerActivity.this, "int the thread", Toast.LENGTH_SHORT).show();
			    }
			});
			
			WifiP2pDeviceList myPeers = null;
			while (myPeers == null) {
				myPeers = ((ClientWifiBroadcast) mReceiver).getPeers();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//if(myPeers != null) {
				
				//Toast.makeText(SelectPeerActivity.this, "adding peers", Toast.LENGTH_SHORT).show();

				specialPeers.addAll(myPeers.getDeviceList());
				
				for(int i = 0; i<myPeers.getDeviceList().size(); i++)
					peers.add(specialPeers.get(i).deviceName.toString());
				
				SelectPeerActivity.this.runOnUiThread(new Runnable() {
				    public void run() {
				    	listView.setAdapter(arrayAdapter);
				    }
				});
				 
				while(!selected);
				
				
				//WifiP2pDevice device = specialPeers.get(0);
				
				WifiP2pDevice device = specialPeers.get(itemSelected);
				
				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = device.deviceAddress;
				
				//Toast.makeText(getBaseContext(), "starting new activity", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(getBaseContext(), LoadingActivity.class);
				intent.putExtra("peerAddr",config.deviceAddress);
				startActivity(intent);
				
				//waitPeers.removeCallbacks(searchForPeers);
				SelectPeerActivity.this.finish();

			//}
		}
	};

}