import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/*
 * Pulse() -> getPulse() -> Client1 
 * Pulse() -> getPulse() -> Client2
 * 
 * Client1 -> sendServer() -> getRequest() -> sendRequest() -> Client2
 * Client2 -> sendServer() -> getRequest() -> sendRequest() -> Client1
 * 
 * */
public class Server {
	
	private static int GET_PORT_NUMBER=2345;
	
	private static int SEND_PORT_NUMBER=1234;
	
	private static int PULSE_PORT_NUMBER=5678;
	
	private static InetAddress Client1=null;
	
	private static InetAddress Client2=null;
	
	private static int turn=1; 
	
	public static Object deserialize(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is;
	    Data ret=null;
		try {
			is = new ObjectInputStream(in);
			
			ret=(Data)is.readObject();
			
		
		} catch (IOException e) {
			System.out.println("IOException deserialize()");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound deserialize()");
			e.printStackTrace();
		}
		return ret;
	   
	}
	
	public static void getRequest() {
		DatagramSocket ds;
		
		try {
			ds = new DatagramSocket(GET_PORT_NUMBER);
			while(true) {

				DatagramPacket DpRecieve= null;
				byte[] recieve= new byte[65535];

				DpRecieve= new DatagramPacket(recieve, recieve.length);
				
				ds.receive(DpRecieve);
				
				Data data= (Data)deserialize(recieve);
				
				System.out.println("Client:- "+ data.getClientAddress()+ " Client Command: " + data.getMessage());
				
				Data send= new Data(data.getClientId(), data.getMessage(),data.getOpponent(),data.getClientAddress(),true);
				
				System.out.println(data.getClientAddress());
				
				sendRequest(send);
				recieve= new byte[65535];
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
			//System.out.println(data.getClientAddress() + "  " + data.getOpponent());

			DatagramSocket ds= new DatagramSocket();
			InetAddress address= data.getClientAddress(); // THE CLIENT'S ADDRESS
			byte[] byteArray= data.getBytes();
			DatagramPacket packet= new DatagramPacket(byteArray, byteArray.length, address,SEND_PORT_NUMBER);
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
		if(ClientAddress==Client1) {
			Data data= new Data(0,"",Client1, Client2, true);
			return data;
		}else {
			Data data= new Data(0,"",Client2,Client1, true);
			return data;
		}
	}
	
	// to find out the list of all the available clients
	public static void getPulse() {
		DatagramSocket ds;
		
		try {
			ds = new DatagramSocket(PULSE_PORT_NUMBER);
			while(true) {

				DatagramPacket DpRecieve= null;
				byte[] recieve= new byte[65535];

				DpRecieve= new DatagramPacket(recieve, recieve.length);
				
				ds.receive(DpRecieve);
				
				Data data= (Data)deserialize(recieve);
				
				
				if(Client1==null && Client2!=data.getClientAddress()) {
					Client1=data.getClientAddress();
					System.out.println("Client1 Address:- " + data.getClientAddress());

				}else if(Client2==null && Client1!=data.getClientAddress()) {
					Client2=data.getClientAddress();
					System.out.println("Client2 Address:- " + data.getClientAddress());

				}
				
				if(Client1!=null && Client2!=null /*&& Client1!=Client2*/) {
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
		
	// to send a signal saying that the turn has been finished (miss, hit or sunk)
	public static void Player1(Data data) {
		
	}
	
	public static void Player2(Data data) {
		
		
	}
	
	public static void main(String args[]) {
		
		getPulse();
		
		System.out.println("Client 1 = " + Client1);
		System.out.println("Client 2 = " + Client2);
		
		Data P1= new Data(0,"Hello Client2 from Client1",Client1,Client2,true);
		Data P2= new Data(0,"Hello Client1 from Client2",Client2,Client1,true);
		
		/*for(int i=0;i<10;i++) {
			if(i%2==0) {
				//Player1(P1);
				sendRequest(P1);
			}else {
				//Player2(P2);
				sendRequest(P2);
			}
		}*/
		
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		getRequest();

	}
	


}
