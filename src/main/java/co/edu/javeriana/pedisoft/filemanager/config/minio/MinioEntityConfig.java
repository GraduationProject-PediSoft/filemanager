package co.edu.javeriana.pedisoft.filemanager.config.minio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioEntityConfig {
    private String bucket_name;
    private String default_folder;
    private String username;
    private String secret;
    private String url;
}
