package ru.shift.server;

public class ServerMain {
    public static void main(String[] args) {
        new ChatServer(ServerConfig.getPort()).start();
    }
}