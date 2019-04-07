package com.cloud.app.aws;

import com.amazonaws.services.sqs.model.Message;

public interface SqsOperations {

    Message receiveMessage();

    void deleteMessage(Message message);
    
    void putMessage(String message);

}
