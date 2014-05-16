package cmov.bomberman.menu;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

import cmov.bomberman.game.LevelProperties;
import cmov.bomberman.pair.Pair;

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
	private static String newCommand = "";
	Socket clientSocket = null;
	InetAddress addr = null;
	private int id = 1;
	private Hashtable clientsIdentification;
	public ArrayList<Socket> clients;
	public EchoThread[] threadCli = new EchoThread[4];
	boolean initialState = true;
	int idClient;

	public int getPlayerIdentification(){
		return LevelProperties.getNumberOfPlayers();
	}

	public Pair nextPlayerPosition(){
		return LevelProperties.findPlayerPositionNotDeleted(getPlayerIdentification() + 1);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clientsIdentification = new Hashtable<Integer , InetAddress>();
		clients = new ArrayList<Socket>();

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


					all:
						while(true){

							clientSocket = serverSocket.accept();

							//Get device Address
							addr = clientSocket.getInetAddress();

							if(!clientsIdentification.contains(addr)){
								LevelProperties.addPlayer(ServerActivity.this.getBaseContext(), getPlayerIdentification(), nextPlayerPosition(), (byte)(getPlayerIdentification() + 1));
								idClient= getPlayerIdentification();
								clientsIdentification.put(idClient , addr);


								//Socket especialissimo para cada cliente
								ServerActivity.this.threadCli[idClient - 2] = new EchoThread(clientSocket, idClient);
								ServerActivity.this.threadCli[idClient - 2].start();
							}
						}
				} catch (UnknownHostException e) {
					System.out.println("EX1");e.printStackTrace();
				} catch (IOException e) {
					System.out.println("EX2");
					e.printStackTrace();
				}	
			}
		});serverSocketThread.start();

	}


	class EchoThread extends Thread {
		protected Socket socket;
		private String result = "startState";

		private int playerNumber = 0;

		private BufferedReader bufferReader = null;
		private PrintWriter printWritter = null;

		public EchoThread(Socket clientSocket, int id) {
			this.socket = clientSocket;
			this.playerNumber=id;

			try {
				this.socket.setKeepAlive(true);
			} catch (SocketException e) {
				e.printStackTrace();
			}

			try {
				this.bufferReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				this.printWritter = new PrintWriter(this.socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {


			if(initialState){
				this.printWritter.println(gameBoard.getLevelProperties().getLevelName() + "|" + gameBoard.getLevelProperties().getGameDuration() + "|" + getPlayerIdentification() + "|" + getPlayerIdentification() + "|" + getCurrentMap());
				this.printWritter.flush();

				if (printWritter.checkError()) System.out.println("WRITE NOT DONE!!!!!");
				System.out.println("SENDS");
				ServerActivity.this.initialState = false;
			}


			//Fica a escutar para receber as accoes dos clientes
			System.out.println("ENVIAR");
			sendCommand("tester");

			while(!(result.equals("exit"))){
				try {

					result = bufferReader.readLine();
					System.out.println("serverside");
					System.out.println("serverside" + "####" + result);
					updateAllPlayers(1  , result);

				} catch (IOException e) {

					e.printStackTrace();
				}


			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void sendCommand(String comand) {
			this.printWritter.println(comand);
			this.printWritter.flush();
		}
	}

	public void updateAllPlayers(int i, String action){
		for (int j = 0 ; j < ServerActivity.this.threadCli.length ; ++j)
			//if (i != this.id) continue; 
			if(i == 1)continue;
			/*else */ else if (ServerActivity.this.threadCli[0] != null) ServerActivity.this.threadCli[0].sendCommand(action);
	}

}