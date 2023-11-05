package co.edu.javeriana.pedisoft.filemanager.config;

import co.edu.javeriana.pedisoft.filemanager.config.minio.MinioEntityConfig;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MinioBucketChecker implements ApplicationRunner {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioEntityConfig minioConfig;

    /**
     * Executes on startup and checks the Minio Server if the bucket exists, if no, the service creates it
     * @param args cli args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception{
        final var found =  minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket_name()).build());
        if (!found) {
            log.info("Bucket not found, creating...");
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket_name()).build());
            log.info("Bucket Created");
        } else {
            log.info("Bucket already exists, all ok");
        }

    }
}
