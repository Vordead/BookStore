package org.example.model;

import java.util.Objects;

public abstract class LibraryItem {

    private final String type;
    private final String title;
    private final String author;

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

    public String getLibraryItemType(){return this.type;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LibraryItem otherItem = (LibraryItem) obj;
        return title.equals(otherItem.title) && author.equals(otherItem.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }


    public abstract void displayItemDetails();
}
