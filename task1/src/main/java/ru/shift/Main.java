package ru.shift;

import ru.shift.domain.entity.Constants;
import ru.shift.domain.entity.MultiplicationTable;
import ru.shift.domain.services.MultiplicationTableService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tableSize;

        while (true) {
            System.out.print("Enter the table size (an integer from 1 to 32): ");
            try {
                tableSize = scanner.nextInt();

                if (tableSize >= Constants.MIN_TABLE_SIZE && tableSize <= Constants.MAX_TABLE_SIZE) {
                    break;
                } else {
                    System.err.println("Error: The table size must be in the range from 1 to 32. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: The input must be an integer. Please try again.");
                scanner.next();
            }
        }

        MultiplicationTable multiplicationTable = new MultiplicationTable(tableSize);
        MultiplicationTableService multiplicationTableService = new MultiplicationTableService(multiplicationTable);

        String table = multiplicationTableService.generateTableAsString();
        System.out.println(table);
    }
}