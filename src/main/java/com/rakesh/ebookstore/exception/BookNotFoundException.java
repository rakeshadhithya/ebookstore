package com.rakesh.ebookstore.exception;

public class BookNotFoundException extends RuntimeException{
    //constructor that takes the message
    public BookNotFoundException(String message){
        super(message);
    }
}