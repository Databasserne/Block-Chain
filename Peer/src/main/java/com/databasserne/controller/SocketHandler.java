package com.databasserne.controller;

import com.databasserne.App;
import com.databasserne.model.Node;
import com.google.gson.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SocketHandler implements Runnable {

    private ConnectionController connectionController;
    private Gson gson = new GsonBuilder().create();
    private JsonParser parser = new JsonParser();
    private Socket socket;

    public SocketHandler(ConnectionController connectionController, Socket socket) {
        this.connectionController = connectionController;
        this.socket = socket;
    }

    public void run() {
        System.out.println("Started");

        try {
            JsonObject json = new JsonObject();
            json.addProperty("type", "query_latest");
            write(gson.toJson(json));

            BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String input;
            while((input = bf.readLine()) != null) {
                JsonObject jsonObject;
                try{
                    jsonObject = parser.parse(input).getAsJsonObject();
                } catch(JsonSyntaxException e){
                    continue;
                } catch(IllegalStateException e){
                    continue;
                }
                
                String type = jsonObject.get("type").getAsString();
                System.out.println("Type -> " + type);

                if (type.equals("query_latest")) {
                    write(responseLatestMessage());
                } else if (type.equals("query_all")) {
                    JsonArray data = new JsonArray();
                    for(Node n : BlockchainController.getBlocks()) {
                        data.add(n.toJson());
                    }
                    JsonObject obj = new JsonObject();
                    obj.addProperty("type", "response_blockchain");
                    obj.add("data", data);
                    write(gson.toJson(obj));
                } else if (type.equals("response_blockchain")) {
                    JsonArray dataArr = jsonObject.get("data").getAsJsonArray();
                    List<Node> nodes = new ArrayList<Node>();
                    for (JsonElement obj : dataArr) {
                        nodes.add(Node.fromJson(obj.getAsJsonObject()));
                    }
                    nodes.sort(new Comparator<Node>() {
                        public int compare(Node o1, Node o2) {
                            return o1.getId() - o2.getId();
                        }
                    });

                    Node latestBlockReceived = nodes.get(nodes.size()-1);
                    Node latestBlockHeld = BlockchainController.getLatestBlock();

                    if(latestBlockReceived.getId() > latestBlockHeld.getId()) {
                        System.out.println("Possibly behind. We got: " + latestBlockHeld.getId() + ", Peer got: " + latestBlockReceived.getId());
                        if(latestBlockHeld.getHash().equals(latestBlockReceived.getPrevious())) {
                            System.out.println("The received is just one block further. We add this to our blockchain.");
                            BlockchainController.addToBlockchain(latestBlockReceived);
                            connectionController.writeToAll(responseLatestMessage());
                        } else if(nodes.size() == 1) {
                            System.out.println("We have to query the chain from our Peer.");
                            JsonObject jsonObj = new JsonObject();
                            jsonObj.addProperty("type", "query_all");
                            connectionController.writeToAll(gson.toJson(jsonObj));
                        } else {
                            System.out.println("Received blockchain is longer than current blockchain.");
                            if(BlockchainController.replaceBlockchain(nodes)) {
                                System.out.println("Blockchain has been replaced.");
                                connectionController.writeToAll(responseLatestMessage());
                            } else {
                                System.out.println("New blockchain is not valid and can therefore not be replaced.");
                            }
                        }
                    } else {
                        System.out.println("Received blockchain is not longer than the held blockchain. Do nothing.");
                    }

                } else {
                }
                System.out.println("New Blockchain look like this: " + BlockchainController.getBlockchainSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException nullPointer) {
            connectionController.closeConnection(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String responseLatestMessage() {
        JsonArray data = new JsonArray();
        data.add(BlockchainController.getLatestBlock().toJson());
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "response_blockchain");
        obj.add("data", data);
        return gson.toJson(obj);
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public void write(String message) throws IOException {
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         out.println(message);
    }
}
