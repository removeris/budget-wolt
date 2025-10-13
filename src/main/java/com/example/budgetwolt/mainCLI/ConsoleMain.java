package com.example.budgetwolt.mainCLI;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleMain {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        String userSelection = "";

        Wolt wolt = Utils.readWoltFromFile();
        if (wolt == null) {
            wolt = new Wolt();
        }

        while (!userSelection.equals("q")) {
            System.out.print("""
                    Welcome to BudgetWolt CLI
                    Choose:
                    u - Work with users
                    o - Work with orders
                    w - Write to file
                    q - quit
                    """);

            userSelection = scanner.nextLine();


            switch (userSelection) {
                case "u":
                    MenuController.generateUserMenu(scanner, wolt);
                    break;
                case "o":
                    break;
                case "w":
                    System.out.println("Updating Wolt Database..");
                    Utils.writeWoltToFile(wolt);
                    break;
                case "q":
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid selection");

            }
        }

        scanner.close();
    }
}
