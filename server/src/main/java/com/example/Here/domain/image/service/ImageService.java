package com.example.Here.domain.image.service;


import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import com.example.Here.domain.image.config.AmazonConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {

    private final S3Client s3Client;
    private final String bucketName;

    public ImageService(S3Client s3Client, AmazonConfig amazonConfig) {
        this.s3Client = s3Client;
        this.bucketName = amazonConfig.getBucketName();
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {

        try {
            File file = convertMultiPartFileToFile(multipartFile);
            String fileName = UUID.randomUUID().toString() + "_" + file.getName();
            s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).build(),
                    RequestBody.fromFile(file));
            file.delete();
            return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(fileName).build()).toString();
        }

        catch (MaxUploadSizeExceededException e){
            throw new BusinessLogicException(ExceptionCode.FILE_SIZE_EXCEEDED);
        }

        catch (IOException e){
           throw new BusinessLogicException(ExceptionCode.FILE_UPLOAD_FAILED);
        }

    }

    public String deleteImage(String fileUrl) throws IllegalArgumentException {

        if (!fileUrl.contains("/"))
            throw new BusinessLogicException(ExceptionCode.IMAGEURL_NOT_VALID);


        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(fileName).build());
        return fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {

        File fileObj = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream outputStream = new FileOutputStream(fileObj)) {
            outputStream.write(file.getBytes());
        }

        return fileObj;
    }
}


