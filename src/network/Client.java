package network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import constants.Constants;
import main.Game;
import view.MainWindow;
import view.Renderer;

import javax.swing.*;

/**
 * This class contains the client related information for the online version of
 * game.
 */
public class Client {
	private static int GET_PORT_NUMBER = 1234;

	private static int SEND_PORT_NUMBER = 2345;// port number of the get function of the server

	private static int PULSE_PORT_NUMBER = 5678;

	private static boolean STARTED_GAME = false;

	private static InetAddress SERVER_ADDRESS; // should be used in the RequestServer() method

	private static InetAddress OPPONENT_ADDRESS;

	private static InetAddress LOCAL_ADDRESS;

	private static int PLAY_SEND_PORT;

	private static int PLAY_RECIEVE_PORT;

	/*
	 * This function takes in a byte array and returns a Object type in our case the
	 * object returned is of
	 */
	public static Object deserialize(byte[] data) {

		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is;
		Data ret = null;
		try {
			is = new ObjectInputStream(in);

			ret = (Data) is.readObject();

		} catch (IOException e) {
			System.out.println("IOException deserialize()");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound deserialize()");
			e.printStackTrace();
		}
		return ret;

	}

	/*
	 * getDefaultClient creates a Data object with all the necessary
	 * 
	 * @return returns data object
	 */
	public static Data getDefaultClient() {
		return new Data(1, "", LOCAL_ADDRESS, OPPONENT_ADDRESS, true);
	}

	/*
	 * sendServer() function: sends a data object to the server using the Port
	 * Number 2345(SEND_PORT_NUMBER)
	 */

	public static void sendServer(Data data) {
		try {
			System.out.println("Sending: " + data.fireTargetX + ", " + data.fireTargetY);
			DatagramSocket ds = new DatagramSocket();
			InetAddress address = SERVER_ADDRESS;// THIS SHOULD BE THE ADDRESS OF THE SERVER
			System.out.println(address);
			data.setAddress(InetAddress.getLocalHost());// the client's local Machine Address

			byte[] byteArray = data.getBytes();
			DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, address, SEND_PORT_NUMBER);
			System.out.println("packet length " + packet.getLength());
			ds.send(packet);

			// Data test = (Data) deserialize(byteArray);
			// System.out.println("Testing deserializing " + test.getMessage());
			ds.close();

		} catch (SocketException e) {
			System.out.println("SocketException sendRequest()");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException sendRequest()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException sendRequest()");
			e.printStackTrace();
		}

	}

	/*
	 * A Java thread that is constantly looking for incoming requests from the
	 * server. The port number used is 1234 (GET_PORT_NUMBER). the datatype recieved
	 * is a byte array that is sent to the deserailize() function which returns a
	 * Data object.
	 */

	public static void recieveServer() {
		DatagramSocket ds;

		try {
			ds = new DatagramSocket(GET_PORT_NUMBER);
			while (true) {

				DatagramPacket DpRecieve = null;
				byte[] recieve = new byte[65535];

				DpRecieve = new DatagramPacket(recieve, recieve.length);

				ds.receive(DpRecieve);

				Data data = (Data) deserialize(recieve);

				STARTED_GAME = data.getgameStart();
				OPPONENT_ADDRESS = data.getOpponent();
				LOCAL_ADDRESS = data.getAddress();
				System.out.println("Game started: " + data.gameStartedFromBoard);

				if (!MainWindow.client2Ready & data.client2Ready)
					MainWindow.client2Ready = true;

				if (!MainWindow.client1Ready & data.client1Ready)
					MainWindow.client1Ready = true;

				if (!MainWindow.bothPlayersReady & MainWindow.client1Ready & MainWindow.client2Ready) {
					MainWindow.bothPlayersReady = true;
				}

				if (data.gameMode.compareToIgnoreCase("normal") == 0) {
					MainWindow.gameMode = "normal";

					MainWindow.timerLabel.setVisible(false);
				} else if (data.gameMode.compareToIgnoreCase("advanced") == 0) {
					MainWindow.gameMode = "advanced";

					MainWindow.timerLabel.setText("<html>Number of shots: " + MainWindow.numberOfPlayerShots + "/"
							+ MainWindow.numberOfPlayerMaxShots + "</html>");
					MainWindow.timerLabel.setVisible(true);
				}

				System.out.println("Client1 ready: " + MainWindow.client1Ready);
				System.out.println("Client2 ready: " + MainWindow.client2Ready);
				System.out.println("Both ready: " + MainWindow.bothPlayersReady);

				System.out.println("Fire target received from server: " + data.fireTargetX + ", " + data.fireTargetY);

				if (MainWindow.bothPlayersReady) {
					if (data.receiveResult) {
						MainWindow.receiveResultFromOpponent(data.fireTargetXList, data.fireTargetYList,
								data.resultIdList);
					} else {
						MainWindow.receiveFireTarget(data.fireTargetXList, data.fireTargetYList);
					}

					if (data.releaseControl)
						MainWindow.freezing = false;
				}

				if (data.fireTargetX < 0) {
					if (!data.releaseControl)
						MainWindow.freezing = true;
					else
						MainWindow.freezing = false;
				}

				if (data.gameOver) {
					MainWindow.gameOver = true;

					if (!MainWindow.humanBoard.checkPlayerSunkShips()) {
						ImageIcon icon = null;
						icon = new ImageIcon(Constants.FIVE_STARS);

						JOptionPane.showMessageDialog(Game.mainWindow,
								"Congratulations!! You were able to defeat your opponent.", "Game Over",
								JOptionPane.INFORMATION_MESSAGE, icon);
					}
				}

				System.out.println("Data game over: " + data.gameOver);
				System.out.println("MainWindow game over: " + MainWindow.gameOver);

				// sendServer(data);
			}

		} catch (SocketException e) {
			System.out.println("SocketException getRequest()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException getRequest()");
			e.printStackTrace();
		}

	}

	/*
	 * pulse() function: The function is activated at the beginning of the program
	 * when it is telling the server that it is ready to connect online to play
	 * opponents remotely
	 */
	public static void pulse() {
		try {
			Data data = new Data(0, "");
			DatagramSocket ds = new DatagramSocket();
			InetAddress address = SERVER_ADDRESS;// THIS SHOULD BE THE ADDRESS OF THE SERVER
			System.out.println(address);
			data.setAddress(InetAddress.getLocalHost());// the client's local Machine Address
			byte[] byteArray = data.getBytes();
			DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, address, PULSE_PORT_NUMBER);
			ds.send(packet);
			ds.close();

		} catch (SocketException e) {
			System.out.println("SocketException sendRequest()");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException sendRequest()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException sendRequest()");
			e.printStackTrace();
		}
	}

	/*
	 * createClient() function: Intializes the 1st client request to the server
	 * after connecting to an opponent.
	 */
	public static void createClient() {
		Data data = new Data(1, "", LOCAL_ADDRESS, OPPONENT_ADDRESS, true);
		sendServer(data);
	}

	/*
	 * Client() Constructor: Creates an object of type Client, it has a set server
	 * address.
	 */
	public Client() {
		try {
			SERVER_ADDRESS = InetAddress.getByName("132.205.94.11");
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException Client main()");
			e.printStackTrace();
		}

		Thread t = new Thread(new Runnable() {
			public void run() {
				recieveServer();
			}
		});

		t.start();

		System.out.println("Waiting for another player to join ... ");

		pulse();
		while (!STARTED_GAME) {
			System.out.println(STARTED_GAME);
		} /**/

		System.out.println(STARTED_GAME);

		System.out.println("The GAME HAS STARTED");

		System.out.println("Local Client " + LOCAL_ADDRESS + " Opponent Client " + OPPONENT_ADDRESS);

		createClient();
	}

}
