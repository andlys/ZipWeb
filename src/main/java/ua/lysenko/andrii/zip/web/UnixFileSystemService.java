package ua.lysenko.andrii.zip.web;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UnixFileSystemService extends FileSystemService {

    public UnixFileSystemService() {
        super(Paths.get("/Users/andriilysenko/storage/files"), Path.of("tmp"));
    }

}
