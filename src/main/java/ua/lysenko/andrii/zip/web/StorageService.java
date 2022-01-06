package ua.lysenko.andrii.zip.web;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    public void storeFile(MultipartFile file) throws IOException;

    public Stream<Path> getAllFiles() throws IOException;

    public Resource getFile(String fileName) throws FileNotFoundException;

    public Resource getAllFilesZip() throws FileNotFoundException;
}
