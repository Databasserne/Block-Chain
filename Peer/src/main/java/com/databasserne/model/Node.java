package com.databasserne.model;

import com.databasserne.controller.security.Sha3;

import java.util.ArrayList;
import java.util.List;

public class Node implements IBlock {

    private int id;
    private int nounce;
    private List<String> data = new ArrayList<String>();
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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
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

    public void calculateHash(int id, int nounce, String previousHash, List<String> data) throws Exception {
        StringBuilder sb = new StringBuilder();
        for(String l : data) { sb.append(l); }
        this.hash = Sha3.encode(String.valueOf(id) + String.valueOf(nounce) + previousHash + sb.toString());
    }

    public String mine() {
        return null;
    }
}
