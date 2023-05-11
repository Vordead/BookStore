package org.example.file;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GsonJSONFileOperationsImpl implements GsonJSONFileOperations {

    private final Gson gson;

    public GsonJSONFileOperationsImpl() {
        gson = new Gson();
    }

    @Override
    public JsonArray readJSONFile(String fileName) throws IOException {
        File file = new File(fileName);
        try (JsonReader reader = new JsonReader(new FileReader(file))) {
            return gson.fromJson(reader, JsonArray.class);
        } catch (JsonIOException | JsonSyntaxException e) {
            throw new IOException("Failed to read JSON file: " + e.getMessage());
        }
    }


    @Override
    public void writeJSONFile(String fileName, JsonArray jsonArray) throws IOException {
        try (JsonWriter writer = new JsonWriter(new FileWriter(fileName))) {
            gson.toJson(jsonArray, writer);
        } catch (JsonIOException e) {
            throw new IOException("Failed to write JSON file: " + e.getMessage());
        }
    }
}

