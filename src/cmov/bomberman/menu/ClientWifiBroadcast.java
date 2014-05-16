package cmov.bomberman.menu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import cmov.bomberman.game.GameBoard;
import cmov.bomberman.game.LevelProperties;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;
import android.widget.Toast;

public class ClientWifiBroadcast extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private ClientActivity mActivity;
	PeerListListener myPeerListListener;
	WifiP2pDeviceList myPeers;
	private boolean connected = false;
	InetAddress host;
	int port;
	int len;
	Socket socket = new Socket();
	byte buf[] = new byte[1024];
	ClientThread clientthread;

	public ClientWifiBroadcast(WifiP2pManager manager, Channel channel,
			ClientActivity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mActivity = activity;

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

			Log.d("WiFi", "WIFI_P2P_PEERS_CHANGED_ACTION");

			if (mManager != null) {
				mManager.requestPeers(mChannel, myPeerListListener);

				mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {

					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {	
						Log.d("WiFi", "onPeersAvailable");

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

				Toast.makeText(context, " Connected AfterRequest!", Toast.LENGTH_LONG).show();	
				this.connected = true;

				mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {

					@Override
					public void onConnectionInfoAvailable(WifiP2pInfo info) {
						Toast.makeText(context, " Connection info!", Toast.LENGTH_LONG).show();
						Log.d("WiFi", "Group owner address: " + String.valueOf(info.groupOwnerAddress));

						host =info.groupOwnerAddress;
						port = 8000;
						ContentResolver cr = context.getContentResolver();
						clientthread = new ClientThread(host, port, cr, socket);	
						clientthread.start();
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

	public WifiP2pDeviceList getPeers() {
		return this.myPeers;
	}

	class ClientThread extends Thread {
		protected Socket socket;
		InetAddress host;
		int port;
		ContentResolver cr;
		BufferedReader bufferReader;
		private PrintWriter printWritter;




		public ClientThread (InetAddress host, int port, ContentResolver cr, Socket socket){
			this.host = host;
			this.port = port;
			this.cr = cr;
			this.socket = socket;
		}

		public void sendCommand(String cpm){
			System.out.println("cpm"+ cpm);
			System.out.println("cpm"+ this.printWritter);
			this.printWritter.println(cpm);
			this.printWritter.flush();
			System.out.println("envieiiiiiiiii");
		}

		@Override
		public void run() {
			try {


				Log.d("WiFi", "Antes");
				System.out.println("ANTES");
				socket.bind(null);
				Log.d("WiFi", "Entretanto");
				Log.d("WiFi", "Host: " +  String.valueOf(host) + " Port: " + port);
				System.out.println("Host: " +  String.valueOf(host) + " Port: " + port);

				InetSocketAddress address = new InetSocketAddress(host, port);
				socket.connect(address, 5000);

				this.bufferReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				this.printWritter = new PrintWriter(this.socket.getOutputStream());

				Log.d("WiFi", "Depois");
				System.out.println("DEPOIS");
				Log.d("WiFi", "Am I connected? " + socket.isConnected());
				System.out.println("Am I connected? " + socket.isConnected());
				Log.d("WiFi", "Ligar os streams");

				System.out.println("estabeli sockets");


				while(!bufferReader.ready()); System.out.println("Buffer not ready");


				String result = bufferReader.readLine();

				Log.d("WiFi", "String: " + result);


				String[] info = result.split("\\|");


				System.out.println( "Map name:" + info[0]);
				System.out.println("Time left: " +  info[1]);
				System.out.println( "Nr of players: " +  info[2]);
				System.out.println("ID: " + info[3]);
				System.out.println("MAPA: " + info[4]);

				System.out.println("CWB");

				((LoadingActivity) mActivity).startGame(info[0], Integer.valueOf(info[1]), Integer.valueOf(info[2]), (info[3].charAt(0) - '0') , info[4]);
				//outputStream.close();
				//inputStream.close();
				while (!result.equals("exit")){
					result = bufferReader.readLine();


					//String []commands = result.split("\\|");
					//int player = Integer.parseInt(commands[0]);
					//String command = commands[1];


					if(result.equals("andaja")){
						System.out.println("RECEBIMSG");
						System.out.println("sendCommand");
						sendCommand("recebi o andaja");

					}

					else if(result.equals("right"))
						continue;
					else if(result.equals("left"))
						continue;
					else if(result.equals("up"))
						continue;
					else if(result.equals("down"))
						continue;
					else if(result.equals("bomb"))
						continue;
					else if(result.equals("stop"))
						continue;

				}socket.close();
			} catch (FileNotFoundException e) {
				Log.d("WiFi", "FileNotFoundException");
				//catch logic
			} catch (IOException e) {
				//catch logic
				Log.d("WiFi", "IOException");
			}

		}
	}
}