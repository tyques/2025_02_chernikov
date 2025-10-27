package ru.cft.javaLessons.miner.app.service;

import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.model.Grid;

public class GridService {
    private Difficulty difficulty;
    private Grid grid;

    private Grid createGrid(){
        return new Grid(difficulty);
    }


}
