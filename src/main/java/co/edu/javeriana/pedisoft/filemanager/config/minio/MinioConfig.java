package co.edu.javeriana.pedisoft.filemanager.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Autowired
    private MinioEntityConfig minioConfig;

    @Bean
    public MinioClient generateMinioClient() {
        return MinioClient.builder().endpoint(minioConfig.getUrl())
                .credentials(minioConfig.getUsername(), minioConfig.getSecret())
                .build();
    }

}
