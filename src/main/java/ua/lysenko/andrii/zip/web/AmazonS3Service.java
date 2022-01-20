package ua.lysenko.andrii.zip.web;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class AmazonS3Service implements StorageService {

    private S3Client s3;

    @Override
    public void init() throws IOException {
        Region region = Region.EU_CENTRAL_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();
    }

    @Override
    public void storeFile(MultipartFile file) throws IOException {
//        createBucket(s3, bucketName, region);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket("zip-aws-bucket")
//                .key("")
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    @Override
    public Stream<Path> getAllFiles() throws IOException {
        return null;
    }

    @Override
    public Resource getFile(String fileName) throws FileNotFoundException {
        return null;
    }

    @Override
    public Resource getAllFilesZip() throws FileNotFoundException {
        return null;
    }
}
