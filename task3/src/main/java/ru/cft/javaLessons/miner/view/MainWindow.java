package ru.cft.javaLessons.miner.view;

import ru.cft.javaLessons.miner.app.controller.GameController;
import ru.cft.javaLessons.miner.app.model.Cell;
import ru.cft.javaLessons.miner.app.model.Difficulty;
import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.model.Grid;
import ru.cft.javaLessons.miner.app.repository.AchievementRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame implements GameListener {
    private final Container contentPane;
    private final GridBagLayout mainLayout;
    private final GameController controller;
    private final AchievementRepository achievementRepository;
    private final Timer timer;
    private int seconds;
    private Grid grid;

    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;

    public MainWindow(GameController controller, AchievementRepository achievementRepository) {
        super("Miner");
        this.controller = controller;
        this.achievementRepository = achievementRepository;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        createMenu();
        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);
        contentPane.setBackground(new Color(144, 158, 184));
        this.timer = new Timer(1000, e -> setTimerValue(++seconds));
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
    public void onNewGame(Grid grid) {
        this.grid = grid;
        this.seconds = 0;
        createGameField(grid.getRows(), grid.getCols());
        setTimerValue(0);
        timer.stop();
        onGridUpdate();
    }

    @Override
    public void onGridUpdate() {
        Cell[][] cells = grid.getArea();
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getCols(); x++) {
                cellButtons[y][x].setIcon(mapCellToImage(cells[y][x]).getImageIcon());
            }
        }
    }

    @Override
    public void onGameStateChange(GameState newState) {
        switch (newState) {
            case IN_PROGRESS -> timer.start();
            case WON -> handleWin();
            case LOST -> handleLoss();
        }
    }

    @Override
    public void onFlagCountChange(int newFlagCount) {
        bombsCounterLabel.setText(String.valueOf(newFlagCount));
    }

    private void setTimerValue(int value) {
        timerLabel.setText(String.valueOf(value));
    }

    private void handleWin() {
        timer.stop();
        Achievement oldAchievement = achievementRepository.load(grid.getDifficulty());
        if (seconds < oldAchievement.time()) {
            AchievementWindow achievementWindow = new AchievementWindow(this);
            achievementWindow.setNameListener(name -> {
                achievementRepository.save(grid.getDifficulty(), new Achievement(name, seconds));
                showWinDialog();
            });
            achievementWindow.setVisible(true);
        } else {
            showWinDialog();
        }
    }

    private void handleLoss() {
        timer.stop();
        onGridUpdate();
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
        settingsWindow.setGameType(mapDifficultyToGameType(grid.getDifficulty()));
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

    private GameImage mapCellToImage(Cell cell) {
        if (!cell.isRevealed()) {
            return cell.isFlagged() ? GameImage.MARKED : GameImage.CLOSED;
        }
        if (cell.isMine()) {
            return GameImage.BOMB;
        }
        return switch (cell.getAdjacentMines()) {
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
        return switch (difficulty) {
            case EASY -> GameType.NOVICE;
            case MEDIUM -> GameType.MEDIUM;
            case HARD -> GameType.EXPERT;
        };
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