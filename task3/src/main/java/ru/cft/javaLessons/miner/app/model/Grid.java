package ru.cft.javaLessons.miner.app.model;

public class Grid {
    private final int rows;
    private final int cols;
    private Cell[][] grid;


    public Grid(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
    }
}
