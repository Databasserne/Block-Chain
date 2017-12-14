package com.databasserne.controller;

import java.io.IOException;
import java.util.List;

public interface IConnection {
    void connect(List<String> peers);
    void writeToAll(String message) throws IOException;
}
