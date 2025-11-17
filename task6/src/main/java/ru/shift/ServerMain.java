package ru.shift;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerMain {
    public static final int PORT = 8899;

    private static final Collection<Socket> clients = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        new Thread(() -> {
            Random rnd = new Random();

            try {
                while (true) {
                    List<Socket> deadClients = new ArrayList<>();

                    for (Socket client : clients) {
                        try {
                            OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
                            PrintWriter printWriter = new PrintWriter(writer, true);
                            printWriter.println("OK - " + rnd.nextInt());
                        } catch (Exception e) {
                            System.out.println("Client disconnected");
                            deadClients.add(client);
                        }
                    }

                    clients.removeAll(deadClients);

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
            }
        }).start();

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            clients.add(socket);
        }
    }
}
