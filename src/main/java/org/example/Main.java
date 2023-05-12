package org.example;

import com.google.gson.JsonArray;
import org.example.model.Book;
import org.example.model.LibraryItem;
import org.example.model.Magazine;
import org.example.model.Map;
import org.example.mongodb.MongoDBImporter;
import org.example.service.LibraryManager;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String connectionString  = "mongodb://localhost:27017";
    static String databaseName = "test";
    static String collectionName = "inventory";
    static String jsonFilePath = "src/main/java/org/example/data.json";
    static boolean isJsonSelected = false;
    static LibraryManager libraryManager = createLibraryManager();
    public static void main(String[] args) {
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
                runMenu(libraryManager, scanner);
            } catch (IOException e) {
                System.out.println("Failed to load data from the JSON file: " + e.getMessage());
            }
        } else if (choice == 2) {
            try {
                System.out.println("Connecting to MongoDB, please wait...");
                isJsonSelected = false;
                MongoDBImporter.importOrPostJSON("read",connectionString,databaseName,collectionName,libraryManager.convertLibraryItemsToJsonArray());
                System.out.println(MongoDBImporter.getJsonArray());
                libraryManager.setLibraryItemsFromJsonArray(MongoDBImporter.getJsonArray());
                runMenu(libraryManager, scanner);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Please Enter a valid input! Exiting...");
        }
    }

    private static LibraryManager createLibraryManager() {
        return new LibraryManager(){};
    }

    private static void runMenu(LibraryManager libraryManager, Scanner scanner) throws IOException {
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
                    libraryManager.displayItems(false);
                    break;
                case 2:
                    libraryManager.addItem(getLibraryItemFromUserInput());
                    break;
                case 3:
                    libraryManager.updateItem(userItemInput(false));
                    break;
                case 4:
                    libraryManager.updateItem(userItemInput(true));
                    break;
                case 5:
                    searchInput();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (menuChoice != 0);
    }

    private static void saveChanges(LibraryManager libraryManager) throws IOException {
        if(isJsonSelected) {
            libraryManager.writeDataToJsonFile(jsonFilePath);
        }
        else{
            MongoDBImporter.importOrPostJSON("import",connectionString,databaseName,collectionName,libraryManager.convertLibraryItemsToJsonArray());
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

    public static LibraryItem userItemInput(boolean isDelete) {
        Scanner scanner = new Scanner(System.in);

        // Ask user to enter author and title
        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        // Search for the library item
        LibraryItem foundItem = null;
        for (LibraryItem item : libraryManager.getLibraryItems()) {
            if (item.getAuthor().equals(author) && item.getTitle().equals(title)) {
                foundItem = item;
                break;
            }
        }

        // If item not found, print message and return null (for delete) or foundItem (for update)
        if (foundItem == null) {
            System.out.println("No item found with the given author and title.");
            return isDelete ? null : foundItem;
        }

        if (isDelete) {
            libraryManager.getLibraryItems().remove(foundItem);
            System.out.println("Item deleted successfully.");
            return null;
        }

        // Get the type of the item
        String itemType = foundItem.getLibraryItemType();

        // Based on the item type, ask the user to enter the data to update
        switch (itemType) {
            case "book" -> {
                System.out.print("Enter new ISBN: ");
                int isbn = scanner.nextInt();
                ((Book) foundItem).setIsbn(isbn);
                System.out.print("Enter new Publication Year : ");
                int publicationYear = scanner.nextInt();
                ((Book) foundItem).setPublicationYear(publicationYear);
            }
            case "magazine" -> {
                System.out.print("Enter new issue number: ");
                int issueNumber = scanner.nextInt();
                ((Magazine) foundItem).setIssueNumber(issueNumber);
            }
            case "map" -> {
                System.out.print("Enter new location: ");
                String location = scanner.nextLine();
                ((Map) foundItem).setLocation(location);
            }
            default -> System.out.println("Unsupported item type.");
        }

        System.out.println("Item updated successfully.");
        return foundItem;
    }

    static private void searchInput(){
        System.out.print("Enter Title: ");
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        List<LibraryItem> results =  libraryManager.searchByTitle(title);
        if(!results.isEmpty()){
            libraryManager.setSearchResults(results);
            libraryManager.displayItems(true);
        }
    }
}