package ru.cft.javaLessons.miner.app.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cft.javaLessons.miner.app.model.Cell;
import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.model.Grid;
import ru.cft.javaLessons.miner.app.model.listeners.FlagCountListener;
import ru.cft.javaLessons.miner.app.model.listeners.GameStateListener;
import ru.cft.javaLessons.miner.app.model.listeners.GridUpdateListener;
import ru.cft.javaLessons.miner.app.model.listeners.NewGameListener;
import ru.cft.javaLessons.miner.app.model.listeners.events.CellInfo;
import ru.cft.javaLessons.miner.app.model.listeners.events.GameStartInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GameEngine {
    private final GridService gridService;
    private final List<NewGameListener> newGameListeners = new ArrayList<>();
    private final List<GridUpdateListener> gridUpdateListeners = new ArrayList<>();
    private final List<GameStateListener> gameStateListeners = new ArrayList<>();
    private final List<FlagCountListener> flagCountListeners = new ArrayList<>();
    private Grid grid;
    private GameState gameState;
    @Getter
    private Difficulty currentDifficulty = Difficulty.EASY;

    public void addNewGameListener(NewGameListener listener) {
        newGameListeners.add(listener);
    }

    public void addGridUpdateListener(GridUpdateListener listener) {
        gridUpdateListeners.add(listener);
    }

    public void addGameStateListener(GameStateListener listener) {
        gameStateListeners.add(listener);
    }

    public void addFlagCountListener(FlagCountListener listener) {
        flagCountListeners.add(listener);
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
            notifyGridUpdated(getAllCellsInfo());
            setGameState(GameState.LOST);
            return;
        }
        Set<int[]> updatedCells = new HashSet<>();
        gridService.revealCellRecursive(grid, x, y, updatedCells);
        notifyGridUpdated(getUpdatedCellsInfo(updatedCells));
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
        notifyGridUpdated(Set.of(new CellInfo(x, y, cell)));
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
            Set<int[]> updatedCells = new HashSet<>();
            boolean mineRevealed = gridService.revealNeighbors(grid, x, y, updatedCells);

            if (mineRevealed) {
                gridService.revealAllMines(grid);
                setGameState(GameState.LOST);
                notifyGridUpdated(getAllCellsInfo());
            } else {
                if (!updatedCells.isEmpty()) {
                    notifyGridUpdated(getUpdatedCellsInfo(updatedCells));
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
            gameStateListeners.forEach(listener -> listener.onGameStateChange(newGameState));
        }
    }

    private int getMinesLeft() {
        return grid.getCountOfMins() - grid.getFlagsPlaced();
    }

    private void notifyNewGame() {
        GameStartInfo info = new GameStartInfo(grid.getRows(), grid.getCols(), grid.getCountOfMins());
        newGameListeners.forEach(listener -> listener.onNewGame(info));
        notifyFlagCountChanged();
    }

    private void notifyGridUpdated(Set<CellInfo> updatedCells) {
        if (updatedCells.isEmpty()) {
            return;
        }
        gridUpdateListeners.forEach(listener -> listener.onGridUpdate(updatedCells));
    }

    private void notifyFlagCountChanged() {
        flagCountListeners.forEach(listener -> listener.onFlagCountChange(getMinesLeft()));
    }

    private Set<CellInfo> getUpdatedCellsInfo(Set<int[]> updatedCellCoords) {
        return updatedCellCoords.stream()
                .map(coords -> new CellInfo(coords[1], coords[0], grid.getArea()[coords[0]][coords[1]]))
                .collect(Collectors.toSet());
    }

    private Set<CellInfo> getAllCellsInfo() {
        Set<CellInfo> allCells = new HashSet<>();
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                allCells.add(new CellInfo(col, row, grid.getArea()[row][col]));
            }
        }
        return allCells;
    }
}