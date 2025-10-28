package ru.cft.javaLessons.miner.app.controller;

import ru.cft.javaLessons.miner.app.model.Achievement;
import ru.cft.javaLessons.miner.app.model.Cell;
import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.service.GameEngine;
import ru.cft.javaLessons.miner.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameController implements CellEventListener, GameTypeListener {
    private final GameEngine gameEngine;
    private final MainWindow view;
    private SettingsWindow settingsWindow;
    private HighScoresWindow highScoresWindow;
    private Timer timer;

    public GameController(GameEngine gameEngine, MainWindow view) {
        this.gameEngine = gameEngine;
        this.view = view;
        init();
    }

    private void init() {
        this.settingsWindow = new SettingsWindow(view);
        this.highScoresWindow = new HighScoresWindow(view);

        this.timer = new Timer(1000, this::updateTimer);
        this.view.setCellListener(this);
        this.settingsWindow.setGameTypeListener(this);

        setupMenuListeners();
        startNewGame(Difficulty.EASY);
    }

    private void setupMenuListeners() {
        view.setNewGameMenuAction(e -> startNewGame(gameEngine.getCurrentDifficulty()));
        view.setSettingsMenuAction(e -> openSettings());
        view.setHighScoresMenuAction(e -> openHighScores());
        view.setExitMenuAction(e -> view.dispose());
    }

    private void startNewGame(Difficulty difficulty) {
        timer.stop();
        gameEngine.startNewGame(difficulty);
        view.createGameField(difficulty.getRows(), difficulty.getCols());
        updateView();
    }

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        if (gameEngine.getGameState() == GameState.WON || gameEngine.getGameState() == GameState.LOST) {
            return;
        }

        if (gameEngine.getGameState() == GameState.BEFORE_START && buttonType == ButtonType.LEFT_BUTTON) {
            timer.start();
        }

        switch (buttonType) {
            case LEFT_BUTTON -> gameEngine.revealCell(x, y);
            case RIGHT_BUTTON -> gameEngine.toggleFlag(x, y);
            case MIDDLE_BUTTON -> gameEngine.revealAdjacentCells(x, y);
        }

        updateView();
        checkGameEnd();
    }

    private void updateView() {
        Cell[][] grid = gameEngine.getGrid();
        for (int y = 0; y < gameEngine.getCurrentDifficulty().getRows(); y++) {
            for (int x = 0; x < gameEngine.getCurrentDifficulty().getCols(); x++) {
                view.setCellImage(x, y, mapCellToImage(grid[y][x]));
            }
        }
        view.setBombsCount(gameEngine.getMinesLeft());
        view.setTimerValue(gameEngine.getElapsedTime());
    }

    private void updateTimer(ActionEvent e) {
        if (gameEngine.getGameState() == GameState.IN_PROGRESS) {
            gameEngine.incrementTime();
            view.setTimerValue(gameEngine.getElapsedTime());
        } else {
            timer.stop();
        }
    }


    private void checkGameEnd() {
        GameState state = gameEngine.getGameState();
        if (state == GameState.WON) {
            timer.stop();
            handleWin();
        } else if (state == GameState.LOST) {
            timer.stop();
            handleLoss();
        }
    }

    private void handleWin() {
        updateView();
        if (gameEngine.isNewAchievement()) {
            AchievementWindow achievementWindow = new AchievementWindow(view);
            achievementWindow.setNameListener(name -> {
                gameEngine.updateHighScore(name);
                showWinDialog();
            });
            achievementWindow.setVisible(true);
        } else {
            showWinDialog();
        }
    }

    private void handleLoss() {
        gameEngine.revealAllMines();
        updateView();
        LoseWindow loseWindow = new LoseWindow(view);
        loseWindow.setNewGameListener(e -> startNewGame(gameEngine.getCurrentDifficulty()));
        loseWindow.setExitListener(e -> view.dispose());
    }

    private void showWinDialog() {
        WinWindow winWindow = new WinWindow(view);
        winWindow.setNewGameListener(e -> startNewGame(gameEngine.getCurrentDifficulty()));
        winWindow.setExitListener(e -> view.dispose());
    }

    private void openSettings() {
        settingsWindow.setGameType(mapDifficultyToGameType(gameEngine.getCurrentDifficulty()));
        settingsWindow.setVisible(true);
    }

    private void openHighScores() {
        Achievement novice = gameEngine.getAchievement(Difficulty.EASY);
        Achievement medium = gameEngine.getAchievement(Difficulty.MEDIUM);
        Achievement expert = gameEngine.getAchievement(Difficulty.HARD);

        highScoresWindow.setNoviceAchievement(novice.name(), novice.time());
        highScoresWindow.setMediumAchievement(medium.name(), medium.time());
        highScoresWindow.setExpertAchievement(expert.name(), expert.time());

        highScoresWindow.setVisible(true);
    }


    @Override
    public void onGameTypeChanged(GameType gameType) {
        startNewGame(mapGameTypeToDifficulty(gameType));
    }

    private GameImage mapCellToImage(Cell cell) {
        if (!cell.isRevealed()) {
            return cell.isFlagged() ? GameImage.MARKED : GameImage.CLOSED;
        } else {
            if (cell.isMine()) {
                return GameImage.BOMB;
            }
            return switch (cell.getAdjacentMines()) {
                case 0 -> GameImage.EMPTY;
                case 1 -> GameImage.NUM_1;
                case 2 -> GameImage.NUM_2;
                case 3 -> GameImage.NUM_3;
                case 4 -> GameImage.NUM_4;
                case 5 -> GameImage.NUM_5;
                case 6 -> GameImage.NUM_6;
                case 7 -> GameImage.NUM_7;
                case 8 -> GameImage.NUM_8;
                default -> GameImage.EMPTY;
            };
        }
    }

    private Difficulty mapGameTypeToDifficulty(GameType gameType) {
        return switch (gameType) {
            case NOVICE -> Difficulty.EASY;
            case MEDIUM -> Difficulty.MEDIUM;
            case EXPERT -> Difficulty.HARD;
        };
    }

    private GameType mapDifficultyToGameType(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> GameType.NOVICE;
            case MEDIUM -> GameType.MEDIUM;
            case HARD -> GameType.EXPERT;
        };
    }
}