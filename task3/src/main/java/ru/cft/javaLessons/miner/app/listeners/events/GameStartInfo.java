package ru.cft.javaLessons.miner.app.listeners.events;

public record GameStartInfo(
        int rows,
        int cols,
        int mineCount
) {
}