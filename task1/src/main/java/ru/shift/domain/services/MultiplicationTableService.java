package ru.shift.domain.services;

import ru.shift.domain.entity.Constants;
import ru.shift.domain.entity.MultiplicationTable;

public class MultiplicationTableService {
    private final int tableSize;
    private final int lengthOfFirstColumn;
    private final int cellSize;

    public MultiplicationTableService(MultiplicationTable multiplicationTable) {
        this.tableSize = multiplicationTable.getTableSize();
        this.lengthOfFirstColumn = multiplicationTable.getLengthOfFirstColumn();
        this.cellSize = multiplicationTable.getCellSize();
    }

    public String generateTableAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        writeFirstRow(stringBuilder);
        writeDividingRow( stringBuilder);
        for (int row = 1; row <= tableSize; row++) {
            stringBuilder.append(Constants.NEW_LINE);
            writeFirstElementInRow(row, lengthOfFirstColumn, stringBuilder);

            for (int column = 1; column <= tableSize; ++column) {
                stringBuilder.append(Constants.STICK);
                stringBuilder.repeat(Constants.SPACE, cellSize - calcDigitsInCell(row, column));
                stringBuilder.append(row * column);
            }
            writeDividingRow(stringBuilder);
        }
        return stringBuilder.toString();
    }

    private void writeFirstRow(StringBuilder stringBuilder) {
        stringBuilder.repeat(Constants.SPACE, lengthOfFirstColumn);
        for (int i = 1; i <= tableSize; i++) {
            stringBuilder.append(Constants.STICK);
            stringBuilder.repeat(Constants.SPACE, cellSize - calcDigitsInCell(1, i));
            stringBuilder.append(i);
        }
    }

    private void writeDividingRow(StringBuilder stringBuilder) {
        stringBuilder.append(Constants.NEW_LINE);
        stringBuilder.repeat(Constants.MINUS, lengthOfFirstColumn);

        for (int j = 0; j < tableSize; j++) {
            stringBuilder.append(Constants.PLUS);
            stringBuilder.repeat(Constants.MINUS, cellSize);
        }
    }

    private void writeFirstElementInRow(int row, int cellSize, StringBuilder stringBuilder) {
        int countOfSpaces = cellSize - calcDigitsInCell(row, 1);
        stringBuilder.repeat(Constants.SPACE, countOfSpaces);
        stringBuilder.append(row);
    }

    private int calcDigitsInCell(int row, int column) {
        return (int) Math.log10(row * column) + 1;
    }
}
