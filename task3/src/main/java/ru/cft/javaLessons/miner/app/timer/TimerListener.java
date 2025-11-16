package ru.cft.javaLessons.miner.app.timer;

@FunctionalInterface
public interface TimerListener {
    void onTimeChange(int seconds);
}