import java.io.Serializable;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
public class Data implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	int clientID;
	
	String Message;
	
	private InetAddress address;
	
	private InetAddress opponentAddress;
	
	private boolean gameStart;
	
	public String getMessage() {
		return Message;
	}
	
	public int getClientId() {
		return clientID;
	}
	
	public void setAddress(InetAddress address) {
		this.address=address;
	}
	
	public InetAddress getClientAddress() {
		
		return address;

	}
	
	public void setgameStart(boolean gameStart) {
		this.gameStart=gameStart;
	}
	
	public boolean getgameStart() {
		return gameStart;
	}
	
	public void setOpponent(InetAddress opponentAddress) {
		this.opponentAddress=opponentAddress;
	}
	
	public InetAddress getOpponent() {
		return opponentAddress;
	}
	
	Data(int clientID, String Message){
		this.clientID=clientID;
		this.Message=Message;
		this.address=null;
		gameStart=false;
	}
	
	Data(int clientID, String Message, InetAddress address,InetAddress opponentAddress,boolean gameStart){
		this.clientID=clientID;
		this.Message=Message;
		this.address=address;
		this.opponentAddress=opponentAddress;
		this.gameStart=gameStart;
	}
	
	public byte[] getBytes() throws IOException {
		Data obj= new Data(clientID,Message,address,opponentAddress,gameStart);
		ByteArrayOutputStream  byteStream = new ByteArrayOutputStream ();
		ObjectOutputStream  oos= new ObjectOutputStream (byteStream );
		oos.writeObject(obj);

		oos.flush();
		byte[] data= byteStream .toByteArray();
		return data;
	}

}
