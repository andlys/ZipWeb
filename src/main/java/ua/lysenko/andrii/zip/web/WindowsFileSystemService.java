package ua.lysenko.andrii.zip.web;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.lysenko.andrii.zip.Zipper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class WindowsFileSystemService implements StorageService {

    private Path baseDir = Paths.get("D:\\storage\\files\\");
    private Path tmpZipDir = baseDir.getParent().resolve("tmp");

    private void init() throws IOException {
        Files.createDirectories(baseDir);
        Files.createDirectories(tmpZipDir);
    }

    @Override
    public void storeFile(MultipartFile multipartFile) throws IOException {
        init();
        Resource resource = multipartFile.getResource();
        Path uploadedFilePath = baseDir.resolve(resource.getFilename());
        Files.write(uploadedFilePath, resource.getInputStream().readAllBytes());
    }

    @Override
    public Stream<Path> getAllFiles() throws IOException {
        return Files.walk(baseDir).filter(path -> !path.equals(baseDir))
                .filter(path -> ! Files.isDirectory(path))
                .map(path -> baseDir.relativize(path));
    }

    @Override
    public Resource getFile(String fileName) throws FileNotFoundException {
        Path outputZip = tmpZipDir.resolve( FilenameUtils.getBaseName(fileName) + ".zip");
        Zipper.zip(baseDir.resolve(fileName).toString(), outputZip.toString());
        Resource resourceZipped = new InputStreamResource(new FileInputStream(outputZip.toFile()));
        return resourceZipped;
    }

    @Override
    public Resource getAllFilesZip() throws FileNotFoundException {
        Path outputZip = tmpZipDir.resolve(System.currentTimeMillis() + ".zip");
        Zipper.zip(baseDir.toString(), outputZip.toString());
        Resource resourceZipped = new InputStreamResource(new FileInputStream(outputZip.toFile()));
        return resourceZipped;
    }
}
