package ru.cft.javaLessons.miner.app.timer;

import lombok.Getter;
import ru.cft.javaLessons.miner.app.model.GameState;
import ru.cft.javaLessons.miner.app.model.listeners.GameStateListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameTimer implements GameStateListener {

    private final Timer timer;
    @Getter
    private int seconds;
    private final List<TimerListener> listeners = new ArrayList<>();

    public GameTimer() {
        this.seconds = 0;
        this.timer = new Timer(1000, e -> {
            seconds++;
            listeners.forEach(l -> l.onTimeChange(seconds));
        });
    }

    public void addTimerListener(TimerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onGameStateChange(GameState newState) {
        switch (newState) {
            case BEFORE_START -> {
                timer.stop();
                seconds = 0;
                listeners.forEach(l -> l.onTimeChange(seconds));
            }
            case IN_PROGRESS -> timer.start();
            case WON, LOST -> timer.stop();
        }
    }
}