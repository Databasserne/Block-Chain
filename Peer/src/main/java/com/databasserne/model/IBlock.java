package com.databasserne.model;

import java.util.List;

public interface IBlock {
    void calculateHash() throws Exception;
    String mine();
}
