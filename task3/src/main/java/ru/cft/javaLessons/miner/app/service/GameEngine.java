package ru.cft.javaLessons.miner.app.service;

import ru.cft.javaLessons.miner.app.model.*;
import ru.cft.javaLessons.miner.app.repository.AchievementRepository;

public class GameEngine {
    private final GridService gridService;
    private final AchievementRepository achievementRepository;
    private GameSession session;

    public GameEngine(GridService gridService, AchievementRepository achievementRepository) {
        this.gridService = gridService;
        this.achievementRepository = achievementRepository;
    }

    public void startNewGame(Difficulty difficulty) {
        this.session = new GameSession(difficulty);
    }

    public void revealCell(int x, int y) {
        if (session.getGameState() == GameState.BEFORE_START) {
            session.setGameState(GameState.IN_PROGRESS);
            session.startTimer();
            gridService.placeMines(session.getGrid(), x, y);
        }

        Cell cell = session.getGrid().getArea()[y][x];
        if (cell.isFlagged() || cell.isRevealed()) {
            return;
        }

        if (cell.isMine()) {
            session.setGameState(GameState.LOST);
            return;
        }

        gridService.revealCell(session.getGrid(), x, y);
        checkWinCondition();
    }

    public void toggleFlag(int x, int y) {
        Cell cell = session.getGrid().getArea()[y][x];
        if (cell.isRevealed()) {
            return;
        }
        if (cell.isFlagged()) {
            cell.setFlagged(false);
            session.decrementFlagsPlaced();
        } else {
            cell.setFlagged(true);
            session.incrementFlagsPlaces();
        }
    }

    public void revealAdjacentCells(int x, int y) {
        Cell cell = session.getGrid().getArea()[y][x];
        if (!cell.isRevealed() || cell.getAdjacentMines() == 0) {
            return;
        }

        int flagsAround = gridService.countFlagsAround(session.getGrid(), y, x);
        if (flagsAround == cell.getAdjacentMines()) {
            boolean mineRevealed = gridService.revealNeighbors(session.getGrid(), x, y);
            if (mineRevealed) {
                session.setGameState(GameState.LOST);
            } else {
                checkWinCondition();
            }
        }
    }


    private void checkWinCondition() {
        int revealedCount = 0;
        int totalCells = session.getDifficulty().getRows() * session.getDifficulty().getCols();
        int mineCount = session.getDifficulty().getCountOfMins();

        for (int r = 0; r < session.getDifficulty().getRows(); r++) {
            for (int c = 0; c < session.getDifficulty().getCols(); c++) {
                if (session.getGrid().getArea()[r][c].isRevealed()) {
                    revealedCount++;
                }
            }
        }

        if (revealedCount == totalCells - mineCount) {
            session.setGameState(GameState.WON);
            session.stopTimer();
        }
    }

    public void revealAllMines() {
        gridService.revealAllMines(session.getGrid());
    }

    public boolean isNewAchievement() {
        if (session.getGameState() != GameState.WON) return false;
        Achievement oldAchievement = achievementRepository.load(session.getDifficulty());
        return session.getElapsedTime() < oldAchievement.time();
    }

    public void updateHighScore(String name) {
        Achievement newAchievement = new Achievement(name, session.getElapsedTime());
        achievementRepository.save(session.getDifficulty(), newAchievement);
    }

    public void incrementTime() {
        if (session.getGameState() == GameState.IN_PROGRESS) {
            session.incrementTime();
        }
    }

    public GameState getGameState() {
        return session.getGameState();
    }

    public Difficulty getCurrentDifficulty() {
        return session.getDifficulty();
    }

    public Cell[][] getGrid() {
        return session.getGrid().getArea();
    }

    public int getMinesLeft() {
        return session.getDifficulty().getCountOfMins() - session.getFlagsPlaced();
    }

    public int getElapsedTime() {
        return session.getElapsedTime();
    }

    public Achievement getAchievement(Difficulty difficulty) {
        return achievementRepository.load(difficulty);
    }

}