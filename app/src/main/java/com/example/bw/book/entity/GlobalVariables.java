package com.example.bw.book.entity;

import android.app.Application;

public class GlobalVariables extends Application{

    String currentBook;

    public String getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(String currentBook) {
        this.currentBook = currentBook;
    }
}