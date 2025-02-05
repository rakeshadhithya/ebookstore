package com.rakesh.ebookstore.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;

import org.springframework.web.multipart.MultipartFile;


public interface FileService {
    
    String saveFile(String path, MultipartFile file) throws IOException, FileAlreadyExistsException;

    InputStream getFileInputStream(String path, String fileName) throws FileNotFoundException;
}
