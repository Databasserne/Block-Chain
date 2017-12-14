package com.databasserne.controller;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
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

    public void serverStart() {
        try {
            serverSocket = new ServerSocket(serverPort);
            new Thread(new Runnable() {
                public void run() {

                    while(true) {
                        try {
                            System.out.println("Waiting");
                            Socket newSocket = serverSocket.accept();
                            SocketHandler socketHandler = new SocketHandler(self, newSocket);
                            serverClients.add(socketHandler);
                            new Thread(socketHandler).start();
                            System.out.println("We have liftoff");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(List<String> peers) {
        try {
            for (String peer : peers) {
                try {
                    String[] peerData = peer.split(":");
                    socket = new Socket(peerData[0], Integer.parseInt(peerData[1]));
                    SocketHandler socketHandler = new SocketHandler(self, socket);
                    serverClients.add(socketHandler);
                    new Thread(socketHandler).start();
                } catch (ConnectException e) {
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
