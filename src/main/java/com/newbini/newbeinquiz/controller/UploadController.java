package com.newbini.newbeinquiz.controller;

import com.newbini.newbeinquiz.web.request.MultiPartFilesForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RestController
public class UploadController {

    @Async
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("attach_file") List<MultipartFile> files) {
        log.info("files ={}", files);
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File dest = new File("C:\\Users\\82109\\Desktop\\Newbini\\src\\main\\resources\\files\\" + fileName);
                try {
                    file.transferTo(dest);
                } catch (IOException e) {
                    return ResponseEntity.status(500).body("Failed to upload " + fileName);
                }



            }
        }
        return ResponseEntity.ok("Files uploaded successfully");
    }
}
