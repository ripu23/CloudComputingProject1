package com.cloud.app.utility;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.InstanceType;

import java.util.LinkedHashSet;
import java.util.Set;

public class Constants {

    public static final Regions REGION = Regions.US_WEST_1;
    public static final String INPUT_QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/468759062465/cc-input-queue";
    public static final String OUPUT_QUEUE_URL="https://sqs.us-west-1.amazonaws.com/468759062465/cc-output-queue";
    public static final String APP_AMI_ID = "ami-06a196542101d93be";
    public static final String SECURITY_GROUP_ID = "sg-0bfab16345ee5184f";
    public static final InstanceType APP_INSTANCE_TYPE = InstanceType.T2Micro;
    public static final String APP_INSTANCE_TAG = "app-tier";
    public static final String APP_INSTANCE_KEYPAIR = "aws-asu-student";
    // Allows EC2 instance to have access to AWS resources granted by this role.
    // Also allows us to avoid explicitly storing access keys in the application.
    public static final String APP_INSTANCE_PROFILE = "arn:aws:iam::468759062465:instance-profile/cloud-computing-role";
    public static final int POLL_INTERVAL = 5000; // 5 seconds
    public static final int MAX_APP_INSTANCES = 19;
    public static final Set<String> ALL_APP_INSTANCE_NAMES = new LinkedHashSet<>(MAX_APP_INSTANCES);
    // Controls how much to wait for a new message. 20s means long polling.
    public static final int MESSAGE_WAIT = 20;
    public static final int MESSAGE_VISIBILITY = 60;
    public static final String PROCESS_ID = "processId";

    static {
        for (int i = 0; i < MAX_APP_INSTANCES; i++)
            ALL_APP_INSTANCE_NAMES.add("App Instance " + (i+1));
    }
}
