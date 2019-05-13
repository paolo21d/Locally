package pckLocally;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;

public class Communication extends Thread {
    private final int communicationPort = 10000;
    private final int communicationPortTCP = 10001;
    private final int sendPort = 10002;
    private final int receivePort = 10003;
    private Controller controller;
    private int pin;

    /*private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String message;*/

    //TODO  Zmienic komunikacje, caly czas bedzie wymiana danych
    Communication(Controller c) {
        controller = c;
        Random generator = new Random();
        pin = generator.nextInt(1000) + 8999;
    }

    int getPin() {
        return pin;
    }

    public void run() {
        System.out.println("Thread start");

        if (!initConnection()) { //nie udalo sie nawiazac polaczenia

        }

        SendThread sendThread = new SendThread();
        ReceiveThread receiveThread = new ReceiveThread();
        sendThread.start();
        receiveThread.start();

        try {
            sendThread.join();
            receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        connectTCP();
//        while (true) { //ciagle odbieram komendy i tylko na nie reaguje
//            System.out.println("Receiving...");
//            message = receiveData();
//
//            if (message.equals("Command:PLAY")) {
//                controller.playPause();
//                System.out.println("PLAY");
//            } else if (message.equals("Command:PAUSE")) {
//                controller.playPause();
//                System.out.println("PAUSE");
//            } else if (message.equals("Command:NEXT")) {
//                controller.nextSong();
//                System.out.println("NEXT");
//            } else if (message.equals("Command:PREV")) {
//                controller.prevSong();
//                System.out.println("PREV");
//            }
//
//        }

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
            /*while (keepConnect) {

            }*/
        }

        public void send(String msg) {
            out.println(msg);
        }
    }

    class ReceiveThread extends Thread {
        boolean keepConnect = true;
        String message;
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
                message = receive();
                if(message == null){
                    System.out.println("Close communication");
                    keepConnect=false;
                    break;
                }
                if (message.equals("Command:PLAY")) {
                    controller.playPause();
                    //controller.playPauseButtonClick(new Event());
                    System.out.println("PLAY");
                } else if (message.equals("Command:PAUSE")) {
                    controller.playPause();
                    System.out.println("PAUSE");
                } else if (message.equals("Command:NEXT")) {
                    controller.nextSong();
                    System.out.println("NEXT");
                } else if (message.equals("Command:PREV")) {
                    controller.prevSong();
                    System.out.println("PREV");
                }
            }
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
}
