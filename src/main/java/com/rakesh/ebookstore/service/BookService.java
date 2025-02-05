package com.rakesh.ebookstore.service;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rakesh.ebookstore.dto.BookDto;
import com.rakesh.ebookstore.dto.BookPageResponse;
import com.rakesh.ebookstore.exception.BookNotFoundException;

public interface BookService  {
    
        BookDto addBook(BookDto bookDto, MultipartFile multipartFile) throws FileAlreadyExistsException, IOException;

        BookDto findBookById(Integer bookId) throws BookNotFoundException;

        List<BookDto> findAllBooks();

        BookDto updateBook(Integer bookId, BookDto bookDto, MultipartFile multipartFile) throws FileAlreadyExistsException, IOException;

        String deleteBook(Integer bookId) throws BookNotFoundException, IOException;

        //different response for page requests
        BookPageResponse getAllBooksWithPagination(Integer pageNumber, Integer pageSize);

        BookPageResponse getAllBooksWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String direction);


}
