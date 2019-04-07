package com.cloud.app.aws;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloud.app.config.AwsConfig;
import com.cloud.app.utility.Constants;

@Component
public class S3OperationsImpl implements S3Operations {

    @Autowired
    private AwsConfig awsConfig;

    @Override
    public void createBucket() {
        if (!awsConfig.amazonS3().doesBucketExistV2(Constants.BUCKET_NAME)) {
            awsConfig.amazonS3().createBucket(Constants.BUCKET_NAME);
        }
    }

    @Override
    public void putObject(String key, String value) {
        // Make sure the bucket exists before proceeding
        createBucket();
        awsConfig.amazonS3().putObject(Constants.BUCKET_NAME, key, value);
    }
}
