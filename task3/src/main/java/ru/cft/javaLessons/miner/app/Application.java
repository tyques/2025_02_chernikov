package ru.cft.javaLessons.miner.app;

import ru.cft.javaLessons.miner.app.controller.GameController;
import ru.cft.javaLessons.miner.app.repository.AchievementRepository;
import ru.cft.javaLessons.miner.app.repository.FileAchievementRepository;
import ru.cft.javaLessons.miner.app.service.GameEngine;
import ru.cft.javaLessons.miner.app.service.GridService;
import ru.cft.javaLessons.miner.view.*;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow view = new MainWindow();
            AchievementRepository achievementRepository = new FileAchievementRepository();
            GridService gridService = new GridService();
            GameEngine model = new GameEngine(gridService, achievementRepository);

            new GameController(model, view);

            view.setVisible(true);
        });
    }
}
