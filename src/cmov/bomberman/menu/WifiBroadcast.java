package cmov.bomberman.menu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class WifiBroadcast extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private GameActivity mActivity;
	PeerListListener myPeerListListener;
	WifiP2pDeviceList myPeers;
	private boolean connected = false;
	String host;
	int port;
	int len;
	Socket socket = new Socket();
	byte buf[] = new byte[1024];
	//Handler startSK = new Handler();
	Thread startSK;


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
						for(int i = 0; i < peers.getDeviceList().size(); i++)
							Toast.makeText(context, (peers.getDeviceList().toString()), Toast.LENGTH_SHORT).show();
						myPeers = peers;
						Toast.makeText(context, Integer.toString(peers.getDeviceList().size()), Toast.LENGTH_SHORT).show();
					}
				});

			}

		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

			NetworkInfo netInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

			if (netInfo.isConnected()) {

				Toast.makeText(context, " Connected!", Toast.LENGTH_LONG).show();	
				this.connected = true;

				mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {

					@Override
					public void onConnectionInfoAvailable(WifiP2pInfo info) {
						Toast.makeText(context, " Connection info!", Toast.LENGTH_LONG).show();
						Log.d("WiFi", "Group owner address: " + String.valueOf(info.groupOwnerAddress));

						host = String.valueOf(info.groupOwnerAddress);
						port = 8000;
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
									
									Log.d("WiFi", "Antes");
									socket.bind(null);
									Log.d("WiFi", "Entretanto");
									Log.d("WiFi", "Host: " +  host + " Port: " + port);

									String[] hostIP = host.split("/");
									Log.d("WiFi", "Host IP: " +  hostIP[1]);
									
									String[] ip = hostIP[1].split("\\.");
									
									byte[] ipaddr = new byte[]{(byte) Integer.parseInt(ip[0]), (byte) Integer.parseInt(ip[1]), (byte) Integer.parseInt(ip[2]), (byte) Integer.parseInt(ip[3])};
									
									InetAddress addr = InetAddress.getByAddress(ipaddr);
									
									Log.d("WiFi", "IP ADDRESS: " + addr.getHostAddress());
									InetSocketAddress a = new InetSocketAddress(addr, port);
									
									socket.connect(a, 5000);
									Log.d("WiFi", "Depois");
									Log.d("WiFi", "Am I connected? " + socket.isConnected());
									
									Log.d("WiFi", "Ligar os streams");
									OutputStream outputStream = socket.getOutputStream();
									ContentResolver cr = context.getContentResolver();
									InputStream inputStream = null;
									
									//inputStream = socket.getInputStream();
									
									
									Log.d("WiFi", "enviar o ola");
									String ola = "Ola";
									buf = ola.getBytes();
									Log.d("WiFi", "enviei o ola");
//									
//									outputStream.write(buf, 0, ola.length());
//									inputStream = cr.openInputStream(Uri.parse("path/to/picture.jpg"));
//									while ((len = inputStream.read(buf)) != -1) {
//										outputStream.write(buf, 0, len);
//									}
									outputStream.close();
									//inputStream.close();
									
								} catch (FileNotFoundException e) {
									//catch logic
								} catch (IOException e) {
									//catch logic
								}
								
								finally {
								    if (socket != null) {
								        if (socket.isConnected()) {
								            try {
								                socket.close();
								            } catch (IOException e) {
								                //catch logic
								            }
								        }
								    }
								}
								
							}
						}).start();
				}

				});

				mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
					@Override
					public void onGroupInfoAvailable(WifiP2pGroup group) {
						Toast.makeText(context, " Group info!", Toast.LENGTH_LONG).show();
						Log.d("WiFi", "Owner device address: " + String.valueOf(group.getOwner().deviceAddress));
						Log.d("WiFi", "Owner device name: " + String.valueOf(group.getOwner().deviceName));
						Log.d("WiFi", "Contents: " + String.valueOf(group.describeContents()));
						Log.d("WiFi", "Owner status: " + String.valueOf(group.getOwner().status));
						Log.d("WiFi", "Nr of clients: " + String.valueOf(group.getClientList().size()));
					}
				});	

			}
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

		}
	}
	
	Runnable connectSK = new Runnable() {

		public void run() {
		
		}
	};
}
