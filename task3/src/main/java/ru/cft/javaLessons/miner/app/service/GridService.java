package ru.cft.javaLessons.miner.app.service;

import ru.cft.javaLessons.miner.app.model.Cell;
import ru.cft.javaLessons.miner.app.model.Grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GridService {
    public void placeMines(Grid grid, int firstClickX, int firstClickY) {
        int rows = grid.getRows();
        int cols = grid.getCols();
        int minesToPlace = grid.getCountOfMins();

        List<int[]> possibleMineLocations = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row != firstClickY || col != firstClickX) {
                    possibleMineLocations.add(new int[]{row, col});
                }
            }
        }

        Collections.shuffle(possibleMineLocations);

        for (int i = 0; i < minesToPlace; i++) {
            int[] location = possibleMineLocations.get(i);
            grid.getArea()[location[0]][location[1]].setMine(true);
        }

        calculateAdjacentMines(grid);
    }

    private void calculateAdjacentMines(Grid grid) {
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                if (!grid.getArea()[row][col].isMine()) {
                    grid.getArea()[row][col].setAdjacentMines(countMinesAround(grid, row, col));
                }
            }
        }
    }

    private byte countMinesAround(Grid grid, int row, int col) {
        byte count = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r >= 0 && r < grid.getRows() && c >= 0 && c < grid.getCols() && grid.getArea()[r][c].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    public byte countFlagsAround(Grid grid, int row, int col) {
        byte count = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r >= 0 && r < grid.getRows() && c >= 0 && c < grid.getCols() && grid.getArea()[r][c].isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void revealCellRecursive(Grid grid, int x, int y, Set<int[]> updatedCells) {
        Cell cell = grid.getArea()[y][x];
        if (cell.isRevealed() || cell.isFlagged()) {
            return;
        }

        cell.setRevealed();
        grid.incrementRevealedCells();
        updatedCells.add(new int[]{y, x});

        if (cell.getAdjacentMines() == 0) {
            for (int r = y - 1; r <= y + 1; r++) {
                for (int c = x - 1; c <= x + 1; c++) {
                    if (r >= 0 && r < grid.getRows() && c >= 0 && c < grid.getCols() && !(r == y && c == x)) {
                        revealCellRecursive(grid, c, r, updatedCells);
                    }
                }
            }
        }
    }

    public boolean revealNeighbors(Grid grid, int x, int y, Set<int[]> updatedCells) {
        boolean mineRevealed = false;
        for (int r = y - 1; r <= y + 1; r++) {
            for (int c = x - 1; c <= x + 1; c++) {
                if (r >= 0 && r < grid.getRows() && c >= 0 && c < grid.getCols() && !(r == y && c == x)) {
                    Cell neighbor = grid.getArea()[r][c];
                    if (!neighbor.isFlagged() && !neighbor.isRevealed()) {
                        if (neighbor.isMine()) {
                            mineRevealed = true;
                        }
                        revealCellRecursive(grid, c, r, updatedCells);
                    }
                }
            }
        }
        return mineRevealed;
    }

    public void revealAllMines(Grid grid) {
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                Cell cell = grid.getArea()[row][col];
                if (cell.isMine()) {
                    cell.setRevealed();
                }
            }
        }
    }
}