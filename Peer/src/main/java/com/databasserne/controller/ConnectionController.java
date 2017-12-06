package com.databasserne.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionController implements IConnection {

    private Socket socket;
    private int port;

    private List<String> peers = new ArrayList<String>();

    public ConnectionController(int port) {
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket("Machine name", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String write(String message) {
        return null;
    }
}
