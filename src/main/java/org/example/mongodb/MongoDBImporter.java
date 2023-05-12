package org.example.mongodb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MongoDBImporter {

     public static JsonArray jsonArray = new JsonArray();

    public static void importOrPostJSON(String action, String connectionString, String databaseName, String collectionName, JsonArray jsonElements) {

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
                importJSON(collection, jsonElements );
                System.out.println("JSON file imported successfully.");
            } else if ("read".equalsIgnoreCase(action)) {
                jsonArray = readJSONArrayFromMongoDB(collection);
                System.out.println("JSON document read successfully.");
            } else {
                System.out.println("Invalid action. Supported actions are 'import' or 'post'.");
            }
        }
    }

    private static void importJSON(MongoCollection<Document> collection, JsonArray jsonArray) {
        Gson gson = new Gson();

        // Convert the JsonArray to an array of Documents
        Document[] documents = gson.fromJson(jsonArray, Document[].class);

        // Insert the documents into the collection
        collection.insertMany(Arrays.asList(documents));
    }

    private static void postJSON(MongoCollection<Document> collection, String jsonFilePath) throws IOException {
        // Read the JSON file as a string
        String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

        // Parse the JSON string into a BSON document
        Document document = Document.parse(json);

        // Insert the BSON document into the collection
        collection.insertOne(document);
    }

    public static JsonArray readJSONArrayFromMongoDB(MongoCollection<Document> collection) {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new GsonBuilder().serializeNulls().create();

        // Retrieve all documents from the collection
        FindIterable<Document> iterable = collection.find();
        boolean isFirst = true; // Flag to track the first JSON object
        for (Document document : iterable) {
            if (isFirst) {
                isFirst = false;
                continue; // Skip the first JSON object
            }
            JsonObject jsonObject = gson.fromJson(document.toJson(), JsonObject.class);
            jsonObject.remove("_id");  // Remove the "_id" field
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }
}