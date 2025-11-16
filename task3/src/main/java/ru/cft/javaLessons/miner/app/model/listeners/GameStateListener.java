package ru.cft.javaLessons.miner.app.model.listeners;

import ru.cft.javaLessons.miner.app.model.GameState;

@FunctionalInterface
public interface GameStateListener {
    void onGameStateChange(GameState newState);
}