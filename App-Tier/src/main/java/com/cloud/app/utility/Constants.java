package com.cloud.app.utility;

import com.amazonaws.regions.Regions;

public class Constants {

    public static final Regions REGION = Regions.US_WEST_1;
    public static final String INPUT_QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/468759062465/cc-input-queue";
    public static final String OUPUT_QUEUE_URL="https://sqs.us-west-1.amazonaws.com/468759062465/cc-output-queue";
    // Controls after how much time message is visible to others.
    public static final int MESSAGE_VISIBILITY = 60;
    // Controls how much to wait for a new message.
    // Normal App tier instances should shutdown immediately if there are no requests in the Q. Hence wait time is set to 0.
    public static final int MESSAGE_WAIT = 0;
    public static final String BUCKET_NAME = "shatrughnripushireen-cc-project1";
    public static final String VIDEO_URL = "http://206.207.50.7/getvideo";

}
