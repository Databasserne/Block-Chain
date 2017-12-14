package com.databasserne;

import com.databasserne.controller.ConnectionController;

import java.io.IOException;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{

    private ConnectionController connectionController;

    public static void main( String[] args ) {
        new App().start();
    }

    private void start() {
        int port = 3001;
        if(System.getenv("PEER_PORT") != null) {
            port = Integer.parseInt(System.getenv("PEER_PORT"));
        }
        connectionController = new ConnectionController(port);
        connectionController.serverStart();

        while(true) {}
    }
}
