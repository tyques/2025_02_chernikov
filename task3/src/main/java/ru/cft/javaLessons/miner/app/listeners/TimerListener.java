package ru.cft.javaLessons.miner.app.listeners;

@FunctionalInterface
public interface TimerListener {
    void onTimeChange(int seconds);
}