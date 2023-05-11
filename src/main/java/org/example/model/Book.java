package org.example.model;

class Book extends LibraryItem {
    private int publicationYear;
    private int isbn;


    public Book(String title, String author, int publicationYear, int isbn) {
        super(title, author);
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }


    public int getIsbn() {
        return isbn;
    }


    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("Book Details:");
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());
        System.out.println("Publication Year: " + getPublicationYear());
    }
}
