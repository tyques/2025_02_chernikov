package ru.cft.javaLessons.miner.app.model.listeners.events;

public record GameStartInfo(
        int rows,
        int cols,
        int mineCount
) {
}