package ua.lysenko.andrii.zip.web;

import org.springframework.stereotype.Service;

import java.nio.file.Paths;

//@Service
public class UnixFileSystemService extends FileSystemService {

    public UnixFileSystemService() {
        baseDir = Paths.get("/Users/andriilysenko/storage/files");
        tmpZipDir = baseDir.getParent().resolve("tmp");
    }

}
