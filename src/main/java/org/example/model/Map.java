package org.example.model;

public class Map extends LibraryItem {
    private String location;

    public Map(String title, String author,String location) {
        super("map", title, author);
        this.location = location;
    }


    public String getLocation() {
        return location;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("Map Details:");
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());
        System.out.println("Location: " + getLocation());
    }
}