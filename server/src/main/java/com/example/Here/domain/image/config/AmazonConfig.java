/*package com.example.Here.domain.image.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextResourceLoader;
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
    public AmazonS3 amazonS3() {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

    }

    public String getBucketName() {
        return bucketName;
    }
}

 */
