package org.example;

import com.google.gson.JsonArray;
import org.example.model.Book;
import org.example.model.LibraryItem;
import org.example.model.Magazine;
import org.example.model.Map;
import org.example.service.LibraryManager;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static boolean isJsonSelected = false;
    public static void main(String[] args) {
        LibraryManager libraryManager = createLibraryManager();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Library Management System!");

        System.out.println("How do you want to load data?");
        System.out.println("1. JSON File");
        System.out.println("2. MongoDB");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            try {
                isJsonSelected = true;
                JsonArray libraryItems = libraryManager.loadDataFromJsonFile("src/main/java/org/example/data.json");
                libraryManager.setLibraryItemsFromJsonArray(libraryItems);
                runMenu(libraryManager, libraryItems, scanner);
            } catch (IOException e) {
                System.out.println("Failed to load data from the JSON file: " + e.getMessage());
            }
        } else if (choice == 2) {
            System.out.println("Connecting to MongoDB, please wait...");
        } else {
            System.out.println("Please Enter a valid input! Exiting...");
        }

        // TODO: Implement the rest of the menu and user interactions
    }

    private static LibraryManager createLibraryManager() {
        return new LibraryManager(){};
    }

    private static void runMenu(LibraryManager libraryManager, JsonArray libraryItems, Scanner scanner) throws IOException {
        System.out.println("\nMenu Options:");
        System.out.println("1. Display Items");
        System.out.println("2. Add Item");
        System.out.println("3. Update Item");
        System.out.println("4. Delete Item");
        System.out.println("5. Search Item");
        System.out.println("0. Exit");

        int menuChoice;
        do {
            System.out.println("==================");
            System.out.print("Enter your choice: ");
            menuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            switch (menuChoice) {
                case 0:
                    System.out.println("Save Changes? (y/n)");
                    if (scanner.next().equalsIgnoreCase("y")) {
                        saveChanges(libraryManager);
                    } else {
                        System.out.println("Exiting without saving...");
                        break;
                    }
                case 1:
                    libraryManager.displayItems();
                    break;
                case 2:
                    libraryManager.addItem(getLibraryItemFromUserInput());
                    break;
                case 3:
                    // TODO: Implement logic to update an item
                    break;
                case 4:
                    // TODO: Implement logic to delete an item
                    break;
                case 5:
                    // TODO: Implement logic to search for an item
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (menuChoice != 0);
    }

    private static void saveChanges(LibraryManager libraryManager) throws IOException {
        if(isJsonSelected) {
            libraryManager.writeDataToJsonFile("src/main/java/org/example/data.json");
        }
        else{

        }
    }

    private static LibraryItem getLibraryItemFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add Item Menu:");
        System.out.println("===============");
        System.out.print("Library Item title: ");
        String title = scanner.nextLine();
        System.out.print("Library Item Author: ");
        String author = scanner.nextLine();
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("3. Map");
        System.out.print("Please select an item type to add: ");

        int type = scanner.nextInt();

        switch (type) {
            case 1 -> {
                System.out.println("You have Selected a Book!");
                System.out.print("Enter ISBN (Integer): ");
                int isbn = scanner.nextInt();
                System.out.print("Enter Publication Year: ");
                int publicationYear = scanner.nextInt();
                return new Book(title, author, publicationYear, isbn);
            }
            case 2 -> {
                System.out.println("You have Selected a Magazine");
                System.out.print("Enter IssueNumber: ");
                int issueNumber = scanner.nextInt();
                return new Magazine(title, author, issueNumber);
            }
            case 3 -> {
                System.out.println("You have Selected a Map");
                System.out.print("Enter location: ");
                String location = scanner.next();
                return new Map(title, author, location);
            }
            default -> System.out.println("Invalid item type.");
        }
        return null;
    }
}