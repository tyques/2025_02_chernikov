package ru.shift.domain.services;

import ru.shift.domain.entity.Constants;
import ru.shift.domain.entity.MultiplicationTable;

public class MultiplicationTableService {
    private final int tableSize;

    private final int lengthOfFirstColumn;

    private final int cellSize;

    private final String dividingRow;

    private final StringBuilder stringBuilder;

    public MultiplicationTableService(MultiplicationTable multiplicationTable) {
        this.tableSize = multiplicationTable.getTableSize();
        this.lengthOfFirstColumn = multiplicationTable.getLengthOfFirstColumn();
        this.cellSize = multiplicationTable.getCellSize();
        this.dividingRow = createDividingRow();
        this.stringBuilder = new StringBuilder(calculateInitialCapacity());
    }

    public String generateTableAsString() {
        writeFirstRow();
        stringBuilder.append(dividingRow);
        for (int row = 1; row <= tableSize; row++) {
            stringBuilder.append(Constants.NEW_LINE);
            writeFirstElementInRow(row, lengthOfFirstColumn);

            for (int column = 1; column <= tableSize; ++column) {
                stringBuilder.append(Constants.STICK);
                stringBuilder.repeat(Constants.SPACE, cellSize - calcDigitsInCell(row, column));
                stringBuilder.append(row * column);
            }
            stringBuilder.append(dividingRow);
        }
        return stringBuilder.toString();
    }

    private void writeFirstRow() {
        stringBuilder.repeat(Constants.SPACE, lengthOfFirstColumn);
        for (int i = 1; i <= tableSize; i++) {
            stringBuilder.append(Constants.STICK);
            stringBuilder.repeat(Constants.SPACE, cellSize - calcDigitsInCell(1, i));
            stringBuilder.append(i);
        }
    }

    private String createDividingRow() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.NEW_LINE);
        stringBuilder.repeat(Constants.MINUS, lengthOfFirstColumn);

        for (int j = 0; j < tableSize; j++) {
            stringBuilder.append(Constants.PLUS);
            stringBuilder.repeat(Constants.MINUS, cellSize);
        }
        return stringBuilder.toString();
    }

    private void writeFirstElementInRow(int row, int cellSize) {
        int countOfSpaces = cellSize - calcDigitsInCell(row, 1);
        stringBuilder.repeat(Constants.SPACE, countOfSpaces);
        stringBuilder.append(row);
    }

    private int calculateInitialCapacity() {
        int tableWidth = lengthOfFirstColumn + tableSize * (1 + cellSize);

        int dividingRowLength = 1 + tableWidth;

        int singleLoopBlockLength = 1 + tableWidth + dividingRowLength;

        int initialCapacity = tableWidth + dividingRowLength + tableSize * singleLoopBlockLength;
        return initialCapacity;
    }

    private int calcDigitsInCell(int row, int column) {
        return (int) Math.log10(row * column) + 1;
    }
}
