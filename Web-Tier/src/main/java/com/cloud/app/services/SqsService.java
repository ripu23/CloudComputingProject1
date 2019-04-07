package com.cloud.app.services;

import com.amazonaws.services.sqs.model.Message;

public interface SqsService {

    void addRequest(String requestId);

    int getNumMsgs();
    
    Message readMessage();

}
