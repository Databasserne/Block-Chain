package com.databasserne.model;

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

    public String mine() {
        return null;
    }
}
