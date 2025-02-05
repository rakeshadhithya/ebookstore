package com.rakesh.ebookstore.controller;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakesh.ebookstore.dto.BookDto;
import com.rakesh.ebookstore.dto.BookPageResponse;
import com.rakesh.ebookstore.exception.BookNotFoundException;
import com.rakesh.ebookstore.service.BookService;
import com.rakesh.ebookstore.utils.AppConstants;

@RestController
@RequestMapping("/book")
public class BookController {

    //service dependency
    private final BookService bookService;
    //logger dependency
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    //constructor inject
    public BookController(BookService bookService){
        this.bookService = bookService;
    }
    

    
    //covert given string to bookdto object
    private BookDto convertStringToBookDto(String bookDtoString) throws JsonMappingException, JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(bookDtoString, BookDto.class);
    }


    //api to handle add book. here for convience in using postman model attribute is taken so that individual attributes can be writtern
    @PostMapping("/add-book")
    public ResponseEntity<BookDto> addBookHander(@RequestPart String bookDtoString, 
                                     @RequestPart MultipartFile multipartFile) throws FileAlreadyExistsException, IOException{
        
        //log
        logger.info("request for adding book recieved by addBookHandler of BookController");
        //covert the dtostring to dtoobject
        BookDto bookDto = convertStringToBookDto(bookDtoString);
        //call the service
        BookDto addedBookDto= bookService.addBook(bookDto, multipartFile);
        //log
        logger.info("request fo adding book successfully processed");
        //send the dto 
        return ResponseEntity.ok(addedBookDto);
    }


    //api to handle get book
    @GetMapping("/get/{bookId}")
    public ResponseEntity<BookDto> findBookById(@PathVariable Integer bookId){
        //log
        logger.info("request for getting a book by id recieved by findBookById method of BookController");
        //call the service
        BookDto bookDto = bookService.findBookById(bookId);
        //log
        logger.info("request for getting a book by id successfully processed ");
        //return in response
        return ResponseEntity.ok(bookDto);
    }

    //api to handle get all books
    @GetMapping("get/all")
    public ResponseEntity<List<BookDto>> findAllBooks(){
        //log
        logger.info("request for finding all books recieved by findAllBooks of BookController");
        //get the bookdto list with service
        List<BookDto> bookDtoList = bookService.findAllBooks();
        //log
        logger.info("request for getting all books is processed");
        //return list of book dto
        return ResponseEntity.ok(bookDtoList);
    }

    //api to handle update book
    @PutMapping("/update-book/{bookId}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Integer bookId, @RequestPart String bookDtoString, @RequestPart(required = false) MultipartFile multipartFile) throws FileAlreadyExistsException, IOException{
        //log
        logger.info("request for update book successfully recieved by updateBook method of BookController");
        //covert given dtostring to dtoobject
        BookDto bookDto = convertStringToBookDto(bookDtoString);
        //use the bookservice to update book
        BookDto updatedBookDto = bookService.updateBook(bookId, bookDto, multipartFile);
        //log
        logger.info("processed successfully for updating the book");
        //return the updated dto
        return ResponseEntity.ok(updatedBookDto);
    }

    //api to handle delete book
    @DeleteMapping("/delete-book/{bookId}")
    public ResponseEntity<String> deleteBookById(@PathVariable Integer bookId ) throws BookNotFoundException, IOException{
        //log
        logger.info("request for deleting the book is recieved by deleteBookById of BookController");
        //delete using the service
        String message = bookService.deleteBook(bookId);
        //log
        logger.info("request for deleting book by id successfully processed");
        //return reposne
        return ResponseEntity.ok(message);
    }




    //pagination and sorting

    //api to handle getting books in page and no sort
    @GetMapping("/get/books-page")
    public ResponseEntity<BookPageResponse> getBooksWithPagination(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                        @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize){

        //log
        logger.info("page number given: " + pageNumber);
        logger.info("page size given: " + pageSize);
        //call the service to return page
        return ResponseEntity.ok(bookService.getAllBooksWithPagination(pageNumber, pageSize));
    }

    //api to handle getting books in page with sorting
    @GetMapping("/get/books-page-sort")
    public ResponseEntity<BookPageResponse> getBooksWithPaginationAndSorting(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                            @RequestParam(defaultValue= AppConstants.SORT_BY, required = false) String sortBy,
                                                                            @RequestParam(defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDirection){
        //call the service and return
        return ResponseEntity.ok(bookService.getAllBooksWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDirection));
    }

}
