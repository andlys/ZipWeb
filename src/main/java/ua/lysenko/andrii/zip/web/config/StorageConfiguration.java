package ua.lysenko.andrii.zip.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.lysenko.andrii.zip.web.FileSystemService;
import ua.lysenko.andrii.zip.web.StorageService;

import java.nio.file.Paths;

@Configuration
@EnableConfigurationProperties({StorageConfigurationProperties.class, FileSystemStorageConfigurationProperties.class})
public class StorageConfiguration {
    private final StorageConfigurationProperties storageConfigurationProperties;
    private final FileSystemStorageConfigurationProperties fileSystemStorageConfigurationProperties;

    public StorageConfiguration(StorageConfigurationProperties storageConfigurationProperties,
                                FileSystemStorageConfigurationProperties fileSystemStorageConfigurationProperties) {
        this.storageConfigurationProperties = storageConfigurationProperties;
        this.fileSystemStorageConfigurationProperties = fileSystemStorageConfigurationProperties;
    }

    @Bean
    public StorageService storageService() {
        if (storageConfigurationProperties.getType().equals("fs")) {
            return new FileSystemService(
                    Paths.get(fileSystemStorageConfigurationProperties.getBaseDir()),
                    Paths.get(fileSystemStorageConfigurationProperties.getTmpZipDir())
            );
        }

        throw new RuntimeException("Invalid storage service configuration");
    }
}
