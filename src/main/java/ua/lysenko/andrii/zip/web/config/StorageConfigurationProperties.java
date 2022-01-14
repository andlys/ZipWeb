package ua.lysenko.andrii.zip.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zipweb.storage")
@ConstructorBinding
public class StorageConfigurationProperties {
    private final StorageType type;

    public StorageConfigurationProperties(StorageType type) {
        this.type = type;
    }

    public StorageType getType() {
        return type;
    }

}
