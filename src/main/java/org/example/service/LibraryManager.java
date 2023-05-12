package org.example.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.example.file.GsonJSONFileOperationsImpl;
import org.example.helper.Helpers;
import org.example.model.LibraryItem;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class LibraryManager {
    private List<LibraryItem> libraryItems;
    private List<LibraryItem> searchResults;
    private final GsonJSONFileOperationsImpl fileOperations;
    private final Gson gson;


    public LibraryManager() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LibraryItem.class, new LibraryItemTypeAdapter())
                .create();
        fileOperations = new GsonJSONFileOperationsImpl();
        libraryItems = new ArrayList<>();
    }

    public JsonArray loadDataFromJsonFile(String fileName) throws IOException {
        System.out.println("Data loaded from JSON file successfully.");
        return fileOperations.readJSONFile(fileName);
    }

    public void writeDataToJsonFile(String fileName) throws IOException {
        fileOperations.writeJSONFile(fileName,convertLibraryItemsToJsonArray());
        System.out.println("Data loaded from JSON file successfully.");
    }

    public void addItem(LibraryItem item) {
        if (!libraryItems.contains(item)) {
            libraryItems.add(item);
            System.out.println("Item added successfully.");
        } else {
            System.out.println("Item already exists.");
        }
    }

    public void updateItem(LibraryItem updatedItem) {
        libraryItems = libraryItems.stream()
                .map(item -> {
                    if (item.equals(updatedItem)) {
                        return updatedItem;
                    } else {
                        return item;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<LibraryItem> searchByTitle(String title) {
        List<LibraryItem> results = libraryItems.stream()
                .filter(item -> item.getTitle().toLowerCase().contains(title.toLowerCase()))
                .sorted(Comparator.comparingInt((LibraryItem item) -> calculateTitleMatchLevel(item.getTitle(), title))
                        .reversed() // Sort by match level in descending order
                        .thenComparing(LibraryItem::getTitle)) // Sort alphabetically
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No items found with the given title.");
        }

        return results;
    }

    private static int calculateTitleMatchLevel(String itemTitle, String searchTitle) {
        if (itemTitle.equalsIgnoreCase(searchTitle)) {
            return 3; // Perfect match
        } else if (itemTitle.toLowerCase().contains(searchTitle.toLowerCase())) {
            return 2; // Close match
        } else if (Helpers.countCommonWords(itemTitle, searchTitle) >= 2) {
            return 1; // Weak match
        } else {
            return 0; // No match
        }
    }

    public List<LibraryItem> getLibraryItems() {
        return libraryItems;
    }

    public void displayItems(boolean isSearching) {
        List<LibraryItem> currentItems = isSearching ? searchResults : libraryItems;
            if (currentItems.size() == 0) {
                System.out.println("No items found in the library.");
            } else {
                System.out.println("Library Items:");
                for (LibraryItem item : currentItems) {
                    System.out.println("------------------------------");
                    item.displayItemDetails();
                    System.out.println("------------------------------");
                }
            }
    }

    public void setLibraryItemsFromJsonArray(JsonArray jsonArray) {
        libraryItems = new GsonBuilder()
                .registerTypeAdapter(LibraryItem.class, new LibraryItemTypeAdapter())
                .create()
                .fromJson(jsonArray, new TypeToken<List<LibraryItem>>() {}.getType());
    }

    public JsonArray convertLibraryItemsToJsonArray() {
        JsonArray jsonArray = new JsonArray();
        for (LibraryItem item : libraryItems) {
            JsonElement jsonElement = gson.toJsonTree(item);
            jsonArray.add(jsonElement);
        }
        return jsonArray;
    }

    public void setSearchResults(List<LibraryItem> items){
        this.searchResults = items;
    }
}