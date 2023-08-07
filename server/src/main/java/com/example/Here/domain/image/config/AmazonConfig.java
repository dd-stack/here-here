package com.example.Here.domain.image.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

    @Value("${AWS_BUCKETNAME}")
    private String bucketName;

    @Value("${AWS_REGION}")
    private String region;

    @Value("${AWS_ACCESSKEY}")
    private String accessKey;

    @Value("${AWS_SECRETKEY}")
    private String secretKey;

    @Bean
    public S3Client amazonS3() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String getBucketName() {
        return bucketName;
    }
}


