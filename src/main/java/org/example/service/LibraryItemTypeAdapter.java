package org.example.service;

import com.google.gson.*;
import org.example.model.Book;
import org.example.model.LibraryItem;
import org.example.model.Magazine;
import org.example.model.Map;

import java.lang.reflect.Type;

public class LibraryItemTypeAdapter implements JsonSerializer<LibraryItem>, JsonDeserializer<LibraryItem> {
    @Override
    public JsonElement serialize(LibraryItem src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", src.getLibraryItemType());
        jsonObject.addProperty("title", src.getTitle());
        jsonObject.addProperty("author", src.getAuthor());
        if(src instanceof Book){
            jsonObject.addProperty("isbn",((Book) src).getIsbn());
            jsonObject.addProperty("author", ((Book) src).getPublicationYear());
        }
        else if(src instanceof Magazine){
            jsonObject.addProperty("issueNumber",((Magazine) src).getIssueNumber());
        }
        else if(src instanceof Map){
            jsonObject.addProperty("location",((Map) src).getLocation());
        }

        return jsonObject;
    }

    @Override
    public LibraryItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String itemType = jsonObject.get("type").getAsString();
        String itemTitle = jsonObject.get("title").getAsString();
        String itemAuthor = jsonObject.get("author").getAsString();
        // Based on the itemType, deserialize the JSON properties into the appropriate subclass of LibraryItem
        switch (itemType) {
            case "book" -> {
                // Deserialize as Book and return
                int publicationYear = jsonObject.get("publicationYear").getAsInt();
                int isbn = jsonObject.get("isbn").getAsInt();
                return new Book(itemTitle,itemAuthor,publicationYear,isbn);
            }
            case "magazine" -> {
                // Deserialize as Magazine and return
                int issueNumber = jsonObject.get("issueNumber").getAsInt();
                return new Magazine(itemTitle,itemAuthor,issueNumber);
            }
            case "map" -> {
                // Deserialize as Map and return
                String location = jsonObject.get("location").getAsString();
                return new Map(itemTitle,itemAuthor,location);
            }
        }

        // Return null or throw an exception if the itemType is unknown or unsupported
        return null;
    }

}

