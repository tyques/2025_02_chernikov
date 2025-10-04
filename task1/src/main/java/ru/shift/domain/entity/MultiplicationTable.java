package ru.shift.domain.entity;

public class MultiplicationTable {
    private final int tableSize;

    private final int lengthOfFirstColumn;

    private final int cellSize;

    public MultiplicationTable(int tableSize) {
        this.tableSize = tableSize;
        this.lengthOfFirstColumn = calcLengthOfFirstColumn(tableSize);
        this.cellSize = calcCellSize(tableSize);

    }

    public int getTableSize() {
        return tableSize;
    }

    public int getLengthOfFirstColumn() {
        return lengthOfFirstColumn;
    }

    public int getCellSize() {
        return cellSize;
    }

    // Логарифм числа по основанию 10 равен количеству цифр минус один
    private int calcCellSize(int tableSize) {
        return (int) (Math.log10(tableSize * tableSize) + 1);
    }

    private int calcLengthOfFirstColumn(int tableSize) {
        return (int) (Math.log10(tableSize) + 1);
    }
}