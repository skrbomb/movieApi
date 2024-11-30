package com.movie.controllers;


import com.movie.service.File.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /*  project.poster= posters/  */
    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile file) {
        try {
            String uploadFileName=fileService.uploadFile(path,file);
            return ResponseEntity.ok("File uploaded: "+uploadFileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File upload failed");
        }
    }

    @GetMapping("/{fileName}")
    public void serveFileHandler(@PathVariable String fileName,
                                 HttpServletResponse response) throws IOException {
        InputStream resourceFile = fileService.getResourceFile(path, fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resourceFile,response.getOutputStream());
        /*使用 StreamUtils.copy 將檔案內容（輸入流）複製到回應的輸出流中。*/
    }
}
