package com.cloud.app.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Configuration;

import com.cloud.app.utility.Constants;

@Configuration
public class AwsConfig {

    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.standard().withRegion(Constants.REGION).build();
    }

    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard().withRegion(Constants.REGION).build();
    }
}
