package co.edu.javeriana.pedisoft.filemanager.controller;

import co.edu.javeriana.pedisoft.filemanager.service.StorageService;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class FileController {

    @Autowired
    private StorageService storageService;

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file){
        return storageService.upload(file);
    }

    @SneakyThrows
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        val file = storageService.loadAsResource(filename);


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + filename + "\"")
                .body(file);
    }

}
