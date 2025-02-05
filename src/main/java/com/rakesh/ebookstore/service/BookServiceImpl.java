package com.rakesh.ebookstore.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import com.rakesh.ebookstore.dto.BookDto;
import com.rakesh.ebookstore.dto.BookPageResponse;
import com.rakesh.ebookstore.entity.BookEntity;
import com.rakesh.ebookstore.exception.BookNotFoundException;
import com.rakesh.ebookstore.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

    //book repo dependency
    private final BookRepository bookRepository;
    //fileservice dependency
    private final FileService fileService;
    //model mapper dependency
    private final ModelMapper modelMapper;
    //path dependency to save or retrive photos using fileservice
    @Value("${ebookstore.coverPhotosPath}")
    private String bookCoverPhotosPath;
    //base dependency url path
    @Value("${base.url}")
    private String baseUrl;
    //logger
    Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    
    //constructor inject
    public BookServiceImpl(BookRepository bookRepository, FileService fileService, ModelMapper modelMapper){
        this.bookRepository = bookRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }




    @Override
    public BookDto addBook(BookDto bookDto, MultipartFile multipartFile) throws FileAlreadyExistsException, IOException {
        //log for request recieved
        logger.info("request for add book recieved by addBook method of bookServiceImpl");

        //1. save book in database
        //covert dto to book entity
        BookEntity book = modelMapper.map(bookDto, BookEntity.class);
        //save the book cover photo in project directory and get its name
        String savedFileName = fileService.saveFile(bookCoverPhotosPath, multipartFile);
        //log for book saved in project directory successful
        logger.info("given book cover photo saved in project directory successful");
        
        
        //assign the savedFileName to book entity
        book.setCoverPhotoName( savedFileName );
        //save the book in database
        BookEntity savedBook = bookRepository.save(book);
        //log for book saved in database successful
        logger.info("Given book saved in database with cover photo name successful");

        
        //2. send the saved book in database as dto
        //convert saved book to dto
        BookDto savedBookDto = modelMapper.map(savedBook, BookDto.class);
        //generate the url (basically they get using the getter method of file controller)
        String url = baseUrl + "/file/get/" + savedBookDto.getCoverPhotoName();
        //add url to the saved book dto
        savedBookDto.setCoverPhotoUrl(url);


        //log for add book request successfull
        logger.info("request for add book successfully processed");
        //return the dto
        return savedBookDto;

    }

    @Override
    public BookDto findBookById(Integer bookId) throws BookNotFoundException {
        //log 
        logger.info("request for getting book recieved by getBook method of BookServiceImpl");

        //get the book using repo, if not found throw book not found exception
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("book with given id not found") );

        //return the book as dto
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        //generate url to access the cover photo image for the cover photo name
        String url = baseUrl + "/file/get/" + book.getCoverPhotoName();
        //set this url in the dto and return
        bookDto.setCoverPhotoUrl(url);
        //log
        logger.info("get book request processed succesfully");
        //return this dto
        return bookDto;

    }

    @Override
    public List<BookDto> findAllBooks() {
        //log
        logger.info("request recieved by findAllBooks method of BookServiceImpl");

        //get from repository
        List<BookEntity> bookEntityList = bookRepository.findAll();

        List<BookDto> bookDtoList = new ArrayList<>();

        //convert each book entity to book dto while adding url of the cover photo
        for(BookEntity bookEntity : bookEntityList){
            BookDto bookDto = modelMapper.map(bookEntity, BookDto.class);
            bookDto.setCoverPhotoUrl( baseUrl + "/file/get/" + bookEntity.getCoverPhotoName());
            bookDtoList.add(bookDto);
        }

        //log
        logger.info("request process successly by findAllBooks metho");
        //return the list
        return bookDtoList;
    }

    
    @Override
    public String deleteBook(Integer bookId) throws BookNotFoundException, IOException {
        //log
        logger.info("request recieved by delteBook method of BookServiceImpl");

        //find the book in the database else throw do not exists
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(()-> new BookNotFoundException("Book is not found with the given id/"));
        //delete the book with the id
        bookRepository.deleteById(bookId);
        //delete the cover photo associated with the book from project directory
        Files.deleteIfExists(Path.of(bookCoverPhotosPath + File.separator + bookEntity.getCoverPhotoName()));

        //log
        logger.info("deleted file successfully");
        //return the message
        return "Book with book id: " + bookId +" deleted successfully";

    }



    //api to handle update file
    @PutMapping("/update-book/{bookId}")
    public BookDto updateBook(Integer bookId, BookDto bookDto, MultipartFile multipartFile) throws FileAlreadyExistsException, IOException{
        //log
        logger.info("request for updating book received by updateBook method of BookServiceImpl");

        //get the book object by book Id, if not there throw an error
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException("book with given id is not found"));

        //if multipart file is not given, keep the old file(i.e. set the coverPhotoName as previoours file)
        if(multipartFile == null){
            //log
            logger.info("multipart file not provided");
            bookDto.setCoverPhotoName(bookEntity.getCoverPhotoName());
            //log
            logger.info("file name set to: existing file name associated with book id= "+ bookEntity.getCoverPhotoName());
        }
        //if multipart file is given, then delete the previous file and add new file
        if(multipartFile != null){
            logger.info("multipart file is provided");
            //delete exiting file of the book entity 
            Path filePath = Path.of(bookCoverPhotosPath + File.separator + bookEntity.getCoverPhotoName());
            //log
            logger.info("attempting delete at absolute path: " + filePath.toAbsolutePath());
            Files.deleteIfExists(filePath);
            //log
            logger.info("file deletion successfull");

            //now save new file
            //log
            logger.info("attempting to save file using saveFile method of FileService");
            String savedFileName = fileService.saveFile(bookCoverPhotosPath, multipartFile);
            //change the name in book dto
            bookDto.setCoverPhotoName(savedFileName);
            //log
            logger.info("file name set to: " + savedFileName);
        }

         //map bookDto to bookEntity and save in repository
         //set id as given for the dto
         bookDto.setBookId(bookId);
         bookEntity = modelMapper.map(bookDto, BookEntity.class); 
         BookEntity updatedBookEntity = bookRepository.save(bookEntity);
 
         //convert updated book entity to updated book dto and return
         BookDto updatedBookDto = modelMapper.map(updatedBookEntity, BookDto.class);
         //get the url and assign to it
         updatedBookDto.setCoverPhotoUrl(baseUrl + "/file/get/" + updatedBookDto.getCoverPhotoName());
         //return the dto
         return updatedBookDto;

    }





    @Override
    public BookPageResponse getAllBooksWithPagination(Integer pageNumber, Integer pageSize) {

        //build pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        //get the records page as per pageable
        Page<BookEntity> booksPage = bookRepository.findAll(pageable);
        //get the books inside page
        List<BookEntity> booksListInPage = booksPage.getContent();

        //covert bookentity list to bookdto list while adding url in book dto
        List<BookDto> bookDtosListInPage = new ArrayList<>();
        for(BookEntity book : booksListInPage){
            //convert bookentity to bookdto
            BookDto bookDto = modelMapper.map(book, BookDto.class);
            //set the url for cover photo
            bookDto.setCoverPhotoUrl(baseUrl + "/file/get/" + book.getCoverPhotoName());
            //add book dto to list
            bookDtosListInPage.add(bookDto);
        }

        //return the dto list along with page details
        return new BookPageResponse(bookDtosListInPage, pageNumber, pageSize, booksPage.getTotalElements(), booksPage.getTotalPages(), booksPage.isLast());
    }

    @Override
    public BookPageResponse getAllBooksWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy,
            String direction) {

        //create sort object based on direction
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        //create pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        //get the records page as per pageable
        Page<BookEntity> booksPage = bookRepository.findAll(pageable);
        //get the books inside page
        List<BookEntity> booksListInPage = booksPage.getContent();

        //covert bookentity list to bookdto list while adding url in book dto
        List<BookDto> bookDtosListInPage = new ArrayList<>();
        for(BookEntity book : booksListInPage){
            //convert bookentity to bookdto
            BookDto bookDto = modelMapper.map(book, BookDto.class);
            //set the url for cover photo
            bookDto.setCoverPhotoUrl(baseUrl + "/file/get/" + book.getCoverPhotoName());
            //add book dto to list
            bookDtosListInPage.add(bookDto);
        }

        //return the dto list along with page details
        return new BookPageResponse(bookDtosListInPage, pageNumber, pageSize, booksPage.getTotalElements(), booksPage.getTotalPages(), booksPage.isLast());
    }

    
}
