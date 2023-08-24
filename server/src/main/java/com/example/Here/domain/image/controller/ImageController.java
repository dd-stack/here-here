package com.example.Here.domain.image.controller;

import com.example.Here.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(imageService.uploadImage(file));
        }
        catch (MaxUploadSizeExceededException e){
            return new ResponseEntity<>("파일이 5MB를 초과할 수 없습니다.", HttpStatus.EXPECTATION_FAILED);
        }
        catch (IOException e){
            return new ResponseEntity<>("파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {

        return ResponseEntity.ok(imageService.deleteImage(fileUrl));
    }
}


