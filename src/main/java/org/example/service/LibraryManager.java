package org.example.service;


import com.google.gson.JsonArray;
import org.example.file.GsonJSONFileOperationsImpl;
import org.example.model.LibraryItem;

import java.io.IOException;

public abstract class LibraryManager {
    private JsonArray libraryItems;
    private GsonJSONFileOperationsImpl fileOperations;

    public LibraryManager() {
        fileOperations = new GsonJSONFileOperationsImpl();
        libraryItems = new JsonArray();
    }

    public void loadDataFromJsonFile() throws IOException {
        this.libraryItems = fileOperations.readJSONFile("/Users/mohamadhamade/IdeaProjects/BookStore/src/main/java/org/example/data.json");
        System.out.println("Data loaded from JSON file successfully.");
    }

    public abstract void addItem(LibraryItem item);

    public abstract void updateItem(LibraryItem item);

    public abstract void deleteItem(LibraryItem item);

    public abstract void displayItems();

    // Other methods and functionality
}