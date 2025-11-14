package ru.cft.javaLessons.miner.app.listeners;

import ru.cft.javaLessons.miner.app.listeners.events.GameStartInfo;

@FunctionalInterface
public interface NewGameListener {
    void onNewGame(GameStartInfo info);
}