package com.cloud.app.services;

import com.amazonaws.services.sqs.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloud.app.aws.SqsOperations;

@Component
public class SqsServiceImpl implements SqsService {

    @Autowired
    private SqsOperations sqsOperations;

    @Override
    public Message receiveMessage () {
        return sqsOperations.receiveMessage();
    }

    @Override
    public void deleteMessage(Message msg) {
        sqsOperations.deleteMessage(msg);
    }

	@Override
	public void putMessageInOutputQueue(String response) {
		sqsOperations.putMessage(response);
	}


}
