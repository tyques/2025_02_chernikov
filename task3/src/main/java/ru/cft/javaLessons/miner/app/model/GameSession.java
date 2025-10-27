package ru.cft.javaLessons.miner.app.model;

public class GameSession {
    private final Difficulty difficulty;
    private final Grid grid;
    private GameState gameState;
    private int flagsPlaced;
    private long startTime;

    public GameSession(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.grid = new Grid(difficulty);
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getFlagsPlaced() {
        return flagsPlaced;
    }

    public long getStartTime() {
        return startTime;
    }
}
