package ru.shift;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    public static final int PORT = 8899;

    public static void main(String[] args) throws IOException {
        // telnet wikipedia.org 80
        // GET / HTTP/1.1

        try (Socket socket = new Socket("localhost", PORT)) {
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
