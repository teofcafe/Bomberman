package cmov.bomberman.menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import cmov.bomberman.game.LevelProperties;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ServerActivity extends MultiplayerGameActivity {

	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	int nrOfPlayers = 1;
	boolean availableIds[] = new boolean[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		availableIds[0] = true;

		mReceiver = new ServerWifiBroadcast(mManager, mChannel, this);

		mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
			public void onSuccess() {
				Log.d("CNN", " remove group success");
			}
			public void onFailure(int reason) {
				Log.d("CNN", " remove group fail " + reason);
			}
		});

		mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
			public void onSuccess() {
				Log.d("CNN", " create group success");
				Toast.makeText(getBaseContext(), "sucesso no create", Toast.LENGTH_SHORT).show();
			}
			public void onFailure(int reason) {
				Log.d("CNN", " create group fail " + reason);
				Toast.makeText(getBaseContext(), "fail no create", Toast.LENGTH_SHORT).show();
			}
		});

		Thread serverSocketThread =  new Thread (new Runnable() {
			@SuppressWarnings("unused")
			@Override
			public void run() {
				try {        
					serverSocket = new ServerSocket(8000);

					int bytesRead;
					byte[] nm = new byte[12];

					all:
						while(true){

							clientSocket = serverSocket.accept();
							clientSocket.setKeepAlive(true);

							InputStream in = clientSocket.getInputStream();

							PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
							System.out.println("is server connecteed : " +clientSocket.isConnected());
							pw.println(gameBoard.getLevelProperties().getLevelName() + "|" + gameBoard.getLevelProperties().getGameDuration() + "|" + ++nrOfPlayers + "|" + getAvailableId() + "|" + getCurrentMap());
							pw.flush();

							if (pw.checkError()) System.out.println("Message not sent");

							//								BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));
							//								if (!bufferReader.ready()) System.out.println("Buffer not ready!!!!!");

							break;
							//								while((bytesRead = in.read(nm)) != -1){
							//									String commandReceived = new String(nm, 0, bytesRead);
							//									System.out.println(commandReceived);
							//									if(commandReceived.equals("exit"))
							//										break all;
							//								}
						}
					//clientSocket.close();
					serverSocket.close(); 

				} catch (UnknownHostException e) {
					System.out.println("EX1");e.printStackTrace();
				} catch (IOException e) {
					System.out.println("EX2");
					e.printStackTrace();
				}	
			}
		}); serverSocketThread.start();
	}

	public int getAvailableId() {
		int i;
		for(i = 0; i < 5; i++)
			if(availableIds[i] == false)
				break;
		return ++i;
	}

	@SuppressWarnings("unused")
	public String getCurrentMap() {
		String output = new String();
		char[][] map = gameBoard.getLevelProperties().getGridMap();
		
		for(int col = 0; col < LevelProperties.gridMap.length; col++) {	
			for(int line = 0; line < LevelProperties.gridMap[col].length; line++) 
				output += map[col][line];
			output+= "nl";
		}

		Log.d("WiFi", output);
		return output;		
	}
	
	
}