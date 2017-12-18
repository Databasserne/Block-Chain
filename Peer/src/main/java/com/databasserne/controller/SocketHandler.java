package com.databasserne.controller;

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
    public Socket socket;

    public SocketHandler(ConnectionController connectionController, Socket socket) {
        this.connectionController = connectionController;
        this.socket = socket;
    }

    public void run() {
            try {
                JsonObject json = new JsonObject();
                json.addProperty("type", "query_latest");
                write(gson.toJson(json));
                JsonObject reqPeers = new JsonObject();
                reqPeers.addProperty("type", "query_peers");
                write(gson.toJson(reqPeers));
                
                BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String input;
                while ((input = bf.readLine()) != null) {
                    String type = null;
                    JsonObject jsonObject = null;
                    if (input.substring(0, 1).equals("{")) {
                        try {
                            jsonObject = parser.parse(input).getAsJsonObject();
                        } catch (JsonSyntaxException e) {
                            System.out.println("error " + e.getMessage());
                            continue;
                        } catch (IllegalStateException e) {
                            System.out.println("error 2 " + e.getMessage());
                            continue;
                        }

                        type = jsonObject.get("type").getAsString();
                    } else {
                        type = input;
                    }

                    if (type.equals("query_peers")) {
                        StringBuilder sb = new StringBuilder();
                        for (SocketHandler p : connectionController.serverClients) {
                            sb.append(p.socket.getInetAddress() + ":" + p.socket.getPort());
                        }
                        write(sb.toString());
                    } else if (type.equals("query_latest")) {
                        write(responseLatestMessage());
                    } else if (type.equals("query_all")) {
                        JsonArray data = new JsonArray();
                        for (Node n : BlockchainController.getBlocks()) {
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
                        responseBlockchain(nodes);

                    } else if (type.substring(0, 4).toLowerCase().equals("peer")) {
                        // This section is for user inputs.
                        String[] command = type.split(" ");
                        if (command.length < 2) {
                            write("Command missing.");
                        } else {
                            if (command[1].toLowerCase().equals("add")) {
                                Node n = new Node();
                                n.setId(BlockchainController.getBlocks().get(BlockchainController.getBlockchainSize() - 1).getId() + 1);
                                n.setNounce(0);
                                n.setPrevious(BlockchainController.getBlocks().get(BlockchainController.getBlockchainSize() - 1).getHash());
                                if (command.length < 3 || command[2] == null || command[2].equals("")) {
                                    n.setData("");
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 2; i < command.length; i++) {
                                        sb.append(command[i] + " ");
                                    }
                                    n.setData(sb.toString());
                                }
                                n.mine();

                                BlockchainController.addToBlockchain(n);
                                JsonArray data = new JsonArray();
                                for (Node node : BlockchainController.getBlocks()) {
                                    data.add(node.toJson());
                                }
                                System.out.println("Hej 2");
                                JsonObject obj = new JsonObject();
                                obj.addProperty("type", "response_blockchain");
                                obj.add("data", data);
                                connectionController.writeToAll(gson.toJson(obj));

                            } else if (command[1].toLowerCase().equals("mine")) {
                                if (command.length < 3 || command[2] == null || command[2].equals("")) {
                                    write("Missing command.");
                                } else {
                                    int id = -1;
                                    try {
                                        id = Integer.parseInt(command[2]);
                                    } catch (NumberFormatException number) {
                                        write("ID of block to mine, is needed.");
                                    }
                                    Node nodeToMine = null;
                                    for (Node n : BlockchainController.getBlocks()) {
                                        if (n.getId() == id) nodeToMine = n;
                                    }
                                    if (nodeToMine == null) write("Could not find block with that ID");
                                    StringBuilder sb = new StringBuilder();
                                    for (String cmd : command) {
                                        sb.append(cmd + " ");
                                    }
                                    write("Choosing");
                                    String[] data = sb.toString().split("\"");
                                    for (String s : data) {
                                        write("Data: " + s);
                                    }
                                    if (data[1].contains("-r") || data[2].contains("-r")) {
                                        write("Recursive");
                                        // Recursive mining.
                                        if (!data[1].equals("-r")) nodeToMine.setData(data[1]);
                                        if (data.length >= 3 && !data[2].equals("-r")) nodeToMine.setData(data[2]);
                                        System.out.println("Got HASH -> " + nodeToMine.mine(0));
                                    } else {
                                        write("Not recursive");
                                        nodeToMine.setNounce(0);
                                        nodeToMine.setData(data[1]);
                                        System.out.println("Got HASH -> " + nodeToMine.mine());
                                    }
                                }
                            } else {
                                write("No such command.");
                            }
                        }
                    } else {
                        write("No such commands :(");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
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

    private void responseBlockchain(List<Node> nodes) throws Exception {

        Node latestBlockReceived = nodes.get(nodes.size() - 1);
        Node latestBlockHeld = BlockchainController.getLatestBlock();

        if (latestBlockReceived.getId() > latestBlockHeld.getId()) {
            System.out.println("Possibly behind. We got: " + latestBlockHeld.getId() + ", Peer got: " + latestBlockReceived.getId());
            if (latestBlockHeld.getHash().equals(latestBlockReceived.getPrevious())) {
                System.out.println("The received is just one block further. We add this to our blockchain.");
                BlockchainController.addToBlockchain(latestBlockReceived);
                connectionController.writeToAll(responseLatestMessage());
            } else if (nodes.size() == 1) {
                System.out.println("We have to query the chain from our Peer.");
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("type", "query_all");
                connectionController.writeToAll(gson.toJson(jsonObj));
            } else {
                System.out.println("Received blockchain is longer than current blockchain.");
                if (BlockchainController.replaceBlockchain(nodes)) {
                    System.out.println("Blockchain has been replaced.");
                    connectionController.writeToAll(responseLatestMessage());
                } else {
                    System.out.println("New blockchain is not valid and can therefore not be replaced.");
                }
            }
        } else {
            System.out.println("Received blockchain is not longer than the held blockchain. Do nothing.");
        }
    }

    public void closeSocket() throws IOException {
        System.out.println("Lost connection.");
        write("Lost connection.");
        socket.close();
    }

    public void write(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
        }
    }
}
