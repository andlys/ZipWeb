package ua.lysenko.andrii.zip.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.lysenko.andrii.zip.web.AmazonS3Service;
import ua.lysenko.andrii.zip.web.FileSystemService;
import ua.lysenko.andrii.zip.web.StorageService;

import java.nio.file.Paths;

@Configuration
@EnableConfigurationProperties({StorageConfigurationProperties.class,
        FileSystemStorageConfigurationProperties.class, AmazonS3StorageConfigurationProperties.class})
public class StorageConfiguration {
    private final StorageConfigurationProperties storageConfigurationProperties;
    private final FileSystemStorageConfigurationProperties fileSystemStorageConfigurationProperties;
    private final AmazonS3StorageConfigurationProperties amazonS3StorageConfigurationProperties;

    public StorageConfiguration(StorageConfigurationProperties storageConfigurationProperties,
                                FileSystemStorageConfigurationProperties fileSystemStorageConfigurationProperties,
                                AmazonS3StorageConfigurationProperties amazonS3StorageConfigurationProperties) {
        this.storageConfigurationProperties = storageConfigurationProperties;
        this.fileSystemStorageConfigurationProperties = fileSystemStorageConfigurationProperties;
        this.amazonS3StorageConfigurationProperties = amazonS3StorageConfigurationProperties;
    }

    @Bean
    public StorageService storageService() {
        switch (storageConfigurationProperties.getType()) {
            case FS:
                return new FileSystemService(
                        Paths.get(fileSystemStorageConfigurationProperties.getBaseDir()),
                        Paths.get(fileSystemStorageConfigurationProperties.getTmpZipDir()));
            case S3:
                return new AmazonS3Service(amazonS3StorageConfigurationProperties.getBucket(),
                        Paths.get(amazonS3StorageConfigurationProperties.getBaseDir()),
                        Paths.get(amazonS3StorageConfigurationProperties.getTmpZipDir()));
            default:
                throw new RuntimeException("Invalid storage service configuration");
        }
    }
}
