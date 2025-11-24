package ru.shift.server;

import ru.shift.common.Message;
import ru.shift.common.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

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
                Message message = server.getMapper().readValue(json, Message.class);

                if (message.getType() == MessageType.LOGIN_REQUEST) {
                    String nick = message.getContent();
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
                Message message = server.getMapper().readValue(json, Message.class);

                if (message.getType() == MessageType.CHAT_MESSAGE) {
                    message.setSender(username);
                    // Сервер может доверять времени клиента или ставить свое
                    // message.setTimestamp(LocalDateTime.now());
                    server.broadcast(message);
                } else if (message.getType() == MessageType.HISTORY_REQUEST) {
                    sendHistory(message.getIndex());
                }
            }
        } catch (IOException e) {
            // Клиент отключился
        } finally {
            server.unsubscribe(username);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            String json = server.getMapper().writeValueAsString(message);
            out.writeUTF(json);
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}