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

    public void calculateHash() throws Exception {
        this.hash = Sha3.encode(String.valueOf(id) + String.valueOf(nounce) + previous + data);
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
}
