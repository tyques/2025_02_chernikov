package ru.cft.javaLessons.miner.app.service;

import ru.cft.javaLessons.miner.app.model.*;

public class GameSessionService {
    private GameSession gameSession;
    private GameState gameState;
    private Grid grid;
    private int rows;
    private int cols;
    private Cell[][] area;
    private int flagsPlaced;
    private long startTime;

    public GameSession createNewSession(Difficulty difficulty){
        GameSession gameSession = new GameSession(difficulty);
        grid = gameSession.getGrid();
        rows = grid.getRows();
        cols = grid.getCols();
        area = grid.getArea();
        return gameSession;
    }


    public void reset(){
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                area[row][col] = new Cell();
            }
        }
        flagsPlaced = 0;
        gameState = GameState.BEFORE_START;
        startTime = 0L;
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
