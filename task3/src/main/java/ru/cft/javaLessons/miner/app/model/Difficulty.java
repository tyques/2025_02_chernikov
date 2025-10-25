package ru.cft.javaLessons.miner.app.model;

public enum Difficulty {
    BEGINNER(9,9,  10),
    ENTHUSIAST(16, 16, 40),
    PROFESSIONAL(16, 30,  99);

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