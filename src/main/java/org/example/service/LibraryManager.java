package org.example.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.example.file.GsonJSONFileOperationsImpl;
import org.example.model.LibraryItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public void addItem(LibraryItem item){
        libraryItems.add(item);
    };


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
                .sorted(Comparator.comparingInt(item -> calculateTitleMatchLevel(item.getTitle(), title)))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No items found with the given title.");
        }

        return results;
    }

    private int calculateTitleMatchLevel(String itemTitle, String searchTitle) {
        int matchLevel = 0;

        // Check for exact match
        if (itemTitle.equalsIgnoreCase(searchTitle)) {
            return matchLevel;
        }

        // Check for partial match
        if (itemTitle.toLowerCase().startsWith(searchTitle.toLowerCase())) {
            matchLevel++;
        }

        return matchLevel;
    }

    public void deleteItem(LibraryItem item){

    };

    public List<LibraryItem> getLibraryItems() {
        return libraryItems;
    }

    public GsonJSONFileOperationsImpl getFileOperations() {
        return fileOperations;
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

    public void fromJson(String json) {
        // Convert the JSON string to libraryItems list
        libraryItems = gson.fromJson(json, new TypeToken<List<LibraryItem>>() {}.getType());
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

    public void setLibraryItems(List<LibraryItem> items) {
        libraryItems = items;
    }

    public void setSearchResults(List<LibraryItem> items){
        this.searchResults = items;
    }


    // Other methods and functionality
}