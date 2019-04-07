package com.cloud.app.aws;

public interface S3Operations {

    void putObject(String key, String value);
    
    void createBucket();

}
