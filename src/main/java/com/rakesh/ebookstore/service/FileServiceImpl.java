package com.rakesh.ebookstore.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileServiceImpl implements FileService {

    //logger
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String saveFile(String directoryPath, MultipartFile multiPartFile) throws IOException, FileAlreadyExistsException {
        //log info of request recieved
        logger.info("request for save file in the path is recieved by saveFile method of FileServiceImpl");

        //check if the directory for given path exists, else create one
        File directory = new File(directoryPath);
        //logs
        logger.info("Directory exists: " + directory.exists());
        logger.info("Can read: " + directory.canRead());
        logger.info("Can write: " + directory.canWrite());
        if( !directory.exists()){
            //returns true if created, false if already exists or cannot create
            directory.mkdir();
            //log info of new directory made
            logger.info("new directory created. path= " + directoryPath);
        }

        String fileName = multiPartFile.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            //log
            logger.error("Attempting to write to directory, File name is missing!");
            throw new IOException("File name is missing!");
        }
        //generate path object for the given directory path along with file name
        //log
        logger.info("File name: " + multiPartFile.getOriginalFilename()); 
        Path filePath = Path.of(directoryPath + File.separator + fileName);


        //try to release resources
        try(//use files.copy (to get inputstream stream form given file,copy using outputstream to a path)
            InputStream fileInputStream = multiPartFile.getInputStream();
            ){
            //log
            logger.info("Attempting to save file at: " + filePath.toAbsolutePath());
            Files.copy(fileInputStream, filePath);
        }
        //if file name already exists, exception is handled by the controller to send appropriate message


        //log of file copy successful
        logger.info("file copy successfull"); 
       
        //return file name
        return multiPartFile.getOriginalFilename();
    }

    @Override
    public InputStream getFileInputStream(String path, String fileName) throws FileNotFoundException {
        //log for request recieved
        logger.info("request for getting input stream of a file in a directory recieved by getFileInputStream method of FileServiceImpl");

        //generate a full path string with file name
        String fullPath = path + File.separator + fileName;
        //if file do not exists throw file do not exist
        if( ! Files.exists(Path.of(fullPath))){
            throw new FileNotFoundException();
        }

        //log for request processed successfully
        logger.info("request for input stream of file successfully processed");

        //send the file input stream for it. stream should be closed by the caller
        return new FileInputStream(fullPath);
       
    }
    
}
