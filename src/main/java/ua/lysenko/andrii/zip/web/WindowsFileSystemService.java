package ua.lysenko.andrii.zip.web;

import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class WindowsFileSystemService extends FileSystemService {

    public WindowsFileSystemService() {
        super(Paths.get("D:\\storage\\files\\"), Path.of("tmp"));
    }
}
