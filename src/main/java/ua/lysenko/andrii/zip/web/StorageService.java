package ua.lysenko.andrii.zip.web;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface StorageService {

    public void init() throws IOException;

    public void storeFile(MultipartFile file) throws IOException;

    public List<String> getAllFiles() throws IOException;

    public Resource getFile(String fileName) throws IOException;

    public Resource getAllFilesZip() throws IOException;
}
