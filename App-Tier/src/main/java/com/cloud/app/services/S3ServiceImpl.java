package com.cloud.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloud.app.aws.S3Operations;

@Component
public class S3ServiceImpl implements S3Service {

    @Autowired
    private S3Operations s3Operations;

    @Override
    public void putObject(String key, String value) {
        s3Operations.putObject(key, value);
    }

}
