package ru.cft.javaLessons.miner.app.model;

public class Grid {
    private final int rows;
    private final int cols;
    private final int countOfMins;
    private final Cell[][] area;


    public Grid(Difficulty difficulty) {
        this.rows = difficulty.getRows();
        this.cols = difficulty.getCols();
        this.countOfMins = difficulty.getCountOfMins();
        this.area = new Cell[rows][cols];
        initArea();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell[][] getArea() {
        return area;
    }

    public int getCountOfMins() {
        return countOfMins;
    }

    private void initArea() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                area[row][col] = new Cell();
            }
        }
    }
}
