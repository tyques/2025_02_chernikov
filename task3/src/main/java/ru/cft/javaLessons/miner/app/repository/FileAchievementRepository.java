package ru.cft.javaLessons.miner.app.repository;


import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.view.Achievement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FileAchievementRepository implements AchievementRepository {
    private static final String RECORDS_FILE = "records.properties";
    private final Properties properties;

    public FileAchievementRepository() {
        this.properties = new Properties();
        load();
    }

    @Override
    public Achievement load(Difficulty difficulty) {
        String key = difficulty.name();
        String defaultValue = "Anonymous;999";
        String recordString = properties.getProperty(key, defaultValue);
        String[] parts = recordString.split(";");
        return new Achievement(parts[0], Integer.parseInt(parts[1]));
    }

    @Override
    public void save(Difficulty difficulty, Achievement achievement) {
        String key = difficulty.name();
        String value = achievement.name() + ";" + achievement.time();
        properties.setProperty(key, value);
        try (FileOutputStream fos = new FileOutputStream(RECORDS_FILE)) {
            properties.store(fos, "Minesweeper High Scores");
        } catch (IOException e) {
            System.err.println("Error saving records: " + e.getMessage());
        }
    }

    private void load() {
        File file = new File(RECORDS_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Error loading records: " + e.getMessage());
            }
        }
    }
}