package ru.cft.javaLessons.miner.app.model.listeners;

import ru.cft.javaLessons.miner.app.model.listeners.events.GameStartInfo;

@FunctionalInterface
public interface NewGameListener {
    void onNewGame(GameStartInfo info);
}