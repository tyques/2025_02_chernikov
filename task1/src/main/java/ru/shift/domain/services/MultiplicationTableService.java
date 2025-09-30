package ru.shift.domain.services;

import ru.shift.domain.entity.Constants;
import ru.shift.domain.entity.MultiplicationTable;

public class MultiplicationTableService {
    public String generateTableAsString(MultiplicationTable multiplicationTable) {
        StringBuilder stringBuilder = new StringBuilder();

        writeFirstRow(multiplicationTable, stringBuilder);
        writeDividingRow(multiplicationTable, stringBuilder);
        for (int row = 1; row <= multiplicationTable.getTableSize(); row++) {
            stringBuilder.append(Constants.NEW_LINE);
            writeFirstElementInRow(row, multiplicationTable.getLengthOfFirstColumn(), stringBuilder);

            for (int column = 1; column <= multiplicationTable.getTableSize(); ++column) {
                stringBuilder.append(Constants.STICK);
                stringBuilder.append(Constants.SPACE.repeat(multiplicationTable.getCellSize() - calcDigitsInCell(row, column)));
                stringBuilder.append(row * column);
            }
            writeDividingRow(multiplicationTable, stringBuilder);
        }
        return stringBuilder.toString();
    }

    private void writeFirstRow(MultiplicationTable multiplicationTable, StringBuilder stringBuilder) {
        stringBuilder.append(Constants.SPACE.repeat(multiplicationTable.getLengthOfFirstColumn()));
        for (int i = 1; i <= multiplicationTable.getTableSize(); i++) {
            stringBuilder.append(Constants.STICK);
            stringBuilder.append(Constants.SPACE.repeat(multiplicationTable.getCellSize() - calcDigitsInCell(1, i)));
            stringBuilder.append(i);
        }
    }

    private void writeDividingRow(MultiplicationTable multiplicationTable, StringBuilder stringBuilder) {
        stringBuilder.append(Constants.NEW_LINE);
        stringBuilder.append(Constants.MINUS.repeat(multiplicationTable.getLengthOfFirstColumn()));

        for (int j = 0; j < multiplicationTable.getTableSize(); j++) {
            stringBuilder.append(Constants.PLUS);
            stringBuilder.append(Constants.MINUS.repeat(multiplicationTable.getCellSize()));
        }
    }

    private void writeFirstElementInRow(int row, int cellSize, StringBuilder stringBuilder) {
        int countOfSpaces = cellSize - calcDigitsInCell(row, 1);
        stringBuilder.append(Constants.SPACE.repeat(countOfSpaces));
        stringBuilder.append(row);
    }

    private int calcDigitsInCell(int row, int column) {
        return (int) Math.log10(row * column) + 1;
    }
}
