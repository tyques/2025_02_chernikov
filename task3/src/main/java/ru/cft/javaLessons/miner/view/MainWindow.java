package ru.cft.javaLessons.miner.view;

import ru.cft.javaLessons.miner.app.controller.GameController;
import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.model.listeners.*;
import ru.cft.javaLessons.miner.app.model.listeners.events.CellInfo;
import ru.cft.javaLessons.miner.app.model.listeners.events.GameStartInfo;
import ru.cft.javaLessons.miner.app.repository.AchievementRepository;
import ru.cft.javaLessons.miner.app.repository.FileAchievementRepository;
import ru.cft.javaLessons.miner.app.timer.TimerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.function.Consumer;

public class MainWindow extends JFrame implements NewGameListener, GridUpdateListener, GameStateListener,
        FlagCountListener, TimerListener, NewRecordListener, WinListener {
    private final Container contentPane;
    private final GridBagLayout mainLayout;
    private final GameController controller;
    private final AchievementRepository achievementRepository; // Only for high scores window

    private Difficulty currentDifficulty;
    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;

    public MainWindow(GameController controller) {
        super("Miner");
        this.controller = controller;
        this.achievementRepository = new FileAchievementRepository(); // For displaying scores

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        createMenu();
        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);
        contentPane.setBackground(new Color(144, 158, 184));
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameMenu = new JMenuItem("New Game");
        JMenuItem highScoresMenu = new JMenuItem("High Scores");
        JMenuItem settingsMenu = new JMenuItem("Settings");
        JMenuItem exitMenu = new JMenuItem("Exit");

        newGameMenu.addActionListener(e -> controller.startNewGame());
        highScoresMenu.addActionListener(e -> openHighScores());
        settingsMenu.addActionListener(e -> openSettings());
        exitMenu.addActionListener(e -> dispose());

        gameMenu.add(newGameMenu);
        gameMenu.addSeparator();
        gameMenu.add(highScoresMenu);
        gameMenu.add(settingsMenu);
        gameMenu.addSeparator();
        gameMenu.add(exitMenu);

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    @Override
    public void onNewGame(GameStartInfo info) {
        this.currentDifficulty = mapDifficultyFromSize(info.rows(), info.cols());
        createGameField(info.rows(), info.cols());
        for (int y = 0; y < info.rows(); y++) {
            for (int x = 0; x < info.cols(); x++) {
                cellButtons[y][x].setIcon(GameImage.CLOSED.getImageIcon());
            }
        }
    }

    @Override
    public void onGridUpdate(Collection<CellInfo> updatedCells) {
        for (CellInfo cell : updatedCells) {
            cellButtons[cell.y()][cell.x()].setIcon(mapCellToImage(cell).getImageIcon());
        }
    }

    @Override
    public void onGameStateChange(GameState newState) {
        if (newState == GameState.LOST) {
            handleLoss();
        }
    }

    @Override
    public void onFlagCountChange(int newFlagCount) {
        bombsCounterLabel.setText(String.valueOf(newFlagCount));
    }

    @Override
    public void onTimeChange(int seconds) {
        timerLabel.setText(String.valueOf(seconds));
    }

    @Override
    public void onNewRecord(Consumer<String> nameConsumer) {
        AchievementWindow achievementWindow = new AchievementWindow(this);
        achievementWindow.setNameListener(name -> {
            nameConsumer.accept(name);
            showWinDialog();
        });
        achievementWindow.setVisible(true);
    }

    @Override
    public void onWin() {
        showWinDialog();
    }

    private void handleLoss() {
        LoseWindow loseWindow = new LoseWindow(this);
        loseWindow.setNewGameListener(e -> controller.startNewGame());
        loseWindow.setExitListener(e -> dispose());
        loseWindow.setVisible(true);
    }

    private void showWinDialog() {
        WinWindow winWindow = new WinWindow(this);
        winWindow.setNewGameListener(e -> controller.startNewGame());
        winWindow.setExitListener(e -> dispose());
        winWindow.setVisible(true);
    }

    private void openSettings() {
        SettingsWindow settingsWindow = new SettingsWindow(this);
        settingsWindow.setGameTypeListener(controller);
        settingsWindow.setGameType(mapDifficultyToGameType(this.currentDifficulty));
        settingsWindow.setVisible(true);
    }

    private void openHighScores() {
        HighScoresWindow highScoresWindow = new HighScoresWindow(this);
        Achievement novice = achievementRepository.load(Difficulty.EASY);
        Achievement medium = achievementRepository.load(Difficulty.MEDIUM);
        Achievement expert = achievementRepository.load(Difficulty.HARD);
        highScoresWindow.setNoviceAchievement(novice.name(), novice.time());
        highScoresWindow.setMediumAchievement(medium.name(), medium.time());
        highScoresWindow.setExpertAchievement(expert.name(), expert.time());
        highScoresWindow.setVisible(true);
    }

    private GameImage mapCellToImage(CellInfo cell) {
        if (!cell.isRevealed()) {
            return cell.isFlagged() ? GameImage.MARKED : GameImage.CLOSED;
        }
        if (cell.isMine()) {
            return GameImage.BOMB;
        }
        return switch (cell.adjacentMines()) {
            case 0 -> GameImage.EMPTY;
            case 1 -> GameImage.NUM_1;
            case 2 -> GameImage.NUM_2;
            case 3 -> GameImage.NUM_3;
            case 4 -> GameImage.NUM_4;
            case 5 -> GameImage.NUM_5;
            case 6 -> GameImage.NUM_6;
            case 7 -> GameImage.NUM_7;
            case 8 -> GameImage.NUM_8;
            default -> GameImage.EMPTY;
        };
    }

    private GameType mapDifficultyToGameType(Difficulty difficulty) {
        if (difficulty == null) return GameType.NOVICE;
        return switch (difficulty) {
            case EASY -> GameType.NOVICE;
            case MEDIUM -> GameType.MEDIUM;
            case HARD -> GameType.EXPERT;
        };
    }

    private Difficulty mapDifficultyFromSize(int rows, int cols) {
        if (rows == 9 && cols == 9) return Difficulty.EASY;
        if (rows == 16 && cols == 16) return Difficulty.MEDIUM;
        if (rows == 16 && cols == 30) return Difficulty.HARD;
        return Difficulty.EASY;
    }

    public void createGameField(int rowsCount, int colsCount) {
        contentPane.removeAll();
        setPreferredSize(new Dimension(20 * colsCount + 70, 20 * rowsCount + 110));
        addButtonsPanel(createButtonsPanel(rowsCount, colsCount));
        addTimerImage();
        addTimerLabel(timerLabel = new JLabel("0"));
        addBombCounter(bombsCounterLabel = new JLabel("0"));
        addBombCounterImage();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButtonsPanel(int numberOfRows, int numberOfCols) {
        cellButtons = new JButton[numberOfRows][numberOfCols];
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(20 * numberOfCols, 20 * numberOfRows));
        buttonsPanel.setLayout(new GridLayout(numberOfRows, numberOfCols, 0, 0));
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                final int x = col;
                final int y = row;
                cellButtons[y][x] = new JButton();
                cellButtons[y][x].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        switch (e.getButton()) {
                            case MouseEvent.BUTTON1 -> controller.onMouseClick(x, y, ButtonType.LEFT_BUTTON);
                            case MouseEvent.BUTTON3 -> controller.onMouseClick(x, y, ButtonType.RIGHT_BUTTON);
                            case MouseEvent.BUTTON2 -> controller.onMouseClick(x, y, ButtonType.MIDDLE_BUTTON);
                        }
                    }
                });
                buttonsPanel.add(cellButtons[y][x]);
            }
        }
        return buttonsPanel;
    }

    private void addButtonsPanel(JPanel buttonsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 20, 5, 20);
        mainLayout.setConstraints(buttonsPanel, gbc);
        contentPane.add(buttonsPanel);
    }

    private void addTimerImage() {
        JLabel label = new JLabel(GameImage.TIMER.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private void addTimerLabel(JLabel timerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        mainLayout.setConstraints(timerLabel, gbc);
        contentPane.add(timerLabel);
    }

    private void addBombCounter(JLabel bombsCounterLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.7;
        mainLayout.setConstraints(bombsCounterLabel, gbc);
        contentPane.add(bombsCounterLabel);
    }

    private void addBombCounterImage() {
        JLabel label = new JLabel(GameImage.BOMB_ICON.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 5, 0, 20);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }
}