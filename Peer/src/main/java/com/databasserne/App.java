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
        connectionController = new ConnectionController(1234);
        connectionController.connect();
    }
}
