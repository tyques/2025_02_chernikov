package ru.cft.javaLessons.miner.app.listeners;

import ru.cft.javaLessons.miner.app.listeners.events.CellInfo;

import java.util.Collection;

@FunctionalInterface
public interface GridUpdateListener {
    void onGridUpdate(Collection<CellInfo> updatedCells);
}