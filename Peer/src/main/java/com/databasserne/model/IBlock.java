package com.databasserne.model;

import java.util.List;

public interface IBlock {
    void calculateHash(int id, int nounce, String previousHash, List<String> data) throws Exception;
    String mine();
}
