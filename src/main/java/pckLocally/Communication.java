package pckLocally;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Communication extends Thread{
    private int communicationPort = 10000;
    private MP3Player player;
    private int pin;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String message;
    Communication(MP3Player p){
        player = p;
        Random generator = new Random();
        pin = generator.nextInt(1000)+8999;
    }
    int getPin(){
        return  pin;
    }
    public void run() {
        System.out.println("Thread start");
        DatagramSocket udpSocket = null;
        try {
            udpSocket = new DatagramSocket(communicationPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            udpSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sentence = new String( receivePacket.getData());
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
        }

        while(true){
            try {
                udpSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*String msg1 = new String(receivePacket.getData());
            //msg1 = msg1.replace(" ", "");
            char m = msg1.charAt(0);
            //System.out.println("RECEIVED:"+msg1+" length:"+msg1.length());

            if(m == '1'){ //play
                player.play();
                System.out.println("PLAY");
            }else if(m == '2'){ //pause
                player.pause();
            }else {
                System.out.println("OTHER:"+msg1);
            }*/


            //byte
            ObjectInputStream iStream = null;
            String messageStr=null;
            try {
                iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
                messageStr = (String) iStream.readObject();
                iStream.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(messageStr);
            if(messageStr.equals("1")){
                player.play();
                System.out.println("PLAY");
            }else if(messageStr.equals("2")){
                player.pause();
                System.out.println("PAUSE");
            }
        }
        //create TCP communication
        /*System.out.println("try to connect:");
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            System.out.println("connected");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                message = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(message.equals("1")){ //play
                player.play();
            }else if(message.equals("2")){ //pause
                player.pause();
            }
        }*/
    }
}
