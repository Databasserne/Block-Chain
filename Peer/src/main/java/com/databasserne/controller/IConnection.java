package com.databasserne.controller;

public interface IConnection {
    void connect();
    String write(String message);
}
