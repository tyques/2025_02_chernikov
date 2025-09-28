package ru.shift.domain.services;

import ru.shift.domain.entity.MultiplicationTable;

public class MultiplicationTableService {
    public void printMultiplicationTable(MultiplicationTable multiplicationTable) {
        StringBuilder stringBuilder = new StringBuilder();

        writeFirstRow(multiplicationTable, stringBuilder);
        writeDividingRow(multiplicationTable, stringBuilder);
        for (int row = 1; row <= multiplicationTable.getTableSize(); row++) {
            stringBuilder.append("\n");
            writeFirstElementInRow(row, multiplicationTable.getLengthOfFirstColumn(), stringBuilder);

            for (int column = 1; column <= multiplicationTable.getTableSize(); ++column) {
                stringBuilder.append("|");
                stringBuilder.append(" ".repeat(multiplicationTable.getCellSize() - calcDigitsInCell(row, column)));
                stringBuilder.append(row * column);
            }
            writeDividingRow(multiplicationTable, stringBuilder);
        }
        System.out.println(stringBuilder);
    }

    private void writeFirstRow(MultiplicationTable multiplicationTable, StringBuilder stringBuilder) {
        stringBuilder.append(" ".repeat(multiplicationTable.getLengthOfFirstColumn()));
        for (int i = 1; i <= multiplicationTable.getTableSize(); i++) {
            stringBuilder.append("|");
            stringBuilder.append(" ".repeat(multiplicationTable.getCellSize() - calcDigitsInCell(1, i)));
            stringBuilder.append(i);
        }
    }

    private void writeDividingRow(MultiplicationTable multiplicationTable, StringBuilder stringBuilder) {
        stringBuilder.append('\n');
        stringBuilder.append("-".repeat(multiplicationTable.getLengthOfFirstColumn()));

        for (int j = 0; j < multiplicationTable.getTableSize(); j++) {
            stringBuilder.append('+');
            stringBuilder.append("-".repeat(multiplicationTable.getCellSize()));
        }
    }

    private void writeFirstElementInRow(int row, int cellSize, StringBuilder stringBuilder) {
        int countOfSpaces = cellSize - calcDigitsInCell(row, 1);
        stringBuilder.append(" ".repeat(countOfSpaces));
        stringBuilder.append(row);
    }

    private int calcDigitsInCell(int row, int column) {
        return (int) Math.log10(row * column) + 1;
    }
}
