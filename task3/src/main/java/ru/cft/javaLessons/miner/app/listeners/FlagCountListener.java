package ru.cft.javaLessons.miner.app.listeners;

@FunctionalInterface
public interface FlagCountListener {
    void onFlagCountChange(int newFlagCount);
}