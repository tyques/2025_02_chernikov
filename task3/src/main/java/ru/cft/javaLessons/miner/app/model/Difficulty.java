package ru.cft.javaLessons.miner.app.model;

public enum Difficulty {
    EASY(9,9,  3),
    MEDIUM(16, 16, 40),
    HARD(16, 30,  99);

    private final int rows;
    private final int cols;
    private final int countOfMins;


    Difficulty(int rows, int cols, int countOfMins) {
        this.rows = rows;
        this.cols = cols;
        this.countOfMins = countOfMins;
    }

    public int getCountOfMins() {
        return countOfMins;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}