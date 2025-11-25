package ru.shift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.shift.common.Message;
import ru.shift.common.MessageType;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class ClientModel {
    private final ObjectMapper objectMapper;
    private final ClientUI ui;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String serverAddress;
    private int serverPort;
    private String username;

    private boolean isRunning = true;
    private boolean isAuthenticated = false;

    public ClientModel(ClientUI ui) {
        this.ui = ui;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void connect(String address, int port, String username) {
        this.serverAddress = address;
        this.serverPort = port;
        this.username = username;

        Thread.ofVirtual().start(this::networkLoop);
    }

    public void disconnect() {
        isRunning = false;
        closeConnection();
        System.exit(0);
    }

    private void networkLoop() {
        while (isRunning) {
            try {
                if (socket == null || socket.isClosed()) {
                    socket = new Socket(serverAddress, serverPort);
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());

                    send(Message.builder()
                            .type(MessageType.LOGIN_REQUEST)
                            .content(username)
                            .timestamp(LocalDateTime.now())
                            .build());
                }

                String json = in.readUTF();
                Message message = objectMapper.readValue(json, Message.class);
                handleMessage(message);

            } catch (IOException e) {
                isAuthenticated = false;
                ui.onConnectionLost();
                closeConnection();
                try {
                    Thread.sleep(3000);
                    System.out.println("Попытка переподключения...");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void handleMessage(Message message) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                switch (message.getType()) {
                    case LOGIN_SUCCESS -> {
                        isAuthenticated = true;
                        ui.onLoginSuccess();
                        requestHistory(0);
                    }
                    case LOGIN_ERROR -> {
                        ui.onLoginError(message.getContent());
                    }
                    case CHAT_MESSAGE, SYSTEM_MESSAGE -> ui.addMessage(message);
                    case USER_LIST_UPDATE -> {
                        List<String> users = objectMapper.readValue(message.getContent(), new TypeReference<>() {
                        });
                        ui.updateUserList(users);
                    }
                    case HISTORY_RESPONSE -> {
                        List<Message> history = objectMapper.readValue(message.getContent(), new TypeReference<>() {
                        });
                        for (Message m : history) ui.addMessage(m);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendTextMessage(String text) {
        if (!isAuthenticated) return;
        send(Message.builder()
                .type(MessageType.CHAT_MESSAGE)
                .content(text)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public void requestHistory(int fromIndex) {
        send(Message.builder()
                .type(MessageType.HISTORY_REQUEST)
                .index(fromIndex)
                .build());
    }

    private void send(Message msg) {
        try {
            if (out != null) {
                out.writeUTF(objectMapper.writeValueAsString(msg));
            }
        } catch (IOException e) {
        }
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}