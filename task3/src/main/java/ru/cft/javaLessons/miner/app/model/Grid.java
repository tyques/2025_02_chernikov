package ru.cft.javaLessons.miner.app.model;

public class Grid {
    private final int rows;
    private final int cols;
    private Cell[][] area;


    public Grid(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.area = new Cell[rows][cols];
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
}
