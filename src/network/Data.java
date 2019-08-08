package network;

import java.io.Serializable;
import java.net.InetAddress;

import org.junit.validator.PublicClassValidator;

import tools.Coordinate;
import view.MainWindow;
import view.Renderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Data implements Serializable {

    private static final long serialVersionUID = 1L;

    int clientID;

    String Message;

    boolean gameStartedFromBoard = false;

    public boolean client1Ready = false, client2Ready = false;

    public String gameMode = "";

    public int fireTargetX = -99, fireTargetY = -99;

    public ArrayList<Integer> fireTargetXList = new ArrayList<Integer>(), fireTargetYList = new ArrayList<Integer>();

    public boolean receiveResult = false;

    public ArrayList<Integer> resultIdList = new ArrayList<Integer>();

    public boolean freezing = false, releaseControl = false;

    public boolean gameOver = false;

    private InetAddress address;

    private InetAddress opponentAddress;

    private boolean gameStart;

    public String getMessage() {
        return Message;
    }

    public int getClientId() {
        return clientID;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public void setgameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    public boolean getgameStart() {
        return gameStart;
    }

    public void setOpponent(InetAddress opponentAddress) {
        this.opponentAddress = opponentAddress;
    }

    public InetAddress getOpponent() {
        return opponentAddress;
    }

    /*
    Data constructors
    */
    Data(int clientID, String Message) {
        this.clientID = clientID;
        this.Message = Message;
        this.address = null;
        gameStart = false;
    }

    public Data(int clientID, String Message, InetAddress address, InetAddress opponentAddress, boolean gameStart) {

        gameStartedFromBoard = MainWindow.startedGame;

        this.clientID = clientID;
        this.Message = Message;
        this.address = address;
        this.opponentAddress = opponentAddress;
        this.gameStart = gameStart;
    }

    /*
    getBytes() function:
        Converts the copy of this object to a byte array and returns it.
    */
    public byte[] getBytes() throws IOException {
        Data obj = new Data(clientID, Message, address, opponentAddress, gameStart);

        obj.freezing = this.freezing;
        obj.releaseControl = this.releaseControl;

        obj.gameMode = this.gameMode;

        obj.fireTargetX = this.fireTargetX;
        obj.fireTargetY = this.fireTargetY;

        obj.fireTargetXList = this.fireTargetXList;
        obj.fireTargetYList = this.fireTargetYList;

        obj.resultIdList = this.resultIdList;
        obj.receiveResult = this.receiveResult;

        obj.gameOver = this.gameOver;

        System.out.println(" this is the data obj " + " (" + obj.fireTargetX + " , " + obj.fireTargetY + " )");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteStream);
        oos.writeObject(obj);

        oos.flush();
        byte[] data = byteStream.toByteArray();
        return data;
    }

}
