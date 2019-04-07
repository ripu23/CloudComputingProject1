package com.cloud.app.services;

import com.amazonaws.services.sqs.model.Message;

public interface SqsService {

    Message receiveMessage();

    void deleteMessage(Message message);
    
    void putMessageInOutputQueue(String response);

}
