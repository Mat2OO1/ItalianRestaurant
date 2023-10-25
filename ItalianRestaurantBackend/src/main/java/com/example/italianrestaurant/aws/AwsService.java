package com.example.italianrestaurant.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsService {

    @Value("${aws.bucket-name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String uploadFile(byte[] fileBytes, String contentType) {
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        String imageName = generateImageName();
        s3Client.putObject(bucketName, imageName, inputStream, metadata);
        s3Client.setObjectAcl(bucketName, imageName, CannedAccessControlList.PublicRead);
        return imageName;
    }

    public void deleteFile(String objectUrl) {
        s3Client.deleteObject(bucketName, getImageNameFromUrl(objectUrl));
    }

    public void deleteAllImages() {
        List<S3ObjectSummary> objectSummaries = s3Client.listObjects(bucketName).getObjectSummaries();

        for (S3ObjectSummary objectSummary : objectSummaries) {
            String key = objectSummary.getKey();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
        }
    }


    public String getObjectUrl(String objectKey) {
        String region = s3Client.getRegionName();
        String endpoint = "s3." + region + ".amazonaws.com";
        return "https://" + bucketName + "." + endpoint + "/" + objectKey;
    }

    public String getImageNameFromUrl(String objectUrl) {
        String[] split = objectUrl.split("/");
        return split[split.length - 1];
    }
    private String generateImageName() {
        String prefix = "image_";
        String randomComponent = RandomStringUtils.randomAlphanumeric(6);
        return prefix + randomComponent;

    }
}
