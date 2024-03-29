package org.example.model;

public class Magazine extends LibraryItem {
    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    private int issueNumber;

    public Magazine(String title, String author, int issueNumber) {
        super("magazine", title, author);
        this.issueNumber = issueNumber;
    }


    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("Magazine Details:");
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());
        System.out.println("Issue Number: " + getIssueNumber());
    }
}