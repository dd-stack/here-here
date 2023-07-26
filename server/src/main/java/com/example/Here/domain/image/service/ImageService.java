package com.example.Here.domain.image.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.Here.domain.image.config.AmazonConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public ImageService(AmazonS3 amazonS3, AmazonConfig amazonConfig) {
        this.amazonS3 = amazonS3;
        this.bucketName = amazonConfig.getBucketName();
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        File fileObj = convertMultiPartFileToFile(multipartFile);
        String fileName = UUID.randomUUID().toString() + "_" + fileObj.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public String deleteImage(String fileUrl) throws IllegalArgumentException {
        if (!fileUrl.contains("/")) {
            throw new IllegalArgumentException("Invalid file URL");
        }

        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File fileObj = new File(file.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(fileObj)) {
            outputStream.write(file.getBytes());
        }
        return fileObj;
    }


}
