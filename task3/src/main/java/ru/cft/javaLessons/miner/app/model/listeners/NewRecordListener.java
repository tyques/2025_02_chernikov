package ru.cft.javaLessons.miner.app.model.listeners;

import java.util.function.Consumer;

@FunctionalInterface
public interface NewRecordListener {
    void onNewRecord(Consumer<String> nameConsumer);
}