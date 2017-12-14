package com.databasserne.model;

import com.databasserne.controller.security.Sha3;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Node implements IBlock {

    private int id;
    private int nounce;
    private String data;
    private String hash;
    private String previous;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNounce() {
        return nounce;
    }

    public void setNounce(int nounce) {
        this.nounce = nounce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public static String calculateHash(Node n) throws Exception {
        return Sha3.encode(String.valueOf(n.getId()) + String.valueOf(n.getNounce()) + n.getPrevious() + n.getData());
    }

    public String mine() {
        return null;
    }
    
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("nounce", nounce);
        obj.addProperty("previousHash", previous);
        obj.addProperty("data", data);
        obj.addProperty("hash", hash);
        return obj;
    }

    public static Node fromJson(JsonObject jsonObject) {
        Node node = new Node();
        node.id = jsonObject.get("id").getAsInt();
        node.nounce = jsonObject.get("nounce").getAsInt();
        node.previous = jsonObject.get("previousHash").getAsString();
        node.data = jsonObject.get("data").getAsString();
        node.hash = jsonObject.get("hash").getAsString();

        return node;
    }

    public static Node getGenesisBlock() throws Exception {
        Node initialBlock = new Node();
        initialBlock.setId(1);
        initialBlock.setData("my genesis block!!");
        initialBlock.setPrevious("0");
        initialBlock.setHash(Node.calculateHash(initialBlock));
        return initialBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        if (nounce != node.nounce) return false;
        if (data != null ? !data.equals(node.data) : node.data != null) return false;
        if (hash != null ? !hash.equals(node.hash) : node.hash != null) return false;
        return previous != null ? previous.equals(node.previous) : node.previous == null;
    }
}
