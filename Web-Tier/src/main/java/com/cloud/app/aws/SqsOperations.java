package com.cloud.app.aws;

import com.amazonaws.services.sqs.model.Message;

public interface SqsOperations {

    void addRequestToQueue(String processId);

    int getNumMsgs();
    
    Message readMessageFromOutputQueue();
    
    void deleteMessage(Message message);

}
