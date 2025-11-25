package ru.shift.server;

import lombok.extern.slf4j.Slf4j;
import ru.shift.common.Message;
import ru.shift.common.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

@Slf4j
public class ClientHandler implements Runnable {
    private final ChatServer server;
    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public ClientHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String json = in.readUTF();
                log.debug("Получен JSON (auth): {}", json);
                Message message = server.getMapper().readValue(json, Message.class);

                if (message.getType() == MessageType.LOGIN_REQUEST) {
                    String nick = message.getContent();
                    log.info("Запрос на вход: {}", nick);

                    if (server.subscribe(nick, this)) {
                        this.username = nick;
                        sendMessage(Message.builder().type(MessageType.LOGIN_SUCCESS).build());
                        break;
                    } else {
                        sendMessage(Message.builder().type(MessageType.LOGIN_ERROR).content("Имя занято").build());
                    }
                }
            }

            while (true) {
                String json = in.readUTF();
                log.trace("Получен JSON от {}: {}", username, json);
                Message message = server.getMapper().readValue(json, Message.class);

                if (message.getType() == MessageType.CHAT_MESSAGE) {
                    message.setSender(username);
                    server.broadcast(message);
                } else if (message.getType() == MessageType.HISTORY_REQUEST) {
                    log.debug("Запрос истории от {} начиная с индекса {}", username, message.getIndex());
                    sendHistory(message.getIndex());
                }
            }
        } catch (EOFException e) {
            log.info("Соединение с клиентом {} разорвано (EOF)", username != null ? username : "unknown");
        } catch (IOException e) {
            log.error("Ошибка связи с клиентом {}", username, e);
        } finally {
            server.unsubscribe(username);
            try {
                socket.close();
            } catch (IOException e) {
                log.error("Ошибка при закрытии сокета", e);
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            String json = server.getMapper().writeValueAsString(message);
            out.writeUTF(json);
        } catch (IOException e) {
            log.error("Не удалось отправить сообщение пользователю {}", username, e);
        }
    }

    private void sendHistory(Integer fromIndex) {
        if (fromIndex == null) fromIndex = 0;
        List<Message> history = server.getHistoryFromIndex(fromIndex);
        try {
            String historyJson = server.getMapper().writeValueAsString(history);
            sendMessage(Message.builder()
                    .type(MessageType.HISTORY_RESPONSE)
                    .content(historyJson)
                    .build());
        } catch (Exception e) {
            log.error("Ошибка отправки истории", e);
        }
    }
}