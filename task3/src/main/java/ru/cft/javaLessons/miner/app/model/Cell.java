package ru.cft.javaLessons.miner.app.model;

import lombok.Data;

@Data
public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private byte adjacentMines;


    public Cell() {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }

    public void setRevealed() {
        isRevealed = true;
    }
}