package ru.cft.javaLessons.miner.view;

import javax.swing.*;
import java.awt.*;

public class HighScoresWindow extends JDialog {
    public static final String NO_RECORDS_YET = "No records yet";
    public static final int DEFAULT_TIME = 999;

    private final JLabel noviceAchievementLabel;
    private final JLabel mediumAchievementLabel;
    private final JLabel expertAchievementLabel;

    public HighScoresWindow(JFrame owner) {
        super(owner, "High Scores", true);

        GridBagLayout layout = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        int gridY = 0;

        contentPane.add(createLabel("Novice:", layout, gridY++, 0));
        contentPane.add(noviceAchievementLabel = createLabel(NO_RECORDS_YET, layout, gridY++, 0));

        contentPane.add(createLabel("Medium:", layout, gridY++, 10));
        contentPane.add(mediumAchievementLabel = createLabel(NO_RECORDS_YET, layout, gridY++, 0));

        contentPane.add(createLabel("Expert:", layout, gridY++, 10));
        contentPane.add(expertAchievementLabel = createLabel(NO_RECORDS_YET, layout, gridY++, 0));

        contentPane.add(createCloseButton(layout));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(200, 200));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public void setNoviceAchievement(String winnerName, int timeValue) {
        noviceAchievementLabel.setText(createAchievementText(winnerName, timeValue));
    }

    public void setMediumAchievement(String winnerName, int timeValue) {
        mediumAchievementLabel.setText(createAchievementText(winnerName, timeValue));
    }

    public void setExpertAchievement(String winnerName, int timeValue) {
        expertAchievementLabel.setText(createAchievementText(winnerName, timeValue));
    }

    public String createAchievementText(String winnerName, int timeValue) {
        if (timeValue >= DEFAULT_TIME) {
            return NO_RECORDS_YET;
        }
        return winnerName + " - " + timeValue + " sec";
    }

    private JLabel createLabel(String labelText, GridBagLayout layout, int gridY, int margin) {
        JLabel label = new JLabel(labelText);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(margin, 20, 0, 20);
        layout.setConstraints(label, gbc);

        return label;
    }

    private JButton createCloseButton(GridBagLayout layout) {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        okButton.setPreferredSize(new Dimension(60, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        layout.setConstraints(okButton, gbc);

        return okButton;
    }
}