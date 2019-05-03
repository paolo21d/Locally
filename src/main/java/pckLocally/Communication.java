package pckLocally;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;

public class Communication extends Thread {
    private int communicationPort = 10000;
    private int communicationPortTCP = 10001;
    private MP3Player player;
    private int pin;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String message;


    Communication(MP3Player p) {
        player = p;
        Random generator = new Random();
        pin = generator.nextInt(1000) + 8999;
    }

    int getPin() {
        return pin;
    }

    public void run() {
        System.out.println("Thread start");

        //UDP - client is searching my IP address, I response to give him my IP address
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
        }

        /////////client has my IP address
        //connect via TCP
        try {
            serverSocket = new ServerSocket(communicationPortTCP);
            clientSocket = serverSocket.accept();
            System.out.println("Accepted");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //we have connection
        //out.println("Tak sie wysyla dane");
        while (true) { //ciagle odbieram komendy i tylko na nie reaguje
            System.out.println("Receiving...");
            try {
                message = in.readLine();

                if (message.equals("Command:PLAY")) {
                    player.play();
                    System.out.println("PLAY");
                } else if (message.equals("Command:PAUSE")) {
                    player.pause();
                    System.out.println("PAUSE");
                } else if (message.equals("Command:NEXT")) {

                } else if (message.equals("Command:PREV")) {

                }

            } catch (SocketException e) {
                System.out.println("Connection lost...");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
