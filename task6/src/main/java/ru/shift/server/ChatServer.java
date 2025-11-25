package ru.shift.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import ru.shift.common.Message;
import ru.shift.common.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class ChatServer {
    private final int port;
    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private final List<Message> messageHistory = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatServer(int port) {
        this.port = port;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void start() {
        log.info("Запуск сервера на порту: {}", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                log.info("Новое соединение: {}", socket.getRemoteSocketAddress());
                Thread.ofVirtual().start(new ClientHandler(this, socket));
            }
        } catch (IOException e) {
            log.error("Критическая ошибка сервера", e);
        }
    }

    public boolean subscribe(String username, ClientHandler handler) {
        if (clients.putIfAbsent(username, handler) == null) {
            log.info("Пользователь {} авторизовался", username);
            broadcastSystemMessage("Клиент " + username + " присоединился к чату");
            broadcastUserList();
            return true;
        }
        log.warn("Попытка входа под занятым именем: {}", username);
        return false;
    }

    public void unsubscribe(String username) {
        if (username == null) return;
        if (clients.remove(username) != null) {
            log.info("Пользователь {} отключился", username);
            broadcastSystemMessage("Клиент " + username + " отключился");
            broadcastUserList();
        }
    }

    public void broadcast(Message message) {
        log.debug("Рассылка сообщения от {}: {}", message.getSender(), message.getType());

        if (message.getType() == MessageType.CHAT_MESSAGE) {
            messageHistory.add(message);
        }

        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    private void broadcastSystemMessage(String text) {
        Message msg = Message.builder()
                .type(MessageType.SYSTEM_MESSAGE)
                .sender("SERVER")
                .content(text)
                .timestamp(LocalDateTime.now())
                .build();
        broadcast(msg);
    }

    private void broadcastUserList() {
        String usersJson;
        try {
            usersJson = objectMapper.writeValueAsString(clients.keySet());
        } catch (Exception e) {
            log.error("Ошибка сериализации списка пользователей", e);
            return;
        }

        Message msg = Message.builder()
                .type(MessageType.USER_LIST_UPDATE)
                .content(usersJson)
                .build();

        for (ClientHandler client : clients.values()) {
            client.sendMessage(msg);
        }
    }

    public List<Message> getHistoryFromIndex(int index) {
        if (index < 0 || index >= messageHistory.size()) return Collections.emptyList();
        return new ArrayList<>(messageHistory.subList(index, messageHistory.size()));
    }

    public ObjectMapper getMapper() {
        return objectMapper;
    }
}