package ru.shift.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        System.out.println("Сервер запущен на порту: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                // Java 21: Virtual Thread для каждого клиента
                Thread.ofVirtual().start(new ClientHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean subscribe(String username, ClientHandler handler) {
        if (clients.containsKey(username)) return false;
        clients.put(username, handler);
        broadcastSystemMessage("Клиент " + username + " присоединился к чату");
        broadcastUserList();
        return true;
    }

    public synchronized void unsubscribe(String username) {
        if (username != null && clients.containsKey(username)) {
            clients.remove(username);
            broadcastSystemMessage("Клиент " + username + " отключился");
            broadcastUserList();
        }
    }

    public void broadcast(Message message) {
        // Сохраняем в историю только текстовые сообщения
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