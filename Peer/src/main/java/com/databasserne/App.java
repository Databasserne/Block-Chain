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
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void start() throws Exception {
        blockchainController = new BlockchainController();

        int port = 3001;
        if(System.getenv("PEER_PORT") != null) {
            port = Integer.parseInt(System.getenv("PEER_PORT"));
        }
        System.out.println(port);
        List<String> tmpArr = new ArrayList();
        if(System.getenv("PEERS") != null) {
            String[] peersEnv = System.getenv("PEERS").split(",");
            for(String peerEnv : peersEnv) {
                tmpArr.add(peerEnv);
            }
        }
        //tmpArr.add("localhost:3001");
        connectionController = new ConnectionController(port);
        connectionController.connect(tmpArr);
        connectionController.serverStart();

        while(true) {}
    }
}
