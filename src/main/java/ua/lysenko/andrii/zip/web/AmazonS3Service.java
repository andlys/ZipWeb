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
import software.amazon.awssdk.services.s3.model.*;
import ua.lysenko.andrii.zip.Zipper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class AmazonS3Service implements StorageService {

    private S3Client s3;
    private final String bucket;
    protected final Path tmpZipDir;

    private Logger log = LoggerFactory.getLogger(AmazonS3Service.class);

    public AmazonS3Service(String bucket, Path tmpZipDir) {
        this.bucket = bucket;
        this.tmpZipDir = tmpZipDir.resolve("all-files");
    }

    @Override
    public void init() throws IOException {
        Region region = Region.EU_CENTRAL_1;
        s3 = S3Client.builder()
                .region(region)
                .build();
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
    public List<String> getAllFiles() {
        return getAllCloudObjects().stream()
                .map(S3Object::key).collect(Collectors.toList());
    }

    @Override
    public Resource getFile(String fileName) throws IOException {
        storeAwsObjectOnLocalStorage(fileName, tmpZipDir);

        Path outputZip = tmpZipDir.resolve( FilenameUtils.getBaseName(fileName) + ".zip");
        Zipper.zip(tmpZipDir.resolve(fileName).toString(), outputZip.toString());
        return new InputStreamResource(new FileInputStream(outputZip.toFile()));
    }

    @Override
    public Resource getAllFilesZip() throws IOException {
        List<String> list = getAllFiles();
        for (File file : tmpZipDir.toFile().listFiles()) {
            Files.delete(file.toPath());
        }
        Files.delete(tmpZipDir);
        log.warn("Wiped out '{}' directory", tmpZipDir.toString());
        // storing all the files temporarily on local storage
        for (String fileName : list) {
            storeAwsObjectOnLocalStorage(fileName, tmpZipDir);
        }
        // zipping all the directory
        Files.createDirectories(tmpZipDir.resolveSibling("zip"));
        Path outputZip = tmpZipDir.resolveSibling("zip").resolve( System.currentTimeMillis() + ".zip");
        log.info(outputZip.toString());
        Zipper.zip(tmpZipDir.toString(), outputZip.toString());
        return new InputStreamResource(new FileInputStream(outputZip.toFile()));
    }

    private List<S3Object> getAllCloudObjects() {
        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucket)
                .build();
        ListObjectsResponse res = s3.listObjects(listObjects);
        return res.contents();
    }

    private void storeAwsObjectOnLocalStorage(String fileName, Path toDir) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        InputStream inputStream = s3.getObject(getObjectRequest);
        FileUtils.copyInputStreamToFile(inputStream, toDir.resolve(fileName).toFile());
    }
}
