package org.example.mongodb;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MongoDBImporter {
    public static void importOrPostJSON(String action, String connectionString, String databaseName, String collectionName, String jsonFilePath) {
        // Create a MongoClient
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            // Get a reference to the database
            MongoDatabase database = mongoClient.getDatabase(databaseName);

            // Get a reference to the collection
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Import or post JSON based on the action
            if ("import".equalsIgnoreCase(action)) {
                importJSON(collection, jsonFilePath);
                System.out.println("JSON file imported successfully.");
            } else if ("post".equalsIgnoreCase(action)) {
                postJSON(collection, jsonFilePath);
                System.out.println("JSON document posted successfully.");
            } else {
                System.out.println("Invalid action. Supported actions are 'import' or 'post'.");
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    private static void importJSON(MongoCollection<Document> collection, String jsonFilePath) throws IOException {
        // Read the JSON file as a string
        String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

        // Parse the JSON string as a JSON array using Gson
        Gson gson = new Gson();
        Document[] documents = gson.fromJson(json, Document[].class);

        // Insert the documents into the collection
        collection.insertMany(List.of(documents));
    }

    private static void postJSON(MongoCollection<Document> collection, String jsonFilePath) throws IOException {
        // Read the JSON file as a string
        String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

        // Parse the JSON string into a BSON document
        Document document = Document.parse(json);

        // Insert the BSON document into the collection
        collection.insertOne(document);
    }

    public static void main(String[] args) {
        String action = "import";  // Specify the action: import or post
        String connectionString = "mongodb://localhost:27017";
        String databaseName = "test";
        String collectionName = "inventory";
        String jsonFilePath = "src/main/java/org/example/data.json";

        importOrPostJSON(action, connectionString, databaseName, collectionName, jsonFilePath);
    }
}