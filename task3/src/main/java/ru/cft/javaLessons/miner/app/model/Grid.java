package ru.cft.javaLessons.miner.app.model;

import lombok.Data;

@Data
public class Grid {
    private final Difficulty difficulty;
    private final Cell[][] area;
    private int flagsPlaced;
    private int revealedCells;

    public Grid(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.area = new Cell[difficulty.getRows()][difficulty.getCols()];
        this.flagsPlaced = 0;
        this.revealedCells = 0;
        initArea();
    }

    public int getRows() {
        return difficulty.getRows();
    }

    public int getCols() {
        return difficulty.getCols();
    }

    public int getCountOfMins() {
        return difficulty.getCountOfMins();
    }

    private void initArea() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                area[row][col] = new Cell();
            }
        }
    }

    public void incrementFlagsPlaced() {
        flagsPlaced++;
    }

    public void decrementFlagsPlaced() {
        flagsPlaced--;
    }

    public void incrementRevealedCells() {
        revealedCells++;
    }
}