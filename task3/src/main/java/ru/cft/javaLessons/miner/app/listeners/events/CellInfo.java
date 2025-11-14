package ru.cft.javaLessons.miner.app.listeners.events;

import ru.cft.javaLessons.miner.app.model.Cell;

public record CellInfo(
        int x,
        int y,
        boolean isMine,
        boolean isRevealed,
        boolean isFlagged,
        byte adjacentMines
) {
    public CellInfo(int x, int y, Cell cell) {
        this(x, y, cell.isMine(), cell.isRevealed(), cell.isFlagged(), cell.getAdjacentMines());
    }
}