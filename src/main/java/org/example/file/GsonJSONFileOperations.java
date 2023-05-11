package org.example.file;

import com.google.gson.JsonArray;

import java.io.IOException;

public interface GsonJSONFileOperations {
    JsonArray readJSONFile(String fileName) throws IOException;
    void writeJSONFile(String fileName, JsonArray jsonArray) throws IOException;
}