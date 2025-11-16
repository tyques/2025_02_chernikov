package ru.cft.javaLessons.miner.app;

import ru.cft.javaLessons.miner.app.controller.GameController;
import ru.cft.javaLessons.miner.app.repository.AchievementRepository;
import ru.cft.javaLessons.miner.app.repository.FileAchievementRepository;
import ru.cft.javaLessons.miner.app.service.AchievementService;
import ru.cft.javaLessons.miner.app.service.GameEngine;
import ru.cft.javaLessons.miner.app.service.GridService;
import ru.cft.javaLessons.miner.app.timer.GameTimer;
import ru.cft.javaLessons.miner.view.MainWindow;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AchievementRepository achievementRepository = new FileAchievementRepository();
            GridService gridService = new GridService();
            GameTimer gameTimer = new GameTimer();
            GameEngine model = new GameEngine(gridService);
            AchievementService achievementService = new AchievementService(achievementRepository, model, gameTimer);

            GameController controller = new GameController(model);
            MainWindow view = new MainWindow(controller);

            model.addNewGameListener(view);
            model.addGridUpdateListener(view);
            model.addGameStateListener(view);
            model.addFlagCountListener(view);
            model.addGameStateListener(gameTimer);
            model.addGameStateListener(achievementService);

            gameTimer.addTimerListener(view);
            achievementService.addNewRecordListener(view);
            achievementService.addWinListener(view);

            controller.startNewGame();

            view.setVisible(true);
        });
    }
}