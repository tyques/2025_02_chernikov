package ru.cft.javaLessons.miner.app.model;

public class GameSession {
    private final Difficulty difficulty;
    private final Grid grid;
    private GameState gameState;
    private int flagsPlaced;
    private int elapsedTime; // in seconds
    private boolean timerRunning;
    private int revealedCells;


    public GameSession(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.grid = new Grid(difficulty);
        this.gameState = GameState.BEFORE_START;
        this.flagsPlaced = 0;
        this.elapsedTime = 0;
        this.timerRunning = false;
        this.revealedCells = 0;
    }

    public void incrementFlagsPlaces() {
        flagsPlaced++;
    }

    public void decrementFlagsPlaced() {
        flagsPlaced--;
    }

    public void incrementRevealedCells() {
        revealedCells++;
    }

    public int getRevealedCells() {
        return revealedCells;
    }

    public void startTimer() {
        this.timerRunning = true;
    }

    public void stopTimer() {
        this.timerRunning = false;
    }

    public void incrementTime() {
        if (timerRunning) {
            this.elapsedTime++;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
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

    public int getElapsedTime() {
        return elapsedTime;
    }
}