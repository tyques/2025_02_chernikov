package ru.cft.javaLessons.miner.app.controller;

import lombok.RequiredArgsConstructor;
import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.service.GameEngine;
import ru.cft.javaLessons.miner.view.ButtonType;
import ru.cft.javaLessons.miner.view.CellEventListener;
import ru.cft.javaLessons.miner.view.GameType;
import ru.cft.javaLessons.miner.view.GameTypeListener;

@RequiredArgsConstructor
public class GameController implements CellEventListener, GameTypeListener {
    private final GameEngine gameEngine;

    public void startNewGame(Difficulty difficulty) {
        gameEngine.startNewGame(difficulty);
    }

    public void startNewGame() {
        gameEngine.startNewGame(gameEngine.getCurrentDifficulty());
    }

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON -> gameEngine.revealCell(x, y);
            case RIGHT_BUTTON -> gameEngine.toggleFlag(x, y);
            case MIDDLE_BUTTON -> gameEngine.revealAdjacentCells(x, y);
        }
    }

    @Override
    public void onGameTypeChanged(GameType gameType) {
        startNewGame(mapGameTypeToDifficulty(gameType));
    }

    private Difficulty mapGameTypeToDifficulty(GameType gameType) {
        return switch (gameType) {
            case NOVICE -> Difficulty.EASY;
            case MEDIUM -> Difficulty.MEDIUM;
            case EXPERT -> Difficulty.HARD;
        };
    }
}