package com.travelsphere.infra.localstack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;

@Component
public class LocalStackInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LocalStackInitializer.class);

    private final String endpoint;
    private final String region;
    private final String accessKey;
    private final String secretKey;
    private final String documentsBucket;
    private final String photosBucket;

    public LocalStackInitializer(
            @Value("${app.s3.endpoint:http://localhost:4566}") String endpoint,
            @Value("${app.s3.region:ap-south-1}") String region,
            @Value("${app.s3.access-key:fake-access-key}") String accessKey,
            @Value("${app.s3.secret-key:fake-secret-key}") String secretKey,
            @Value("${app.s3.bucket-documents:travelsphere-documents}") String documentsBucket,
            @Value("${app.s3.bucket-photos:travelsphere-photos}") String photosBucket) {
        this.endpoint = endpoint;
        this.region = region;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.documentsBucket = documentsBucket;
        this.photosBucket = photosBucket;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            S3Client s3Client = S3Client.builder()
                    .endpointOverride(URI.create(endpoint))
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .forcePathStyle(true)
                    .build();

            createBucketIfNotExists(s3Client, documentsBucket);
            createBucketIfNotExists(s3Client, photosBucket);

            log.info("LocalStack S3 buckets initialized successfully");
        } catch (Exception e) {
            log.warn("Failed to initialize LocalStack S3 buckets: {}", e.getMessage());
        }
    }

    private void createBucketIfNotExists(S3Client s3Client, String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            log.info("S3 bucket '{}' already exists", bucketName);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
                log.info("Created S3 bucket '{}'", bucketName);
            }
        }
    }
}
