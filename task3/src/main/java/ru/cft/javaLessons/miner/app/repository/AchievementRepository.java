package ru.cft.javaLessons.miner.app.repository;

import ru.cft.javaLessons.miner.app.model.Achievement;
import ru.cft.javaLessons.miner.app.model.Difficulty;

public interface AchievementRepository {
    Achievement load(Difficulty difficulty);
    void save(Difficulty difficulty, Achievement achievement);
}