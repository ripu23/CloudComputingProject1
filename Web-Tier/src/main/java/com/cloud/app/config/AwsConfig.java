package com.cloud.app.config;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Configuration;

import com.cloud.app.utility.Constants;

@Configuration
public class AwsConfig {

    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.standard().withRegion(Constants.REGION).build();
    }

    public AmazonEC2 amazonEC2() {
        return AmazonEC2ClientBuilder.standard().withRegion(Constants.REGION).build();
    }
}
