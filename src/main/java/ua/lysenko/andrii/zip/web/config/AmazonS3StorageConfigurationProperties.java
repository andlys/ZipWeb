package ua.lysenko.andrii.zip.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zipweb.storage.s3")
@ConstructorBinding
public class AmazonS3StorageConfigurationProperties {
    private final String bucket;
    private final String tmpZipDir;

    public AmazonS3StorageConfigurationProperties(String bucket, String tmpZipDir) {
        this.bucket = bucket;
        this.tmpZipDir = tmpZipDir;
    }

    public String getBucket() {
        return bucket;
    }

    public String getTmpZipDir() {
        return tmpZipDir;
    }
}
