package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import view.MainWindow;

/*
 * Pulse() -> getPulse() -> Client1 
 * Pulse() -> getPulse() -> Client2
 * 
 * Client1 -> sendServer() -> getRequest() -> sendRequest() -> Client2
 * Client2 -> sendServer() -> getRequest() -> sendRequest() -> Client1
 * 
 * */
public class Server {

	private static int GET_PORT_NUMBER = 2345;

	private static int SEND_PORT_NUMBER = 1234;

	private static int PULSE_PORT_NUMBER = 5678;

	private static InetAddress Client1 = null;

	private static InetAddress Client2 = null;

	private static boolean client1TookAction = false, client2TookAction = false;

	public static byte[] serialization(Data data) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(byteStream);
		oos.writeObject(data);
		oos.flush();
		byte[] ret = byteStream.toByteArray();
		return ret;
	}

	public static Object deserialize(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is;

		try {
			is = new ObjectInputStream(in);

			Data ret = (Data) is.readObject();
			System.out.println(" the deserializing function " + ret.fireTargetX + " ," + ret.fireTargetY);
			return ret;

		} catch (IOException e) {
			System.out.println("IOException deserialize()");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound deserialize()");
			e.printStackTrace();
		}
		return null;

	}

	public static void getRequest() {
		DatagramSocket ds;

		try {
			ds = new DatagramSocket(GET_PORT_NUMBER);
			while (true) {

				DatagramPacket DpRecieve = null;
				byte[] recieve = new byte[65535];

				DpRecieve = new DatagramPacket(recieve, recieve.length);

				ds.receive(DpRecieve);

				Data data = (Data) deserialize(recieve);

				System.out.println("SERVER::  Client- " + data.getAddress());
				// System.out.println("Client 1 = " + Client1);
				// System.out.println("Client 2 = " + Client2);
				//
				// System.out.println(
				// " Client 1 equality? " +
				// (data.getAddress().toString().compareTo(Client1.toString()) == 0));
				// System.out.println(
				// " Client 2 equality? " +
				// (data.getAddress().toString().compareTo(Client2.toString()) == 0));

				if (data.getAddress().toString().compareTo(Client1.toString()) == 0) {
					// System.out.println(" it should be client 2");
					data.setAddress(Client2);

					if (!MainWindow.client1Ready)
						data.client1Ready = true;

					data.client2Ready = true;

					System.out.println("Server client2: " + data.client2Ready);

					if (MainWindow.bothPlayersReady) {
						if (data.freezing) {
							client1TookAction = true;
							// data.releaseControl = false;
						}
					}
				} else {
					// System.out.println(" it should be client 1");
					data.setAddress(Client1);

					if (!MainWindow.client2Ready) {
						data.client2Ready = true;
						MainWindow.freezing = true;
					}

					data.client1Ready = true;

					System.out.println("Server client1: " + data.client1Ready);

					if (MainWindow.bothPlayersReady) {
						if (data.freezing) {
							client2TookAction = true;
							data.releaseControl = true;
						}
					}
				}

				if (client1TookAction & client2TookAction) {
					if (data.getAddress().toString().compareTo(Client2.toString()) == 0) {
						data.releaseControl = true;
					}

					client1TookAction = false;
					client2TookAction = false;
				}

				System.out.println("Action: " + client1TookAction + client2TookAction);

				System.out.println("SERVER::   Sending request to Client " + data.getAddress());
				sendRequest(data);
				recieve = new byte[65535];
			}

		} catch (SocketException e) {
			System.out.println("SocketException getRequest()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException getRequest()");
			e.printStackTrace();
		}

	}/**/

	private static void sendRequest(Data data) {
		try {
			DatagramSocket ds = new DatagramSocket();
			InetAddress address = data.getAddress(); // THE CLIENT'S ADDRESS
			byte[] byteArray = serialization(data);
			DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, address, SEND_PORT_NUMBER);
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

	public static Data startGame(InetAddress ClientAddress) {
		if (ClientAddress == Client1) {
			Data data = new Data(0, "", Client1, Client2, true);
			return data;
		} else {
			Data data = new Data(0, "", Client2, Client1, true);
			return data;
		}
	}

	// to find out the list of all the available clients
	public static void getPulse() {
		DatagramSocket ds;

		try {
			ds = new DatagramSocket(PULSE_PORT_NUMBER);
			while (true) {

				DatagramPacket DpRecieve = null;
				byte[] recieve = new byte[65535];

				DpRecieve = new DatagramPacket(recieve, recieve.length);

				ds.receive(DpRecieve);

				Data data = (Data) deserialize(recieve);

				if (Client1 == null && Client2 != data.getAddress()) {
					Client1 = data.getAddress();
					System.out.println("Client1 Address:- " + data.getAddress());

				} else if (Client2 == null && Client1 != data.getAddress()) {
					Client2 = data.getAddress();
					System.out.println("Client2 Address:- " + data.getAddress());

				}

				if (Client1 != null && Client2 != null /* && Client1!=Client2 */) {
					sendRequest(startGame(Client1));
					sendRequest(startGame(Client2));
					break;
				}

			}

		} catch (SocketException e) {
			System.out.println("SocketException getRequest()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException getRequest()");
			e.printStackTrace();
		}
	}

	public Server() {
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		getPulse();

		Data P1 = new Data(0, "Hello Client2 from Client1", Client1, Client2, true);
		Data P2 = new Data(0, "Hello Client1 from Client2", Client2, Client1, true);

		/*
		 * for(int i=0;i<10;i++) { if(i%2==0) { //Player1(P1); sendRequest(P1); }else {
		 * //Player2(P2); sendRequest(P2); } }
		 */

		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		getRequest();
	}

}
