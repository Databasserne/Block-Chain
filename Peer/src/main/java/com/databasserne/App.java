package com.databasserne;

import com.databasserne.controller.ConnectionController;

import com.databasserne.model.Node;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{

    private ConnectionController connectionController;
    
    public static ArrayList<Node> blocks = new ArrayList<Node>();

    public App() throws Exception {
        Node initialBlock = new Node();
        initialBlock.setId(1);
        initialBlock.setData("my genesis block!!");
        initialBlock.setPrevious("0");
        initialBlock.calculateHash();
        blocks.add(initialBlock);
    }

    public static void main( String[] args ) {
        try {
            new App().start();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
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
