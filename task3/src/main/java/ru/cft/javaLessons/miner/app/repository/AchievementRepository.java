package ru.cft.javaLessons.miner.app.repository;

import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.view.Achievement;

public interface AchievementRepository {
    Achievement load(Difficulty difficulty);
    void save(Difficulty difficulty, Achievement achievement);
}