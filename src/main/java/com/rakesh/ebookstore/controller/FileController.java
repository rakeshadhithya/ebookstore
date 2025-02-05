package com.rakesh.ebookstore.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rakesh.ebookstore.service.FileService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

    //dependency: fileservice
    private final FileService fileService;
    //logger
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    //path for saving or retriving cover photos of books 
    
    @Value("${ebookstore.coverPhotosPath}")
    private String bookCoverPhotosPath;

    //for constructor spring automatically injects the bean
    FileController(FileService fileService){
        this.fileService = fileService;
    }


    //api handler for uploading file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile multipartFile) throws IOException{
        //log info of request recieved
        logger.info("Request for the upload file has been received by uplaodFileHandler method of FileController");

        //call the service method for saving file to the project file system
        String fileName = fileService.saveFile(bookCoverPhotosPath, multipartFile);

        //log info of request successfully process
        logger.info("File uploading successful");

        //return the reponse
        return ResponseEntity.ok("File Upload Successful. File Name: "+ fileName);
    }

    
    //api handler to get the file
    @GetMapping("/get/{fileName}")
    public void getFileHandler(@PathVariable String fileName, HttpServletResponse response ) throws IOException{
        //log info of request recieved
        logger.info("Request for getting the file is recieved by getFileHandler of FileController");


        //try is used to close streams properly
        try(//get input stream of the file
            InputStream fileInputStream = fileService.getFileInputStream(bookCoverPhotosPath, fileName);
            //get the output stream of the response
            OutputStream responseOutputStream = response.getOutputStream();
            ){
                //set the contect type of response to ensure if is correctly interpreted by client
                response.setContentType("image/png");
                //get the stream from inputstream and copy to outputstream
                StreamUtils.copy(fileInputStream, responseOutputStream);
        }

        //log info of request processed successfully
        logger.info("File has been sent successfully");
    }
}
