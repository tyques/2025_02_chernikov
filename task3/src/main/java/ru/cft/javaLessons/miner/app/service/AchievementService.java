package ru.cft.javaLessons.miner.app.service;

import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.model.listeners.GameStateListener;
import ru.cft.javaLessons.miner.app.model.listeners.NewRecordListener;
import ru.cft.javaLessons.miner.app.model.listeners.WinListener;
import ru.cft.javaLessons.miner.app.repository.AchievementRepository;
import ru.cft.javaLessons.miner.app.timer.GameTimer;
import ru.cft.javaLessons.miner.view.Achievement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AchievementService implements GameStateListener {

    private final AchievementRepository repository;
    private final GameEngine gameEngine;
    private final GameTimer timer;
    private final List<NewRecordListener> newRecordListeners = new ArrayList<>();
    private final List<WinListener> winListeners = new ArrayList<>();

    public AchievementService(AchievementRepository repository, GameEngine gameEngine, GameTimer timer) {
        this.repository = repository;
        this.gameEngine = gameEngine;
        this.timer = timer;
    }

    public void addNewRecordListener(NewRecordListener listener) {
        newRecordListeners.add(listener);
    }

    public void addWinListener(WinListener listener) {
        winListeners.add(listener);
    }

    @Override
    public void onGameStateChange(GameState newState) {
        if (newState == GameState.WON) {
            checkAndNotify();
        }
    }

    private void checkAndNotify() {
        int time = timer.getSeconds();
        Difficulty difficulty = gameEngine.getCurrentDifficulty();
        Achievement oldAchievement = repository.load(difficulty);

        if (time < oldAchievement.time()) {
            Consumer<String> nameConsumer = name -> {
                if (name == null || name.isBlank()) {
                    name = "Anonymous";
                }
                repository.save(difficulty, new Achievement(name, time));
            };
            newRecordListeners.forEach(l -> l.onNewRecord(nameConsumer));
        } else {
            winListeners.forEach(WinListener::onWin);
        }
    }
}