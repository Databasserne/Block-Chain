package com.databasserne.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.bcel.internal.generic.SWITCH;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable {

    private ConnectionController connectionController;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonParser parser = new JsonParser();
    private Socket socket;

    public SocketHandler(ConnectionController connectionController, Socket socket) {
        this.connectionController = connectionController;
        this.socket = socket;
    }

    public void run() {
        System.out.println("Started");
        try {
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
                    //out.println("Her er min dimmer");
                    write(socket, "Her er min seneste");

                } else if (type.equals("query_all")) {

                } else if (type.equals("response_blockchain")) {

                } else {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException nullPointer) {
            connectionController.closeConnection(this);
        }
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public static void write(Socket socket, String message) throws IOException {
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         out.println(message);
    }
}
