package pckLocally;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;

public class Communication extends Thread {
    private static Communication instance = null;

    private final int communicationPort = 10000;
    private final int communicationPortTCP = 10001;
    private final int sendPort = 10002;
    private final int receivePort = 10003;
    private Controller controller;
    private int pin;
    private MP3Player.PlayerStatus status;
    private SendThread sendThread;
    private ReceiveThread receiveThread;
    private boolean connected = false;

    /*private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String message;*/

    //TODO  Zmienic komunikacje, caly czas bedzie wymiana danych
    private Communication() {
        Random generator = new Random();
        pin = generator.nextInt(1000) + 8999;
    }

    public static Communication getInstance() {
        if (instance == null)
            instance = new Communication();
        return instance;
    }

    public void init(Controller c, MP3Player.PlayerStatus st) {
        controller = c;
        status = st;
    }

    int getPin() {
        return pin;
    }

    public void run() {
        System.out.println("Thread start");

        /*if (!initConnection()) { //nie udalo sie nawiazac polaczenia
        }*/
        while (!initConnection()) ;
        //TODO dopisac zabezpieczenie polaczenia pinem wewnatrz initConnection

        sendThread = new SendThread();
        receiveThread = new ReceiveThread();
        sendThread.start();
        receiveThread.start();

        try {
            sendThread.join();
            receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendStatus() {
        Gson json = new Gson();
//        String msg = json.toJson(status);
        Message message = new Message(MessageType.STATUS, status);
        String msg = json.toJson(message);
        if (connected)
            sendThread.send(msg);
    }

    private boolean initConnection() {
        //UDP - client is searching my IP address, I response to give him my IP address
        DatagramSocket udpSocket = null;
        try {
            udpSocket = new DatagramSocket(communicationPort);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            udpSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String sentence = new String(receivePacket.getData());
        System.out.println("RECEIVED: " + sentence);
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();
        String capitalizedSentence = sentence.toUpperCase();
        sendData = capitalizedSentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        try {
            udpSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        /////////client has my IP address
        connected = true;
        return true;
    }

    /*private boolean connectTCP() {
        //connect via TCP
        try {
            serverSocket = new ServerSocket(communicationPortTCP);
            clientSocket = serverSocket.accept();
            System.out.println("Accepted");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //we have connection
        return true;
    }

    private String receiveData() {
        String msg = "";
        try {
            msg = in.readLine();
        } catch (SocketException e) {
            System.out.println("Connection lost...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    private boolean sendData(String msg) {
        //out.println("Tak sie wysyla dane");
        out.println(msg);
        return true;
    }*/

    public void resetCommunication(){
        sendThread = null;
        receiveThread = null;
    }
    public enum MessageType {
        PLAYPAUSE, NEXT, PREV, REPLAY, LOOP, STATUS, VOLMUTE, VOLDOWN, VOLUP, SETSONG, SETVOLUME
    }

    class SendThread extends Thread {
        boolean keepConnect = true;
        private Socket clientSocket;
        private PrintWriter out;

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(sendPort);
                clientSocket = serverSocket.accept();
                System.out.println("Accepted send");
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson json = new Gson();
            //String msg = json.toJson(status);
            Message message = new Message(MessageType.STATUS, status);
            String msg = json.toJson(message);
            send(msg);
            /*while (keepConnect) {

            }*/
        }

        public void send(String msg) {
            out.println(msg);
        }
    }

    class ReceiveThread extends Thread {
        boolean keepConnect = true;
        String msg;
        private Socket clientSocket;
        private BufferedReader in;

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(receivePort);
                clientSocket = serverSocket.accept();
                System.out.println("Accepted receive");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (keepConnect) {
                msg = receive();
                if (msg == null) {
                    System.out.println("Close communication");
                    keepConnect = false;
                    break;
                }
                Gson json = new Gson();
                Message message = json.fromJson(msg, Message.class);
                if (message.messageType == MessageType.PLAYPAUSE) {
                    controller.playPause();
                    //controller.playPauseButtonClick(new Event());
                    System.out.println("PLAY");
                } else if (message.messageType == MessageType.NEXT) {
                    controller.nextSong();
                    System.out.println("NEXT");
                } else if (message.messageType == MessageType.PREV) {
                    controller.prevSong();
                    System.out.println("PREV");
                } else if (message.messageType == MessageType.REPLAY) {
                    controller.repeat();
                    System.out.println("REPEAT");
                } else if (message.messageType == MessageType.LOOP) {
                    controller.loopChange();
                    System.out.println("LOOP");
                } else if(message.messageType == MessageType.VOLMUTE){
                    controller.volumeMute();
                } else if(message.messageType == MessageType.VOLDOWN){
                    controller.volumeDown();
                }else if(message.messageType == MessageType.VOLUP){
                    controller.volumeUp();
                }else if(message.messageType == MessageType.SETSONG){
                    String title = message.song.getSongName();
                    String path = message.song.getSongPath();
                    controller.setSong(title, path);
                }
            }

//            resetCommunication();
            controller.closeCommunication();
        }

        public String receive() {
            String msg = "";
            try {
                msg = in.readLine();
            } catch (SocketException e) {
                System.out.println("Connection lost...");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }
    }

    public class Message {
        MessageType messageType;
        String message;
        Song song;
        Double volValue;
        MP3Player.PlayerStatus statusMessage;

        public Message(MessageType type, MP3Player.PlayerStatus st) {
            messageType = type;
            statusMessage = st;
        }

        public Message(MessageType type) {
            messageType = type;
        }
    }
}
