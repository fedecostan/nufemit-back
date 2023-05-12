package com.nufemit.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.nufemit.exception.S3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

import static java.lang.Boolean.TRUE;

@Service
@Slf4j
public class S3Service {

    private final AmazonS3 amazonS3;

    private final String bucketName;

    @Autowired
    public S3Service(AmazonS3 amazonS3, @Value("${aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public Boolean uploadFile(MultipartFile file, String filename) {
        try {
            ObjectMetadata data = new ObjectMetadata();
            data.setContentType(file.getContentType());
            data.setContentLength(file.getSize());
            amazonS3.putObject(bucketName, filename, file.getInputStream(), data);
            return TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new S3Exception();
    }

    public ResponseEntity<ByteArrayResource> getFile(String filename) {
        try {
            log.info("FETCHING FILE: {}", filename);
            S3Object s3Object = amazonS3.getObject(bucketName, filename);
            ByteArrayResource resource = new ByteArrayResource(s3Object.getObjectContent().readAllBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new S3Exception();
        } catch (AmazonS3Exception e) {
            throw new EntityNotFoundException();
        }
    }
}
