package ua.lysenko.andrii.zip.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zipweb.storage.fs")
@ConstructorBinding
public class FileSystemStorageConfigurationProperties {
    private final String baseDir;
    private final String tmpZipDir;

    public FileSystemStorageConfigurationProperties(String baseDir, String tmpZipDir) {
        this.baseDir = baseDir;
        this.tmpZipDir = tmpZipDir;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public String getTmpZipDir() {
        return tmpZipDir;
    }
}
