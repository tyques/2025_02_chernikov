package ru.shift.domain.entity;

public class MultiplicationTable {
    private final int tableSize;
    private final int lengthOfFirstColumn;
    private final int cellSize;

    public MultiplicationTable(int tableSize) {
        this.tableSize = tableSize;
        // Логарифм числа по основанию 10 равен количеству цифр минус один
        this.lengthOfFirstColumn = (int) (Math.log10(tableSize) + 1);
        this.cellSize = (int) (Math.log10(tableSize * tableSize) + 1);

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
}