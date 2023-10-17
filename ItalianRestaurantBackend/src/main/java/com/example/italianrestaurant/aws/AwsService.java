package com.example.italianrestaurant.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsService {

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.secret-key}")
    private String secretKey;

    private final AmazonS3 s3Client;

    public void uploadFile(byte[] fileBytes) {
        try {
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/octet-stream");
            s3Client.putObject(bucketName, secretKey, inputStream, metadata);
            log.info("File uploaded successfully to S3.");
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
        } catch (AmazonClientException e) {
            log.error(e.getMessage());
        }
    }
}
