package org.example.mongodb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.Arrays;

public class MongoDBImporter {
    private static JsonArray jsonArray = new JsonArray();

    public static void importOrPostJSON(String action, String connectionString, String databaseName, String collectionName, JsonArray jsonElements) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            if ("import".equalsIgnoreCase(action)) {
                importJSON(collection, jsonElements);
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

        Document[] documents = gson.fromJson(jsonArray, Document[].class);
        collection.insertMany(Arrays.asList(documents));
    }

    private static JsonArray readJSONArrayFromMongoDB(MongoCollection<Document> collection) {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new GsonBuilder().serializeNulls().create();

        FindIterable<Document> iterable = collection.find().skip(1); // Skip the first JSON object
        for (Document document : iterable) {
            JsonObject jsonObject = gson.fromJson(document.toJson(), JsonObject.class);
            jsonObject.remove("_id");
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    public static JsonArray getJsonArray() {
        return jsonArray;
    }
}