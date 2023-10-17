package co.edu.javeriana.pedisoft.filemanager.service;

import co.edu.javeriana.pedisoft.filemanager.config.minio.MinioEntityConfig;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class BucketDrainerService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioEntityConfig minioConfig;

    @Scheduled(fixedRate = 60000 * 5)  // 5 Minutos (60000 milis es un minuto)
    private void bucketPurge(){
        log.info("Starting pruning...");
        val objects =  minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(minioConfig.getBucket_name())
                .build());

        objects.forEach(result -> {
            try {
                val item = result.get();
                val objectName = item.objectName();
                val objectCreationTime = item.lastModified().toInstant();
                val currentTime = Instant.now();
                val fiveMinutesAgo = currentTime.minusSeconds(5 * 60);

                if (objectCreationTime.isBefore(fiveMinutesAgo)) {
                    minioClient.removeObject(RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucket_name()).object(objectName).build());
                }
                log.info("Prune completed, next will be in 5 minutes");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }
}
