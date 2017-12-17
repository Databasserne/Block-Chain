package com.databasserne.controller;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionController implements IConnection {

    private ConnectionController self;
    private ServerSocket serverSocket;
    private List<SocketHandler> serverClients;
    private Socket socket;
    private int serverPort;

    private List<String> peers = new ArrayList<String>();

    public ConnectionController(int serverPort) {
        this.self = this;
        this.serverPort = serverPort;
        this.serverClients = new ArrayList<SocketHandler>();
    }

    public int serverStart() throws UnknownHostException {
        while(true) {
            try {
                serverSocket = new ServerSocket(serverPort);
                new Thread(new Runnable() {
                    public void run() {

                        while (true) {
                            try {
                                System.out.println("Waiting");
                                Socket newSocket = serverSocket.accept();
                                SocketHandler socketHandler = new SocketHandler(self, newSocket);
                                serverClients.add(socketHandler);
                                new Thread(socketHandler).start();
                                System.out.println("We have liftoff");
                            } catch (IOException e) {
                                System.out.println("IO: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            } catch (IOException e) {
                System.out.println("Port: " + serverPort + " is already in use.");
                serverPort++;
                continue;
            }
            System.out.println("Created server with ip: " + InetAddress.getLocalHost().getHostAddress() + " and on port: " + serverPort);
            return serverPort;
        }
    }

    public void connect(List<String> peers) {
        try {
            System.out.println("Starting to connect.");
            for (String peer : peers) {
                System.out.println("Found something to connect to: " + peer);
                while(true) {
                    try {
                        String[] peerData = peer.split(":");
                        socket = new Socket(peerData[0], Integer.parseInt(peerData[1]));
                        System.out.println("Test");
                        SocketHandler socketHandler = new SocketHandler(self, socket);
                        System.out.println("Test2");
                        serverClients.add(socketHandler);
                        System.out.println("Test3");
                        new Thread(socketHandler).start();
                        System.out.println("Connection handler thread, created and started.");
                    } catch (ConnectException e) {
                        System.out.println("Error " + e.getMessage());
                        e.printStackTrace();
                        continue;
                    }
                    break;
                }
                System.out.println("Tester 1,2,3");
            }

        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception! " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Done?");
    }

    public void closeConnection(SocketHandler socketHandler) {
        try {
            socketHandler.closeSocket();
            serverClients.remove(socketHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeToAll(String message) throws IOException {
        for (SocketHandler handler : serverClients) {
            handler.write(message);
        }
    }
}
