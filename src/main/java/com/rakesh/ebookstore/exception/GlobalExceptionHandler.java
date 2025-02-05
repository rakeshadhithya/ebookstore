package com.rakesh.ebookstore.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//return type ProblemDetail(for status and message) is better than sending response entity
//(it is used to send status with objects)

@RestControllerAdvice
public class GlobalExceptionHandler {

    //logger
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    //handle filealreadyexists
    @ExceptionHandler(FileAlreadyExistsException.class)
    public ProblemDetail handleFileNotFoundException(FileAlreadyExistsException e){
        logger.error("The filename of given file already exists in "+e.getMessage());

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, 
                        "Given file name already exists, provide another file or change filename");
    }

    //handle filenotfoundexception
    @ExceptionHandler(FileNotFoundException.class)
    public ProblemDetail handleFileNotFoundException(FileNotFoundException e){
        logger.error("The file is not found with given parameters "+e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "file not found with given parameters");
    }

    //handle ioexception
    public ProblemDetail handleIOException(IOException e){
        logger.error("error in file i/o operation "+e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "error in file i/o operation");
    }


    //handle BookNotFound exception
    @ExceptionHandler(BookNotFoundException.class)
    public ProblemDetail handleBookNotFoundException(BookNotFoundException e){
        logger.error("book with given id is not found"+e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "book with given id is not found");
    }

    //handle BookId missing exception
    @ExceptionHandler(BookIdMissingException.class)
    public ProblemDetail handleBookIdMissingException(BookIdMissingException e){
        logger.error("book is not present please provide a valid email id"+e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "book id not present, plase provide a valid book id");
    }


}
