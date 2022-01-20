package ua.lysenko.andrii.zip.web;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AwsS3StorageTest {

//    @Test
    public void test() throws FileNotFoundException {
        File file = new File("C:\\projects\\ZipWeb\\src\\main\\resources\\application.properties");
        Region region = Region.EU_CENTRAL_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket("zip-aws-bucket")
                .key(file.getName())
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("zip-aws-bucket")
                .key("lorem3.txt")
                .build();

        s3.getObject(getObjectRequest);

        s3.putObject(objectRequest, RequestBody.fromInputStream(new FileInputStream(file), file.length()));
    }
}
