package ru.cft.javaLessons.miner.app.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cft.javaLessons.miner.app.model.Cell;
import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.model.Grid;
import ru.cft.javaLessons.miner.view.GameListener;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GameEngine {
    private final GridService gridService;
    private final List<GameListener> listeners = new ArrayList<>();
    private Grid grid;
    private GameState gameState;
    @Getter
    private Difficulty currentDifficulty = Difficulty.EASY;

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    public void startNewGame(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
        this.grid = new Grid(difficulty);
        this.gameState = GameState.BEFORE_START;
        notifyNewGame();
    }

    public void revealCell(int x, int y) {
        if (gameState == GameState.WON || gameState == GameState.LOST) {
            return;
        }

        if (gameState == GameState.BEFORE_START) {
            gridService.placeMines(grid, x, y);
            setGameState(GameState.IN_PROGRESS);
        }

        Cell cell = grid.getArea()[y][x];
        if (cell.isFlagged() || cell.isRevealed()) {
            return;
        }

        if (cell.isMine()) {
            gridService.revealAllMines(grid);
            setGameState(GameState.LOST);
            return;
        }

        gridService.revealCellRecursive(grid, x, y);
        notifyGridUpdated();
        checkWinCondition();
    }

    public void toggleFlag(int x, int y) {
        if (gameState != GameState.IN_PROGRESS && gameState != GameState.BEFORE_START) {
            return;
        }

        Cell cell = grid.getArea()[y][x];
        if (cell.isRevealed()) {
            return;
        }

        if (cell.isFlagged()) {
            cell.setFlagged(false);
            grid.decrementFlagsPlaced();
        } else {
            cell.setFlagged(true);
            grid.incrementFlagsPlaced();
        }
        notifyGridUpdated();
        notifyFlagCountChanged();
    }

    public void revealAdjacentCells(int x, int y) {
        if (gameState != GameState.IN_PROGRESS) {
            return;
        }

        Cell cell = grid.getArea()[y][x];
        if (!cell.isRevealed() || cell.getAdjacentMines() == 0) {
            return;
        }

        int flagsAround = gridService.countFlagsAround(grid, y, x);
        if (flagsAround == cell.getAdjacentMines()) {
            int revealedBefore = grid.getRevealedCells();
            boolean mineRevealed = gridService.revealNeighbors(grid, x, y);

            if (mineRevealed) {
                gridService.revealAllMines(grid);
                setGameState(GameState.LOST);
            } else {
                if (grid.getRevealedCells() > revealedBefore) {
                    notifyGridUpdated();
                }
                checkWinCondition();
            }
        }
    }

    private void checkWinCondition() {
        int totalCells = grid.getRows() * grid.getCols();
        int mineCount = grid.getCountOfMins();
        int targetCount = totalCells - mineCount;

        if (grid.getRevealedCells() >= targetCount) {
            setGameState(GameState.WON);
        }
    }

    private void setGameState(GameState newGameState) {
        if (this.gameState != newGameState) {
            this.gameState = newGameState;
            notifyGameStateChanged(newGameState);
        }
    }

    public int getMinesLeft() {
        return grid.getCountOfMins() - grid.getFlagsPlaced();
    }

    private void notifyNewGame() {
        for (GameListener listener : listeners) {
            listener.onNewGame(grid);
        }
        notifyFlagCountChanged();
    }

    private void notifyGridUpdated() {
        for (GameListener listener : listeners) {
            listener.onGridUpdate();
        }
    }

    private void notifyGameStateChanged(GameState newState) {
        for (GameListener listener : listeners) {
            listener.onGameStateChange(newState);
        }
    }

    private void notifyFlagCountChanged() {
        for (GameListener listener : listeners) {
            listener.onFlagCountChange(getMinesLeft());
        }
    }
}