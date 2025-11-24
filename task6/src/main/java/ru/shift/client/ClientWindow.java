package ru.shift.client;

import ru.shift.common.Message;
import ru.shift.common.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientWindow extends JFrame implements ClientUI {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ClientModel model;
    private JTextArea chatArea;
    private JTextArea messageInput;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JButton sendButton;
    private JButton disconnectButton;

    public ClientWindow() {
        this.model = new ClientModel(this);
        initLoginDialog(); // Сначала просим данные
    }

    private void initLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Вход в чат", true);
        loginDialog.setLayout(new GridLayout(4, 2));
        loginDialog.setSize(300, 200);
        loginDialog.setLocationRelativeTo(null);

        JTextField ipField = new JTextField("localhost");
        JTextField portField = new JTextField("8189");
        JTextField nameField = new JTextField("User");
        JButton connectBtn = new JButton("Подключиться");
        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        loginDialog.add(new JLabel("IP адрес:"));
        loginDialog.add(ipField);
        loginDialog.add(new JLabel("Порт:"));
        loginDialog.add(portField);
        loginDialog.add(new JLabel("Имя:"));
        loginDialog.add(nameField);
        loginDialog.add(connectBtn);
        loginDialog.add(errorLabel);

        connectBtn.addActionListener(e -> {
            try {
                String ip = ipField.getText();
                int port = Integer.parseInt(portField.getText());
                String name = nameField.getText();

                if (name.isEmpty()) {
                    errorLabel.setText("Введите имя");
                    return;
                }

                model.connect(ip, port, name);
                loginDialog.dispose();
                initMainWindow();
                setVisible(true);

            } catch (NumberFormatException ex) {
                errorLabel.setText("Неверный порт");
            }
        });

        loginDialog.setVisible(true);
    }

    private void initMainWindow() {
        setTitle("Чат 2025");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setPreferredSize(new Dimension(150, 0));
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JLabel("Участники:"), BorderLayout.NORTH);
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
        add(userPanel, BorderLayout.EAST);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageInput = new JTextArea(3, 0);
        sendButton = new JButton("Отправить");
        disconnectButton = new JButton("Отключиться");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(sendButton);
        buttonPanel.add(disconnectButton);

        inputPanel.add(new JScrollPane(messageInput), BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        disconnectButton.addActionListener(e -> model.disconnect());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                model.disconnect();
            }
        });
    }

    private void sendMessage() {
        String text = messageInput.getText().trim();
        if (!text.isEmpty()) {
            model.sendTextMessage(text);
            messageInput.setText("");
        }
    }

    @Override
    public void onLoginSuccess() {
        chatArea.append("--- Вы успешно подключились к серверу ---\n");
    }

    @Override
    public void onLoginError(String error) {
        JOptionPane.showMessageDialog(this, "Ошибка входа: " + error, "Ошибка", JOptionPane.ERROR_MESSAGE);
        // В идеале тут нужно вернуть пользователя на экран логина
        System.exit(0);
    }

    @Override
    public void onConnectionLost() {
        chatArea.append("!!! Соединение потеряно. Переподключение... !!!\n");
    }

    @Override
    public void addMessage(Message message) {
        String time = message.getTimestamp().format(TIME_FORMAT);
        if (message.getType() == MessageType.SYSTEM_MESSAGE) {
            chatArea.append(String.format("[%s] СИСТЕМА: %s\n", time, message.getContent()));
        } else {
            chatArea.append(String.format("[%s] %s: %s\n", time, message.getSender(), message.getContent()));
        }
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    @Override
    public void updateUserList(List<String> users) {
        userListModel.clear();
        users.forEach(userListModel::addElement);
    }
}