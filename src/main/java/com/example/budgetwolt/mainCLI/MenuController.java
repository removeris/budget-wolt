package com.example.budgetwolt.mainCLI;

import com.example.budgetwolt.models.BasicUser;
import com.example.budgetwolt.models.Driver;
import com.example.budgetwolt.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuController {

    static final int userRequiredDataLength = 5;

    public static User selectUserByUsername(Wolt wolt, Scanner scanner) {
        User user = null;

        System.out.println("Enter User's username:");
        String username = scanner.nextLine();

        for (User usr : wolt.getAllUsers()) {
            if (usr.getUsername().equals(username)) {
                user = usr;
            }
        }
        return user;
    }

    public static void generateUserMenu(Scanner scanner, Wolt wolt) {

        int userSelection = 0;

        while (userSelection != 6) {

            System.out.print("""
                    BudgetWolt CLI | User Menu
                    1 - Create User
                    2 - View User
                    3 - Update User
                    4 - Delete User
                    5 - View All Users
                    6 - Exit User Menu
                    """);

            userSelection = scanner.nextInt();
            scanner.nextLine();

            switch (userSelection) {
                case 1:
                    System.out.println("Enter User data in the following format:\nusername;password;name;surname;phoneNumber;address;driverLicense;dateOfBirth(dd/MM/yyyy)");
                    String[] userData = scanner.nextLine().split(";");

                    User user = new User(userData[0], userData[1], userData[2], userData[3], userData[4]);
                    wolt.getAllUsers().add(user);
                    Utils.writeUserToFile(user);
                    break;
                case 2:
                    User foundUser = selectUserByUsername(wolt, scanner);
                    if (foundUser != null) {
                        System.out.println(foundUser.toString());
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 3:
                    User userToUpdate = selectUserByUsername(wolt, scanner);
                    if (userToUpdate == null) {
                        System.out.println("User not found.");
                        break;
                    }

                    System.out.println("""
                            Change User Property:
                            username|password|name|surname|phoneNumber=newValue
                            """);
                    String[] selectedUpdate = scanner.nextLine().split("=");

                    switch (selectedUpdate[0]) {
                        case "username":
                            userToUpdate.setUsername(selectedUpdate[1]);
                            break;
                        case "password":
                            userToUpdate.setPassword(selectedUpdate[1]);
                            break;
                        case "name":
                            userToUpdate.setName(selectedUpdate[1]);
                            break;
                        case "surname":
                            userToUpdate.setSurname(selectedUpdate[1]);
                            break;
                        case "phoneNumber":
                            userToUpdate.setPhoneNumber(selectedUpdate[1]);
                            break;
                        default:
                            System.out.println("Invalid input.");
                    }
                    break;
                case 4:
                    User userToDelete = selectUserByUsername(wolt, scanner);
                    if (userToDelete != null) {
                        wolt.getAllUsers().remove(userToDelete);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 5:
                    System.out.println("System Users:");
                    System.out.println("username\tpassword\tname\tsurname\tphoneNum");
                    for (User usr : wolt.getAllUsers()) {
                        System.out.println(usr.toString());
                    }
                    System.out.println();
                    break;
                case 6:
                    System.out.println("Exiting User Menu...");
                    break;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        }
    }
}
