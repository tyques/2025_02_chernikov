package ru.cft.javaLessons.miner.app.model.listeners;

import ru.cft.javaLessons.miner.app.model.listeners.events.CellInfo;

import java.util.Collection;

@FunctionalInterface
public interface GridUpdateListener {
    void onGridUpdate(Collection<CellInfo> updatedCells);
}