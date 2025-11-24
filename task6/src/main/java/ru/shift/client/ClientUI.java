package ru.shift.client;

import ru.shift.common.Message;

import java.util.List;

public interface ClientUI {
    void onLoginSuccess();

    void onLoginError(String error);

    void onConnectionLost();

    void addMessage(Message message);

    void updateUserList(List<String> users);
}