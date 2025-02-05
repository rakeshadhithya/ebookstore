package com.rakesh.ebookstore.exception;

public class BookIdMissingException extends RuntimeException{
    public BookIdMissingException(String message){
        super(message);
    }
}
