package cmov.bomberman.menu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WifiBroadcast extends BroadcastReceiver{

	private WifiP2pManager mManager;
	private Channel mChannel;
	private GameActivity mActivity;
	PeerListListener myPeerListListener;
	WifiP2pDeviceList myPeers;
	

	public WifiBroadcast(WifiP2pManager manager, Channel channel,
			GameActivity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mActivity = activity;
		TextView view = new TextView(mActivity.getBaseContext());
		//new ServerAsyncTask(mActivity, view).execute();
		
		/*socketWait = new Handler();
		socketWait.post(socketWaiting);*/
	}

	public WifiP2pDeviceList getPeers(){
		return this.myPeers;
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
			
			if (mManager == null) {
                return;
            }

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
			// Respond to new connection or disconnections
			Toast.makeText(context, "Conectadoooooooooooooooooo", Toast.LENGTH_SHORT).show();
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Respond to this device's wifi state changing
		}

	}
	
	/*public static class ServerAsyncTask extends AsyncTask<Void, Void, String> {

		private final Context context;
		private final TextView statusText;

		public ServerAsyncTask(Context context, View statusText) {
			this.context = context;
			this.statusText = (TextView) statusText;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				Toast.makeText(context, "Estou a escutar", Toast.LENGTH_LONG).show();
				Log.d("CD", "escutas");
				ServerSocket serverSocket = new ServerSocket(8888);
				Log.d("CD", "Server: Socket opened");
				Socket client = serverSocket.accept();
				Log.d("CD", "Server: connection done");
				final File f = new File(Environment.getExternalStorageDirectory() + "/"
						+ context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
						+ ".jpg");

				File dirs = new File(f.getParent());
				if (!dirs.exists())
					dirs.mkdirs();
				f.createNewFile();

				Log.d("CD", "server: copying files " + f.toString());
				InputStream inputstream = client.getInputStream();
				copyFile(inputstream, new FileOutputStream(f));
				serverSocket.close();
				return f.getAbsolutePath();
			} catch (IOException e) {
				return null;
			}
		}
	
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				statusText.setText("File copied - " + result);
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + result), "image/*");
				context.startActivity(intent);
			}

		}

		@Override
		protected void onPreExecute() {
			statusText.setText("Opening a server socket");
		}

	}

	public static boolean copyFile(InputStream inputStream, OutputStream out) {
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				out.write(buf, 0, len);

			}
			out.close();
			inputStream.close();
		} catch (IOException e) {
			Log.d("CD", e.toString());
			return false;
		}
		return true;
	}	*/
}
