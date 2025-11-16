package ru.cft.javaLessons.miner.app.model.listeners;

@FunctionalInterface
public interface FlagCountListener {
    void onFlagCountChange(int newFlagCount);
}