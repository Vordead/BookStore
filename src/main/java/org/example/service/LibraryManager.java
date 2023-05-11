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
import java.util.List;
import java.util.stream.Collectors;

public abstract class LibraryManager {
    private List<LibraryItem> libraryItems;
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

    public void searchItem(LibraryItem libraryItem) {
        libraryItems.stream()
                .filter(item -> item.getTitle().equals(libraryItem.getTitle()) && item.getAuthor().equals(libraryItem.getAuthor()))
                .findFirst()
                .ifPresent(item -> {
                    int index = libraryItems.indexOf(item);
                    libraryItems.set(index, libraryItem);
                });
    }

    public void deleteItem(LibraryItem item){

    };

    public List<LibraryItem> getLibraryItems() {
        return libraryItems;
    }

    public GsonJSONFileOperationsImpl getFileOperations() {
        return fileOperations;
    }

    public void displayItems() {
        if (libraryItems.size() == 0) {
            System.out.println("No items found in the library.");
        } else {
            System.out.println("Library Items:");
            for (LibraryItem item : libraryItems) {
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

    protected void setLibraryItems(List<LibraryItem> items) {
        libraryItems = items;
    }

    // Other methods and functionality
}