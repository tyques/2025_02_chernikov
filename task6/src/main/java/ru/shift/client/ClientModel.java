package ru.shift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import ru.shift.common.Message;
import ru.shift.common.MessageType;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        log.info("Инициализация подключения: {}@{}:{}", username, address, port);
        Thread.ofVirtual().start(this::networkLoop);
    }

    public void disconnect() {
        log.info("Завершение работы клиента");
        isRunning = false;
        closeConnection();
        System.exit(0);
    }

    private void networkLoop() {
        while (isRunning) {
            try {
                if (socket == null || socket.isClosed()) {
                    log.info("Подключение к серверу...");
                    socket = new Socket(serverAddress, serverPort);
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    log.info("Socket подключен");

                    send(Message.builder()
                            .type(MessageType.LOGIN_REQUEST)
                            .content(username)
                            .timestamp(LocalDateTime.now())
                            .build());
                }

                String json = in.readUTF();
                log.debug("Входящее сообщение: {}", json);
                Message message = objectMapper.readValue(json, Message.class);
                handleMessage(message);

            } catch (IOException e) {
                if (isAuthenticated) {
                    log.warn("Соединение потеряно: {}", e.getMessage());
                }
                isAuthenticated = false;
                ui.onConnectionLost();
                closeConnection();
                try {
                    Thread.sleep(3000);
                    log.info("Попытка переподключения...");
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
                        log.info("Успешный вход в чат");
                        isAuthenticated = true;
                        ui.onLoginSuccess();
                        requestHistory(0);
                    }
                    case LOGIN_ERROR -> {
                        log.error("Ошибка входа: {}", message.getContent());
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
                        log.debug("Загружена история: {} сообщений", history.size());
                        for (Message m : history) ui.addMessage(m);
                    }
                }
            } catch (Exception e) {
                log.error("Ошибка обработки сообщения", e);
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
            log.error("Ошибка отправки сообщения", e);
        }
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            log.warn("Ошибка при закрытии ресурсов: {}", e.getMessage());
        }
    }
}