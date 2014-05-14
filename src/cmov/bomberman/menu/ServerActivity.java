package cmov.bomberman.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ServerActivity extends MultiplayerGameActivity {

		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
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
								pw.println("OLALALALA!!!!!");
								pw.flush();
								
								if (pw.checkError()) System.out.println("WRITE NOT DONE!!!!!");
								
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
			});serverSocketThread.start();

		}

	}
