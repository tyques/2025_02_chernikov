package ru.cft.javaLessons.miner.app.model;

public class GameSession {
    private final Difficulty difficulty;
    private final Grid grid;
    private GameState gameState;
    private int flagsPlaced;
    private long startTime;

    public GameSession(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.grid = new Grid(difficulty.getRows(), difficulty.getCols(), difficulty.getCountOfMins());
        this.gameState = GameState.BEFORE_START;
        this.flagsPlaced = 0;
    }

    public void incrementFlagsPlaces(){
        flagsPlaced++;
    }

    public void decrementFlagsPlaced(){
        flagsPlaced--;
    }

    public GameState getGameState() {
        return gameState;
    }


/*
    private void reset() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col] = new Cell();
            }
        }
        flagsPlaced = 0;
        gameState = GameState.BEFORE_START;
        startTime = 0;
    }

     */
}
