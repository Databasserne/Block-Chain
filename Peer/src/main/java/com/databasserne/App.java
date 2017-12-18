package com.databasserne;

import com.databasserne.controller.BlockchainController;
import com.databasserne.controller.ConnectionController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    private BlockchainController blockchainController;
    private ConnectionController connectionController;

    public static void main( String[] args ) {
        try {
            new App().start();
        } catch (Exception ex) {
            System.out.println("MAIN EXCEPTION: " + ex.getMessage());
            ex.printStackTrace();
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void start() throws Exception {
        blockchainController = new BlockchainController();

        int port = 6000;
        if(System.getenv("PEER_PORT") != null) port = Integer.parseInt(System.getenv("PEER_PORT"));
        System.out.println(port);
        List<String> tmpArr = new ArrayList();
        connectionController = new ConnectionController(port);
        int serverport = connectionController.serverStart();
        if(System.getenv("PEERS") != null) {
            tmpArr.add(System.getenv("PEERS"));
            connectionController.connect(tmpArr);
        }

        while(true) {}
    }
}
