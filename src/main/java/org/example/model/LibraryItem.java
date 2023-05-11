package org.example.model;

public abstract class LibraryItem {

    private String type;
    private String title;
    private String author;

    public LibraryItem(String type,String title, String author) {
        this.type = type;
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLibraryItemType(){return this.type;}


    public abstract void displayItemDetails();
}
