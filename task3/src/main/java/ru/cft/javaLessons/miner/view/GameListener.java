package ru.cft.javaLessons.miner.view;

import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.model.Grid;

public interface GameListener {
    void onNewGame(Grid grid);

    void onGridUpdate();

    void onGameStateChange(GameState newState);

    void onFlagCountChange(int newFlagCount);
}