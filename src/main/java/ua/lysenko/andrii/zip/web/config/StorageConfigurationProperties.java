package ua.lysenko.andrii.zip.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zipweb.storage")
@ConstructorBinding
public class StorageConfigurationProperties {
    private final String type;

    public StorageConfigurationProperties(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
