package ru.cft.javaLessons.miner.app.repository;

import ru.cft.javaLessons.miner.app.model.Difficulty;

public interface RecordsRepository {
    Record load(Difficulty difficulty);
    void save(Difficulty difficulty, Record record);
}