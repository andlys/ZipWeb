package ua.lysenko.andrii.zip.web;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import ua.lysenko.andrii.zip.Zipper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AmazonS3Service implements StorageService {

    private S3Client s3;
    private final String bucket;
    protected final Path baseDir;
    protected final Path tmpZipDir;

    private Logger log = LoggerFactory.getLogger(AmazonS3Service.class);

    public AmazonS3Service(String bucket, Path baseDir, Path tmpZipDir) {
        this.bucket = bucket;
        this.baseDir = baseDir;
        this.tmpZipDir = baseDir.getParent().resolve(tmpZipDir);
    }

    @Override
    public void init() throws IOException {
        Region region = Region.EU_CENTRAL_1;
        s3 = S3Client.builder()
                .region(region)
                .build();
        Files.createDirectories(baseDir);
        Files.createDirectories(tmpZipDir);
    }

    @Override
    public void storeFile(MultipartFile file) throws IOException {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(file.getResource().getFilename())
                .build();
        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    @Override
    public List<String> getAllFiles() throws IOException {
        return List.of("lorem3.txt");
    }

    @Override
    public Resource getFile(String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        InputStream inputStream = s3.getObject(getObjectRequest);
        FileUtils.copyInputStreamToFile(inputStream, tmpZipDir.resolve(fileName).toFile());

        Path outputZip = tmpZipDir.resolve( FilenameUtils.getBaseName(fileName) + ".zip");
        Zipper.zip(tmpZipDir.resolve(fileName).toString(), outputZip.toString());
        return new InputStreamResource(new FileInputStream(outputZip.toFile()));
    }

    @Override
    public Resource getAllFilesZip() throws FileNotFoundException {
        return null;
    }
}
