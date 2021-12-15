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

    private Path baseDir = Paths.get("D:\\files\\");

    @Override
    public void storeFile(MultipartFile multipartFile) throws IOException {
        Resource resource = multipartFile.getResource();
        Path uploadedFilePath = Paths.get(baseDir.toString(), resource.getFilename());
        Files.write(uploadedFilePath, resource.getInputStream().readAllBytes());
        Path outputZip = baseDir.resolve( FilenameUtils.getBaseName(resource.getFilename()) + ".zip");
        Zipper.zip(uploadedFilePath.toString(), outputZip.toString());
    }

    @Override
    public Stream<Path> getAllFiles() throws IOException {
        return Files.walk(baseDir).filter(path -> !path.equals(baseDir))
                .map(path -> baseDir.relativize(path));
    }

    @Override
    public Resource getFile(String fileName) throws FileNotFoundException {
        Path outputZip = baseDir.resolve( FilenameUtils.getBaseName(fileName) + ".zip");
        // suppose that file is already zipped
        Resource resourceZipped = new InputStreamResource(new FileInputStream(outputZip.toFile()));
        return resourceZipped;
    }
}
