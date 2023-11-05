package co.edu.javeriana.pedisoft.filemanager.service;

import co.edu.javeriana.pedisoft.filemanager.config.minio.MinioEntityConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.util.UUID;

@Service
@Slf4j
public class StorageService {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioEntityConfig minioConfig;

    /**
     * Uploads file on Minio storage
     * @param file File to upload
     * @return a unique identifier for the file uploaded that can be used to download the resource
     */
    public String upload(MultipartFile file){
        val filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucket_name())
                            .object(filename)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(),-1)
                            .build()
            );
        } catch (ErrorResponseException | InvalidKeyException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Unknown error when uploading file: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return filename;
    }

    /**
     * Load Resource from Minio Storage
     * @param filename The unique identifier for the resource
     * @return a resource instance that contains the desired object
     */
    public Resource loadAsResource(String filename) {
        try (InputStream obj = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucket_name())
                .object(minioConfig.getDefault_folder()+"/"+filename)
                .build()
        )){
            byte[] content = IOUtils.toByteArray(obj);
            return new ByteArrayResource(content);
        } catch (ErrorResponseException | InvalidKeyException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Unknown error when downloading file: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
