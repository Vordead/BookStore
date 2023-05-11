package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.file.GsonJSONFileOperationsImpl;
import org.example.model.LibraryItem;
import org.example.model.Magazine;
import org.example.service.LibraryManager;

import java.awt.print.Book;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        LibraryManager libraryManager = new LibraryManager() {
            @Override
            public void addItem(LibraryItem item) {
                // TODO: Implement the logic to add an item
            }

            @Override
            public void updateItem(LibraryItem item) {
                // TODO: Implement the logic to update an item
            }

            @Override
            public void deleteItem(LibraryItem item) {
                // TODO: Implement the logic to delete an item
            }

            @Override
            public void displayItems(JsonArray libraryItems) {
                if (libraryItems.size() == 0) {
                    System.out.println("No items found in the library.");
                    return;
                }
                System.out.println(libraryItems);
            }
        };

        GsonJSONFileOperationsImpl fileOperations = new GsonJSONFileOperationsImpl();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Library Management System!");

        System.out.print("Do you want to load data from the JSON file? (y/n): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            try {
                JsonArray libraryItems = libraryManager.loadDataFromJsonFile("/Users/mohamadhamade/IdeaProjects/BookStore/src/main/java/org/example/data.json");
                libraryManager.displayItems(libraryItems);
            } catch (IOException e) {
                System.out.println("Failed to load data from the JSON file: " + e.getMessage());
            }
        }

        // TODO: Implement the rest of the menu and user interactions
    }
}